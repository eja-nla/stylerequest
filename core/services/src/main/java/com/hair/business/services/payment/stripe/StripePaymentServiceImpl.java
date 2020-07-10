package com.hair.business.services.payment.stripe;

import static org.slf4j.LoggerFactory.getLogger;

import com.hair.business.beans.entity.AddOn;
import com.hair.business.beans.entity.Merchant;
import com.hair.business.beans.entity.StyleRequest;
import com.hair.business.beans.entity.TransactionResult;
import com.hair.business.beans.helper.PaymentOperation;
import com.hair.business.dao.datastore.abstractRepository.Repository;
import com.hair.business.services.client.retry.RetryWithExponentialBackOff;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.Customer;
import com.stripe.model.PaymentIntent;
import com.stripe.model.PaymentMethod;
import com.stripe.model.PaymentMethodCollection;
import com.stripe.model.Refund;
import com.stripe.model.oauth.TokenResponse;
import com.stripe.net.OAuth;
import com.stripe.net.RequestOptions;
import com.stripe.param.ChargeCaptureParams;
import com.stripe.param.ChargeCreateParams;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.PaymentMethodListParams;
import com.stripe.param.RefundCreateParams;
import com.x.business.exception.PaymentException;
import com.x.business.utilities.Assert;

import org.slf4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.inject.Inject;

/**
 * Stripe payment processor
 *
 *  *  Payment workflow looks like this:
 *  *     //1. 7 days or less before appointment, charge the known total to the card (could be the estimatedPrice with or without addons)
 *  *              We need 7 days or less because payment auth and charge only works within 7 days between this auth and the corresponding charge in step 3
 *  *     //2. if customer/merchant cancel before the day, release the charge. No payment needed
 *  *     //3. if they cancel on the day or don't show up, capture 20% of charge from 1
 *  *     //4. when appt is completed, cancel the charge from 1, then authorize and charge (chargeNow) the total (style+addOn)
 *
 * Created by olukoredeaguda on 03/07/2020.
 */
public class StripePaymentServiceImpl implements StripePaymentService {

    private static final Logger logger = getLogger(StripePaymentServiceImpl.class);
    private final Repository repository;

    private static final String CURRENCY = "USD";

    @Inject
    public StripePaymentServiceImpl(Repository repository) {
        this.repository = repository;
        Stripe.apiKey = System.getProperty("stripe.secret.key");
    }

    @Override
    public TransactionResult createPaymentIntent(int amount, String customerStripeId) {

        try {
            PaymentIntentCreateParams createParams = new PaymentIntentCreateParams.Builder()
                    .setCurrency(CURRENCY)
                    .setAmount((long) amount)
                    .setCustomer(customerStripeId)
                    .setSetupFutureUsage(PaymentIntentCreateParams.SetupFutureUsage.OFF_SESSION)
                    .build();

            PaymentIntent intent = PaymentIntent.create(createParams);

            if (intent.getStatus().equals("succeeded")) {
                logger.info("Payment intent succeeded for Stripe customerID={} amount={} paymentdID={}", customerStripeId, amount, intent.getId());
            } else  {
                logger.info("Payment intent failure for Stripe customerID={} amount={} paymentdID={} status='{}'", customerStripeId, amount, intent.getId(), intent.getStatus());
            }

            return new TransactionResult(intent.getId(), PaymentOperation.INTENT, amount, intent.getStatus());
        } catch (StripeException e) {
            logger.warn("Payment intent error from Stripe for customerId: {} amount: {} error:{}", customerStripeId, amount, e.getMessage());
            throw new PaymentException(e);
        }
    }

    @Override
    public String createCustomer(String internalCustomerId) {
        final CustomerCreateParams customerParams = new CustomerCreateParams.Builder()
                .build();
        final Customer customer = RetryWithExponentialBackOff.execute(() -> {
            try {
                return Customer.create(customerParams);
            } catch (StripeException e) {
                throw new PaymentException(String.format("Unable to create new Stripe payment Profile for user %s. Error message %s", internalCustomerId, e.getMessage()));
            }
        });

        logger.info("New customer account created successfully for internalId={} stripeId={}", internalCustomerId, customer.getId());
        return customer.getId();
    }

