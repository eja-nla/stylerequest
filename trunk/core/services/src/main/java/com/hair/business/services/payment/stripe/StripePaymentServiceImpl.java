package com.hair.business.services.payment.stripe;

import static java.util.Optional.ofNullable;
import static org.slf4j.LoggerFactory.getLogger;

import com.hair.business.beans.entity.AddOn;
import com.hair.business.beans.entity.Merchant;
import com.hair.business.beans.entity.PaymentTrace;
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

import org.joda.time.DateTime;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

/**
 * Stripe payment processor
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
        Stripe.apiKey = "sk_test_VYfFv7fFJgpX658QJ0Q8WddE00SgjFVMuC";
//        Stripe.apiKey = System.getProperty("stripe.secret.key");
    }

    public static void main(String[] args) throws StripeException {
        StripePaymentServiceImpl service = new StripePaymentServiceImpl(null);

//        String id = service.createCustomer("intellijc1");
//        String intent = service.createPaymentIntent(2222, id);
//        service.cancelPayment(intent); // pending, so we can cancel

 //       service.chargeNow("cus_HaKp6AUpG8D4aL", 1200, "acct_1H0draCQiLLG2cvn", null);

  //      String intent = service.createPaymentIntent(2223, "cus_HaKp6AUpG8D4aL", null); // lets try to pay with a valid user and payment method

        String res = service.authorize("cus_HaKp6AUpG8D4aL", 3000, "acct_1H0draCQiLLG2cvn","test service from intellij", null);
        service.capture(3000, res, null);
    }


    @Override
    public String createPaymentIntent(int amount, String customeStripeId, PaymentTrace paymentTrace) {

        try {
            PaymentIntentCreateParams createParams = new PaymentIntentCreateParams.Builder()
                    .setCurrency(CURRENCY)
                    .setAmount((long) amount)
                    .setCustomer(customeStripeId)
                    .setSetupFutureUsage(PaymentIntentCreateParams.SetupFutureUsage.OFF_SESSION)
                    .build();

            PaymentIntent intent = PaymentIntent.create(createParams);

            if (intent.getStatus().equals("succeeded")) {
                logger.info("Payment intent succeeded for Stripe customerID={} amount={} paymentdID={}", customeStripeId, amount, intent.getId());
            } else  {
                logger.info("Payment intent failure for Stripe customerID={} amount={} paymentdID={} status='{}'", customeStripeId, amount, intent.getId(), intent.getStatus());
            }

            // we store a copy of txn IDs locally for payment reference/troubleshooting
            ofNullable(paymentTrace).ifPresent(pi -> pi.getPaymentTxnIds().put(intent.getId() + DateTime.now().toString("mmddyy_hhmmss"), String.format("createPaymentIntent for amount %s", amount)));
            return intent.getId();
        } catch (StripeException e) {
            logger.warn("Payment intent error from Stripe for customerId: {} amount: {} error:{}", customeStripeId, amount, e.getMessage());
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

        return customer.getId();
    }

    /**
     * We have received an authcode from Stripe after a successful onboarding
     * https://stripe.com/docs/connect/express-accounts#onboarding-express-accounts-outside-of-your-platforms-country
     * we now take the authcode back to fetch the new connectedAccount (i.e. merchant) ID so we can store here.
     * */
    @Override
    public String createMerchantAccount(String authCode, String refId){

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

        return response.getStripeUserId();
    }


    /**
     * Authorize a charge for later capture
     * */

    @Override
    public String authorize(String customerStripeId, int amount, String merchantStripeId, String chargeDescription, PaymentTrace paymentTrace) {
        try {

            final ChargeCreateParams.TransferData transferDataParams = ChargeCreateParams.TransferData.builder()
                    .setDestination(merchantStripeId)
                    .build();

            final ChargeCreateParams chargeParams = new ChargeCreateParams.Builder()
                    .setCurrency(CURRENCY)
                    .setAmount((long) amount)
                    .setDescription(chargeDescription)
                    .setCustomer(customerStripeId)
                    .setApplicationFeeAmount((long) commission(amount)) //attach our commission
                    .setTransferData(transferDataParams)
                    .setCapture(false)
                    .build();
            final Charge chargeRes = Charge.create(chargeParams);

            if (chargeRes.getStatus().equals("succeeded")) {
                logger.info("Authorization succeeded for Stripe customerID={} amount={} paymentdID={} description='{}'", customerStripeId, amount, chargeRes.getId(), chargeDescription);
            } else  {
                logger.info("Authorization failure for Stripe customerID={} amount={} paymentdID={} status='{}' description='{}'", customerStripeId, amount, chargeRes.getId(), chargeRes.getStatus(), chargeDescription);
            }
            ofNullable(paymentTrace).ifPresent(pi -> pi.getPaymentTxnIds().put(chargeRes.getId() + DateTime.now().toString("mmddyy_hhmmss"), String.format("authorize for amount %s", amount)));

            return chargeRes.getId();

        } catch (StripeException e) {
            logger.warn("Authorization error from Stripe for customerId: {} amount: {} error:{}", customerStripeId, amount, e.getMessage());
            throw new PaymentException(e);
        }
    }

    @Override
    public void refund(int amount, String payId, PaymentTrace paymentTrace) {
        Refund refund;
        try {
            refund = Refund.create(RefundCreateParams.builder()
                    .setAmount((long) amount)
                    .setPaymentIntent(payId)
                    .build());
            if(!refund.getStatus().equals("succeeded")){
                throw new PaymentException(String.format("Unable to complete refund for paymentId %s, amount %s", payId, amount));
            }
        } catch (StripeException e) {
            throw new PaymentException(e);
        }

        ofNullable(paymentTrace).ifPresent(pi -> pi.getPaymentTxnIds().put(refund.getId() + DateTime.now().toString("mmddyy_hhmmss"), String.format("refund for amount %s", amount)));

    }

    @Override
    public void cancelPayment(String paymentIntentId, PaymentTrace paymentTrace){
        PaymentIntent intent;
        try {
            intent = PaymentIntent.retrieve(paymentIntentId);
            intent.cancel();
            ofNullable(paymentTrace).ifPresent(pi -> pi.getPaymentTxnIds().put(intent.getId() + DateTime.now().toString("mmddyy_hhmmss"), String.format("cancelPayment for amount %s", paymentIntentId)));
        } catch (StripeException e) {
            throw new PaymentException(String.format("Unable to cancel payment with intentId %s. Error: %s", paymentIntentId, e.getMessage()));
        }
    }

    @Override
    public void capture(int amount, String chargeId, PaymentTrace paymentTrace) {
        Charge charge;
        try {
            charge = Charge.retrieve(chargeId);

            final long fees = commission(amount);
            final ChargeCaptureParams.TransferData transferDataParams = ChargeCaptureParams.TransferData.builder()
                    .build();

            ChargeCaptureParams capParams = ChargeCaptureParams.builder()
                    .setAmount((long) amount)
                    .setTransferData(transferDataParams)
                    .setApplicationFeeAmount(fees)
                    .build();

            charge.capture(capParams);

            if (charge.getStatus().equals("succeeded")) {
                ofNullable(paymentTrace).ifPresent(pi -> pi.getPaymentTxnIds().put(charge.getId() + DateTime.now().toString("mmddyy_hhmmss"), String.format("capture for amount %s", amount)));
                logger.info("Capture succeeded for Stripe ChargeId={} amount={}", chargeId, amount);
            }
        } catch (StripeException e) {
            logger.warn("Capture error from Stripe for ChargeId: {} amount: {} error:{}", chargeId, amount, e.getMessage());
            throw new PaymentException(e);

        }
    }

    @Override
    public void chargeNow(String stripeCustomerId, int amount, String merchantStripeId, PaymentTrace paymentTrace) {
        try {
            // List the customer's payment methods to find one to charge
            final PaymentMethodListParams listParams = new PaymentMethodListParams.Builder()
                    .setCustomer(stripeCustomerId)
                    .setType(PaymentMethodListParams.Type.CARD)
                    .build();

            final long fees = commission(amount);
            final PaymentIntentCreateParams.TransferData transferDataParams = PaymentIntentCreateParams.TransferData.builder()
                    .setDestination(merchantStripeId)
                    .build();

            final PaymentMethodCollection paymentMethods = PaymentMethod.list(listParams);
            final PaymentIntentCreateParams createParams = new PaymentIntentCreateParams.Builder()
                    .setCurrency(CURRENCY)
                    .setAmount((long) amount)
                    .setApplicationFeeAmount(fees)
                    .setTransferData(transferDataParams)
                    .setPaymentMethod(paymentMethods.getData().get(0).getId())
                    .setCustomer(stripeCustomerId)
                    .setConfirm(true)
                    .setOffSession(PaymentIntentCreateParams.OffSession.ONE_OFF).build();

            final PaymentIntent paymentIntent = PaymentIntent.create(createParams);

            if (paymentIntent.getStatus().equals("succeeded")) {
                ofNullable(paymentTrace).ifPresent(pi -> pi.getPaymentTxnIds().put(paymentIntent.getId() + DateTime.now().toString("mmddyy_hhmmss"), String.format("chargeNow for amount %s", amount)));
                logger.info("Payment succeeded for Stripe customerID={} amount={} net={} fees={}", stripeCustomerId, amount, fees);
            }
        } catch (StripeException e) {
            logger.warn("Payment error from Stripe for customerId: {} amount: {} error:{}", stripeCustomerId, amount, e.getMessage());
            throw new PaymentException(e);
        }
    }

    private int commission(int amount) {
        // Take a 15% cut.
        return (int) (0.15 * amount);
    }

    private int calculateOrderAmount(List<AddOn> addOns) {
        // Replace this constant with a calculation of the order's amount
        // Calculate the order total on the server to prevent
        // people from directly manipulating the amount on the client
        int total = 0;
        for (AddOn each : addOns){
            total += each.getTotalAmount();
        }

        return total * 100;
    }
}
