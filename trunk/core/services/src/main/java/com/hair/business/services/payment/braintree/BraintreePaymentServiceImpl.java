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
import com.hair.business.beans.entity.PaymentInformation;
import com.hair.business.beans.entity.PaymentItem;
import com.hair.business.beans.entity.PaymentMethod;
import com.hair.business.beans.entity.StyleRequest;
import com.hair.business.beans.entity.StyleRequestPayment;
import com.hair.business.beans.helper.PaymentStatus;
import com.hair.business.dao.datastore.abstractRepository.Repository;
import com.x.business.utilities.Assert;

import org.slf4j.Logger;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * Braintree payment processor
 *
 * Created by olukoredeaguda on 06/03/2017.
 */
public class BraintreePaymentServiceImpl implements BraintreePaymentService {

    private static final Logger logger = getLogger(BraintreePaymentServiceImpl.class);
    private final BraintreeGateway gateway;
    private final Repository repository;

    @Inject
    public BraintreePaymentServiceImpl(Provider<BraintreeGateway> gatewayProvider, Repository repository) {
        this.gateway = gatewayProvider.get();
        this.repository = repository;
    }

    @Override
    public StyleRequest holdPayment(String nonce, final StyleRequest styleRequest, final Customer customer) {
        Assert.notNull(styleRequest, styleRequest.getStyle(), customer.getId(), customer.getPayment());
        final double price = styleRequest.getStyle().getPrice();
        final Transaction result = createTransaction(nonce, customer.getId(), price, false);

        final StyleRequestPayment authorizedPayment = new StyleRequestPayment(price, customer.getId(), styleRequest.getMerchant().getId(), false, result);
        styleRequest.setAuthorizedPayment(authorizedPayment);
        logger.debug("Successfully authorized payment for stylerequest {}", styleRequest.getId());

        return styleRequest;
    }

    @Override
    public StyleRequest deductPreAuthPayment(String nonce, Long styleRequestId, double totalAmount) {
        Assert.validId(styleRequestId);
        StyleRequest styleRequest = repository.findOne(styleRequestId, StyleRequest.class);
        Assert.notNull(styleRequest);
        Assert.notNull(styleRequest.getSettledPayment());
        String authorizedId = styleRequest.getSettledPayment().getAuthorization().getAuthorizedTransactionId();

        Transaction transaction = settleTransaction(nonce, authorizedId, styleRequest.getAddOns());

        StyleRequestPayment settledPayment = new StyleRequestPayment();
        settledPayment.setSettled(true);
        settledPayment.setPaymentStatus(PaymentStatus.SETTLED);
        settledPayment.setPayment(transaction);

        styleRequest.setSettledPayment(settledPayment);

        repository.saveOne(styleRequest);

        return styleRequest;
    }

    @Override
    public void deductNonPreAuthPayment(String nonce, String paymentToken, List<AddOn> items) {

        Transaction transaction = settleTransaction(nonce, paymentToken, items);

        StyleRequestPayment settledPayment = new StyleRequestPayment();
        settledPayment.setSettled(true);
        settledPayment.setPaymentStatus(PaymentStatus.SETTLED);
        settledPayment.setPayment(transaction);
        //fixme store this somehow?
//        styleRequest.setSettledPayment(settledPayment);
//
//        repository.saveOne(styleRequest);
//
//        return styleRequest;
    }

    @Override
    public double computeTax(String countryCode, double itemPrice) {
        return 0;
    }

    @Override
    public void updatePayment(Long customerId, PaymentMethod paymentMethod, PaymentType paymentType, boolean isDefault) {

    }

    @Override
    public void updatePayment(Long customerId, PaymentMethod paymentMethod, PaymentType paymentType, String nonce, boolean isDefault) {
        Customer customer = repository.findOne(customerId, Customer.class);
        customer.getPayment().getPaymentItems().add(new PaymentItem(paymentType, paymentMethod, isDefault));
        addPaymentMethod(nonce, customerId, paymentMethod, isDefault);

        repository.saveOne(customer);
    }

    @Override
    public Customer createCustomerPaymentProfile(final Customer customer, PaymentType paymentType, String nonce, boolean isDefault) {

        PaymentMethod paymentMethod = new PaymentMethod(UUID.randomUUID().toString(), isDefault, customer.getPermanentId().toString());
        PaymentItem paymentItem = new PaymentItem(paymentType, paymentMethod, isDefault);

        PaymentInformation paymentInformation = new PaymentInformation();
        paymentInformation.setDefaultPaymentMethod(paymentItem);

        customer.setPayment(paymentInformation);

        createCustomer(customer, nonce);

        return customer;
    }