    /**
     * We have received an authcode from Stripe after a successful onboarding
     * https://stripe.com/docs/connect/express-accounts#onboarding-express-accounts-outside-of-your-platforms-country
     * we now take the authcode back to fetch the new connectedAccount (i.e. merchant) ID so we can store here.
     * */
    @Override
    public String createMerchant(String authCode, String refId){

        final Map<String, Object> params = new HashMap<>();
        params.put("grant_type", "authorization_code");
        params.put("code", authCode);

        TokenResponse response;

        try {
            RequestOptions options = RequestOptions.builder()
                    .setMaxNetworkRetries(3)
                    .build();
            response = OAuth.token(params, options);
        } catch (StripeException e) {
            throw new PaymentException(String.format("Unable to create new Stripe payment Profile for user %s authcode %s. Error message %s", refId, authCode, e.getMessage()));
        }

        logger.info("Successfully created new merchant account with StripeId={} internalId={}", response.getStripeUserId(), refId);

        Merchant merchant = repository.findOne(Long.valueOf(refId), Merchant.class);
        Assert.notNull(merchant, String.format("Merchant with ID %s not found", refId));

        merchant.setPaymentId(response.getStripeUserId());

        repository.saveOne(merchant);

        return response.getStripeUserId();
    }

    @Override
    public TransactionResult authorize(StyleRequest styleRequest, String chargeDescription) {
        Assert.notNull(styleRequest, String.format("Authorization failed. StyleRequest with ID %s not found", styleRequest.getId()));
        int amount = calculateOrderAmount(styleRequest.getStyle().getPrice(), styleRequest.getAddOns());
        Charge chargeRes;
        try {

            final ChargeCreateParams.TransferData transferDataParams = ChargeCreateParams.TransferData.builder()
                    .setDestination(styleRequest.getMerchant().getPaymentId())
                    .build();

            final ChargeCreateParams chargeParams = new ChargeCreateParams.Builder()
                    .setCurrency(CURRENCY)
                    .setAmount((long) amount)
                    .setDescription(chargeDescription)
                    .setCustomer(styleRequest.getCustomer().getPaymentId())
                    .setApplicationFeeAmount((long) commission(amount)) //attach our commission
                    .setTransferData(transferDataParams)
                    .setCapture(false)
                    .build();
            chargeRes = Charge.create(chargeParams);

            if (chargeRes.getStatus().equals("succeeded")) {
                logger.info("Authorization succeeded for Stripe. StyleRequestId={} customerID={} amount={} paymentdID={} description='{}'",
                        styleRequest.getId(), styleRequest.getCustomer().getPaymentId(), amount, chargeRes.getId(), chargeDescription);
            } else  {
                logger.info("Authorization failure for Stripe. StyleRequestId={} customerID={} amount={} paymentdID={} status='{}' description='{}'",
                        styleRequest.getId(), styleRequest.getCustomer().getPaymentId(), amount, chargeRes.getId(), chargeRes.getStatus(), chargeDescription);
            }

        } catch (StripeException e) {
            logger.warn("Authorization error from Stripe for StyleRequestId={} customerId: {} amount: {} error:{}", styleRequest.getId(), styleRequest.getCustomer().getPaymentId(), amount, e.getMessage());
            throw new PaymentException(e);
        }

        return new TransactionResult(chargeRes.getId(), PaymentOperation.AUTHORIZE, amount, chargeRes.getStatus());
    }


    /**
     * Authorize a charge for later capture
     * */

    @Override
    public TransactionResult authorize(Long styleRequestId, String chargeDescription) {
        final StyleRequest styleRequest = repository.findOne(styleRequestId, StyleRequest.class);
        return authorize(styleRequest, chargeDescription);
    }

