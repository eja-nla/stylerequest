package com.hair.business.services.payment.braintree;

import static org.slf4j.LoggerFactory.getLogger;

import com.braintreegateway.BraintreeGateway;
import com.braintreegateway.ClientTokenRequest;
import com.braintreegateway.CustomerRequest;
import com.braintreegateway.PaymentMethodRequest;
import com.braintreegateway.Result;
import com.braintreegateway.Transaction;
import com.braintreegateway.TransactionRequest;
import com.hair.business.beans.constants.PaymentType;
import com.hair.business.beans.entity.AddOn;
import com.hair.business.beans.entity.Customer;
import com.hair.business.beans.entity.PaymentItem;
import com.hair.business.beans.entity.PaymentMethod;
import com.hair.business.beans.entity.StyleRequest;
import com.hair.business.beans.entity.StyleRequestPayment;
import com.hair.business.beans.helper.PaymentStatus;
import com.hair.business.dao.datastore.abstractRepository.Repository;
import com.hair.business.services.payment.PaymentService;
import com.x.business.exception.PaymentException;
import com.x.business.utilities.Assert;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import java.math.BigDecimal;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * Braintree payment processor
 *
 * Created by olukoredeaguda on 06/03/2017.
 */
public class BraintreePaymentServiceImpl implements PaymentService {

    private static final Logger logger = getLogger(BraintreePaymentServiceImpl.class);
    private final BraintreeGateway gateway;
    private final Repository repository;

    @Inject
    public BraintreePaymentServiceImpl(Provider<BraintreeGateway> gatewayProvider, Repository repository) {
        this.gateway = gatewayProvider.get();
        this.repository = repository;
    }

    @Override
    public StyleRequest authorize(String nonce, final Long styleRequestId) {
        StyleRequest styleRequest = repository.findOne(styleRequestId, StyleRequest.class);
        return authorize(nonce, styleRequest);
    }

    @Override
    public StyleRequest authorize(String nonce, final StyleRequest styleRequest) {
        Assert.notNull(styleRequest, styleRequest.getStyle());

        Customer customer = styleRequest.getCustomer();
        Assert.notNull(customer.getId(), customer.getPayment());

        final double price = styleRequest.getStyle().getPrice();
        final double tax = computeTax("USA", price);

        final Transaction result = createTransaction(nonce, Long.toString(styleRequest.getId()), customer.getPaymentId(), price, tax,false);

        final StyleRequestPayment authorizedPayment = createPayment(result, styleRequest.getMerchant().getId(), PaymentStatus.AUTHORIZED);
        styleRequest.setAuthorizedPayment(authorizedPayment);
        logger.debug("Successfully authorized payment amount {} for style request {}", authorizedPayment.getTotalAmount(), styleRequest.getId());

        return styleRequest;
    }

    @Override
    public StyleRequest settlePreAuthPayment(StyleRequest styleRequest) {
        Assert.notNull(styleRequest, "Stylerequest cannot be null");

        StyleRequestPayment srPayment = styleRequest.getAuthorizedPayment();
        Assert.notNull(srPayment, "Stylerequest authorized payment cannot be null");
        Assert.isTrue(srPayment.getPaymentStatus() == PaymentStatus.AUTHORIZED, "Settling an unauthorized transaction is forbidden");

        String authorizedId = srPayment.getTransactionId();
        Assert.notNull(authorizedId, "Pre-Authorized Transaction must have an ID");

        Transaction transaction = settleTransaction(authorizedId, styleRequest.getStyle().getPrice(), styleRequest.getAddOns());

        final StyleRequestPayment settledPayment = createPayment(transaction, styleRequest.getMerchant().getId(), PaymentStatus.SETTLED);
        styleRequest.setSettledPayment(settledPayment);

        return styleRequest;
    }

    @Override
    public void deductNonPreAuthPayment(String nonce, List<AddOn> items) {

//        Transaction transaction = settleTransaction(nonce, items);
//        final StyleRequestPayment settledPayment = createPayment(transaction, styleRequest);
//        repository.saveOne(styleRequest);
    }

    @Override
    public double computeTax(String countryCode, double itemPrice) {

        // goo fetch the rate from a provider & store it. If request fails, get from local cache.
        return itemPrice / 10;
    }

    @Override
    public void updatePayment(Long customerId, PaymentMethod paymentMethod, PaymentType paymentType, boolean isDefault) {

    }

    @Override
    public void updatePayment(Long customerId, PaymentMethod paymentMethod, PaymentType paymentType, String nonce, boolean isDefault) {
        Customer customer = repository.findOne(customerId, Customer.class);
        customer.getPayment().getPaymentItems().add(new PaymentItem(paymentType, paymentMethod, isDefault));
        addPaymentMethod(nonce, paymentMethod);

        repository.saveOne(customer);
    }