    @Override
    public StyleRequest holdPayment(StyleRequest styleRequest, Customer customer) {
        return null;
    }

    @Override
    public StyleRequest deductPreAuthPayment(Long styleRequestId, double totalAmount) {
        return null;
    }

    @Override
    public void deductNonPreAuthPayment(String paymentToken, List<AddOn> addOns) {

    }

    @Override
    public Customer createCustomerPaymentProfile(Customer customer, PaymentType paymentType, boolean isDefault) {
        return null;
    }

    @Override
    public void refund(StyleRequest styleRequest) {

        Assert.notNull(styleRequest);
        Assert.notNull(styleRequest.getSettledPayment());

        Transaction settledTransaction = styleRequest.getSettledPayment().getPayment();
        Assert.notNull(settledTransaction);

        String transactionId = settledTransaction.getId();

        refund(transactionId);
    }


    @Override
    public Transaction settleTransaction(String nonce, String paymentMethodToken, List<AddOn> addOnItems) {
        double total = 0.0;
        for (AddOn addOn: addOnItems) {
            total += addOn.getAmount();
        }

        TransactionRequest request = new TransactionRequest()
                .amount(BigDecimal.valueOf(total))
                .paymentMethodNonce(nonce)
                .options()
                .submitForSettlement(true)
                .done();

        Result<Transaction> result = gateway.transaction().sale(request);

        if (!result.isSuccess()) {
            logger.error("Braintree settle non-preauthorized transaction request failed {}", result.getMessage());
        }

        return result.getTarget();
    }

    @Override
    public String issueClientToken(String customerId) {
        final ClientTokenRequest clientTokenRequest = new ClientTokenRequest().customerId(customerId);

        return gateway.clientToken().generate(clientTokenRequest);
    }

    @Override
    public String createCustomer(Customer customer, String nonce) {
        PaymentInformation pInfo = customer.getPayment();
        Assert.notNull(customer, pInfo, pInfo.getDefaultPaymentMethod(), pInfo.getDefaultPaymentMethod().getToken());

        CustomerRequest request = new CustomerRequest()
                .id(customer.getId().toString())
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .email(customer.getEmail())
                .paymentMethodNonce(nonce);


        Result<com.braintreegateway.Customer> result = gateway.customer().create(request);

        if (!result.isSuccess()){
            logger.error("Braintree create customer request failed: {}", result.getMessage());
        }

        return result.getTarget().getId();

    }

    @Override
    public void refund(String transactionId) {
        Result<Transaction> result = gateway.transaction().refund(transactionId);

        if (!result.isSuccess()){
            logger.error("Braintree refund request failed: {}", result.getMessage());
        }
    }


    @Override
    public Transaction createTransaction(String nonce, Long customerId, double amount, boolean isSettled) {
        final TransactionRequest request = new TransactionRequest()
                .customerId(customerId.toString())
                .amount(BigDecimal.valueOf(amount))
                .paymentMethodNonce(nonce)
                .options()
                    .submitForSettlement(isSettled)
                    .done();

        final Result result = gateway.transaction().sale(request);

        if (!result.isSuccess()){
            logger.error("Braintree authorization failed with message {}", result.getMessage());
        }
        return (Transaction) result.getTarget();


    }

    @Override
    public Transaction settleTransaction(String transactionId, double amount) {
        Result<Transaction> result = gateway.transaction().submitForSettlement(transactionId, BigDecimal.valueOf(amount));

        if (!result.isSuccess()) {
            logger.error("Braintree settle transaction request failed {}", result.getMessage());
        }

        return result.getTarget();
    }

    @Override
    public boolean addPaymentMethod(String nonce, Long customerId, PaymentMethod paymentMethod, boolean isDefault) {
        PaymentMethodRequest request = new PaymentMethodRequest()
                .customerId(customerId.toString())
                .paymentMethodNonce(nonce)
                .options()
                .verifyCard(true)
                .failOnDuplicatePaymentMethod(true)
                .makeDefault(isDefault)
                .done();
        Result<? extends com.braintreegateway.PaymentMethod> result = gateway.paymentMethod().create(request);

        if (!result.isSuccess()){
            logger.error("Braintree add new payment method request failed {}", result.getMessage());
        }

        return result.isSuccess();
    }

}