    /**
     * Only the merchant can issue a refund so its safe to take amount param from them
     * If amount is given, we use it. If it's not i.e. 0, we refund the totals of addons
     * We do this because the amount a merchant might choose to refund may not necessarily be the ahe price of the style request or addons e.g. "gift to client"
     * */
    @Override
    public TransactionResult refund(StyleRequest styleRequest, int amount, List<AddOn> addOns) {
        Assert.notNull(styleRequest, String.format("Refund failed. StyleRequest with ID %s not found", styleRequest.getId()));
        Refund refund;

        Optional<TransactionResult> settled = styleRequest.getTransactionResults().stream().filter(t -> t.getOperation().equals(PaymentOperation.CAPTURE)).findFirst();

        if(!settled.isPresent()) {
            throw new PaymentException(String.format("Refund failed for stylerequestId %s. Unable to find a previously settled transaction", styleRequest.getId()));
        }

        int total = amount <= 0 ? calculateOrderAmount(0, addOns) : amount;
        try {
            refund = Refund.create(RefundCreateParams.builder()
                    .setAmount((long) total)
                    .setCharge(settled.get().getOwnId())
                    .build());
            if(!refund.getStatus().equals("succeeded")){
                throw new PaymentException(String.format("Unable to complete refund for styleReqiestId %s", styleRequest.getId()));
            }
        } catch (StripeException e) {
            throw new PaymentException(e);
        }

        return new TransactionResult(refund.getId(), PaymentOperation.REFUND, Math.toIntExact(refund.getAmount()), refund.getStatus());
    }

    @Override
    public TransactionResult refund(Long styleRequestId, int amount, List<AddOn> addOns) {
        final StyleRequest styleRequest = repository.findOne(styleRequestId, StyleRequest.class);
        return refund(styleRequest, amount, addOns);
    }

    @Override
    public TransactionResult cancel(Long styleRequestId){
        final StyleRequest styleRequest = repository.findOne(styleRequestId, StyleRequest.class);
        return cancel(styleRequest);
    }

    @Override
    public TransactionResult cancel(StyleRequest styleRequest) {
        Assert.notNull(styleRequest, String.format("Cancel failed. StyleRequest with ID %s not found", styleRequest.getId()));

        Optional<TransactionResult> authorized = styleRequest.getTransactionResults().stream().filter(t -> t.getOperation().equals(PaymentOperation.AUTHORIZE)).findFirst();

        if(!authorized.isPresent()) {
            throw new PaymentException(String.format("Cancel failed for stylerequestId %s. Unable to find a previously settled transaction", styleRequest.getId()));
        }

        PaymentIntent intent;
        try {
            intent = PaymentIntent.retrieve(authorized.get().getOwnId());
            intent.cancel();
        } catch (StripeException e) {
            throw new PaymentException(String.format("Unable to cancel payment with StyleRequestId=%s Error: %s", styleRequest.getId(), e.getMessage()));
        }

        return new TransactionResult(intent.getId(), PaymentOperation.CANCEL, Math.toIntExact(intent.getAmount()), intent.getStatus());
    }

    @Override
    public TransactionResult capture(StyleRequest styleRequest) {

        return capture(styleRequest, calculateOrderAmount(styleRequest.getStyle().getPrice(), styleRequest.getAddOns()));
    }

    /**
     * We may want to capture a fraction of the price e.g. for no shows, we charge 15%
     * This must not be exposed to client, the amount to capture must always be calculated from the backend
     * */
    @Override
    public TransactionResult capture(StyleRequest styleRequest, int price) {

        Assert.notNull(styleRequest, String.format("Capture failed. StyleRequest with ID %s not found", styleRequest.getId()));

        Optional<TransactionResult> authorized = styleRequest.getTransactionResults().stream().filter(t -> t.getOperation().equals(PaymentOperation.AUTHORIZE)).findFirst();
        if(!authorized.isPresent()) {
            throw new PaymentException(String.format("Capture failed for stylerequestId %s. Unable to find a previously settled transaction", styleRequest.getId()));
        }

        Charge charge;
        try {
            charge = Charge.retrieve(authorized.get().getOwnId());

            final long fees = commission(price);
            final ChargeCaptureParams.TransferData transferDataParams = ChargeCaptureParams.TransferData.builder()
                    .build();

            ChargeCaptureParams capParams = ChargeCaptureParams.builder()
                    .setAmount((long) price)
                    .setTransferData(transferDataParams)
                    .setApplicationFeeAmount(fees)
                    .build();

            charge.capture(capParams);

            if (charge.getStatus().equals("succeeded")) {
                logger.info("Capture succeeded for Stripe ChargeId={} amount={}", authorized.get().getOwnId(), price);
            } else  {
                logger.info("Capture failure for Stripe. StyleRequestId={} customerID={} amount={} paymentdID={} status='{}'",
                        styleRequest.getId(), styleRequest.getCustomer().getPaymentId(), price, charge.getId(), charge.getStatus());
            }
        } catch (StripeException e) {
            logger.warn("Capture error from Stripe for ChargeId: {} amount: {} error:{}", authorized.get().getOwnId(), price, e.getMessage());
            throw new PaymentException(e);

        }

        return new TransactionResult(charge.getId(), PaymentOperation.CAPTURE, price, charge.getStatus());
    }