    @Override
    public void refund(Long styleRequestId, double amount) {

        Assert.notNull(styleRequestId);

        StyleRequest request = repository.findOne(styleRequestId, StyleRequest.class);
        Assert.notNull(request, "Stylerequest with ID " + styleRequestId + "cannot be found");
        Assert.notNull(request.getSettledPayment());

        refund(Long.toString(request.getSettledPayment().getId()), new BigDecimal(amount));
    }

    @Override
    public Result refund(String transactionId, BigDecimal amount) {
        Result<Transaction> result;
        if (amount == null || amount.compareTo(BigDecimal.ZERO) == 0) {
            result = gateway.transaction().refund(transactionId);
        } else {
            result = gateway.transaction().refund(transactionId, amount);
        }
        if (result != null && !result.isSuccess()){
            logger.error("Braintree refund request failed: {}", result.getMessage());
        }

        return result;
    }


    @Override
    public Transaction settleTransaction(String preAuthorisedTransactionID, double price, List<AddOn> addOnItems) {
        for (AddOn addOn: addOnItems) {
            price += addOn.getAmount();
        }

        return settleTransaction(preAuthorisedTransactionID, price);
    }

    @Override
    public Transaction settleTransaction(String transactionId, double amount) {
        Result<Transaction> result = gateway.transaction().submitForSettlement(transactionId, BigDecimal.valueOf(amount));

        if (!result.isSuccess()) {
            throw new PaymentException("Braintree settle transaction request failed: " + result.getMessage());
        }
        return result.getTarget();
    }


    @Override
    public String issueClientToken(final String entityId) {
        final ClientTokenRequest clientTokenRequest = new ClientTokenRequest();

        if (StringUtils.isNotEmpty(entityId)) {
            clientTokenRequest.customerId(entityId);
        }

        String result = gateway.clientToken().generate(clientTokenRequest);

        if (StringUtils.isEmpty(result)){
            logger.warn("Failed to issue token for entity {}", entityId);
        }
        return result;
    }

    @Override
    public String createProfile(String userId, String nonce) {
        CustomerRequest request = new CustomerRequest()
                .customerId(userId)
                .paymentMethodNonce(nonce);

        Result<com.braintreegateway.Customer> result = gateway.customer().create(request);

        if (!result.isSuccess()){
            logger.error("Braintree create customer request failed: {}", result.getMessage());
        }

        return result.getTarget().getId();
    }


    @Override
    public Transaction createTransaction(String nonce, String orderId, String paymentId, double totalAmount, double taxAmount, boolean isSettled) {
        Assert.notNull(paymentId,"Payment ID cannot be null");
        Assert.notNull(nonce, "Nonce cannot be null");
        final TransactionRequest request = new TransactionRequest()
                .customerId(paymentId)
                .amount(BigDecimal.valueOf(totalAmount))
                .taxAmount(BigDecimal.valueOf(taxAmount)) //important!! otherwise the interchange reduction won't apply
                .orderId(orderId)
                .paymentMethodNonce(nonce)
                .options()
                    .submitForSettlement(isSettled)
                    .done();

        final Result result = gateway.transaction().sale(request);

        if (!result.isSuccess()){
            throw new PaymentException("Payment authorization failed with message: " + result.getMessage());
        }
        return (Transaction) result.getTarget();
    }


    @Override
    public boolean addPaymentMethod(String nonce, PaymentMethod paymentMethod) {
        PaymentMethodRequest request = new PaymentMethodRequest()
                .customerId(paymentMethod.getCustomerId())
                .paymentMethodNonce(nonce);
        Result<? extends com.braintreegateway.PaymentMethod> result = gateway.paymentMethod().create(request);

        if (!result.isSuccess()){
            logger.error("Braintree add new payment method request failed: {}", result.getMessage());
        }

        return result.isSuccess();
    }

    private StyleRequestPayment createPayment(Transaction transaction, Long merchantId, PaymentStatus paymentStatus){
        final StyleRequestPayment settledPayment = new StyleRequestPayment(
                transaction.getAmount().doubleValue(),
                Long.valueOf(transaction.getCustomer().getId()),
                merchantId,
                PaymentStatus.SETTLED == paymentStatus
        );
        settledPayment.setPaymentStatus(paymentStatus);
        settledPayment.setTransactionId(transaction.getId());
        settledPayment.setTax(transaction.getTaxAmount().doubleValue());

        return settledPayment;

    }

}