    /**
     * We have the ID, so we fetch the stylerequest and capture the amount in it
     * */
    @Override
    public TransactionResult capture(Long styleRequestId) {
        final StyleRequest styleRequest = repository.findOne(styleRequestId, StyleRequest.class);
        return capture(styleRequest, calculateOrderAmount(styleRequest.getStyle().getPrice(), styleRequest.getAddOns()));
    }

    @Override
    public TransactionResult chargeNow(StyleRequest styleRequest, List<AddOn> addOns) {
        return chargeNow(styleRequest.getId(), addOns);
    }

    @Override
    public TransactionResult chargeNow(Long styleRequestId, List<AddOn> addOns) {
        final StyleRequest styleRequest = repository.findOne(styleRequestId, StyleRequest.class);
        Assert.notNull(styleRequest, String.format("ChargeNow failed. StyleRequest with ID %s not found", styleRequestId));
        int amount = calculateOrderAmount(styleRequest.getStyle().getPrice(), addOns);

        PaymentIntent paymentIntent;
        try {
            // List the customer's payment methods to find one to charge
            final PaymentMethodListParams listParams = new PaymentMethodListParams.Builder()
                    .setCustomer(styleRequest.getCustomer().getPaymentId())
                    .setType(PaymentMethodListParams.Type.CARD)
                    .build();

            final long fees = commission(amount);
            final PaymentIntentCreateParams.TransferData transferDataParams = PaymentIntentCreateParams.TransferData.builder()
                    .setDestination(styleRequest.getMerchant().getPaymentId())
                    .build();

            final PaymentMethodCollection paymentMethods = PaymentMethod.list(listParams);
            final PaymentIntentCreateParams createParams = new PaymentIntentCreateParams.Builder()
                    .setCurrency(CURRENCY)
                    .setAmount((long) amount)
                    .setApplicationFeeAmount(fees)
                    .setTransferData(transferDataParams)
                    .setPaymentMethod(paymentMethods.getData().get(0).getId())
                    .setCustomer(styleRequest.getCustomer().getPaymentId())
                    .setConfirm(true)
                    .setOffSession(PaymentIntentCreateParams.OffSession.ONE_OFF).build();

            paymentIntent = PaymentIntent.create(createParams);

            if (paymentIntent.getStatus().equals("succeeded")) {
                logger.info("Payment succeeded for Stripe customerID={} amount={} net={} fees={}", styleRequest.getCustomer().getPaymentId(), amount, fees);
            }
        } catch (StripeException e) {
            logger.warn("Payment error from Stripe for customerId: {} amount: {} error:{}", styleRequest.getCustomer().getPaymentId(), amount, e.getMessage());
            throw new PaymentException(e);
        }

        return new TransactionResult(paymentIntent.getId(), PaymentOperation.PAYNOW, amount, paymentIntent.getStatus());
    }

    @Override
    public int calculateOrderAmount(int basePrice, List<AddOn> addOns) {
        int total = basePrice;
        if(addOns!=null){
            for (AddOn each : addOns){
                total += each.getTotalAmount();
            }
        }
        return total;
    }

    private int commission(int amount) {
        return (int) (COMMISSION * amount);
    }
}
