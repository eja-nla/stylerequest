package com.hair.business.services.payment.braintree;

import static org.slf4j.LoggerFactory.getLogger;

import com.braintreegateway.BraintreeGateway;
import com.braintreegateway.ClientTokenRequest;
import com.braintreegateway.CustomerRequest;
import com.braintreegateway.PaymentMethodNonce;
import com.braintreegateway.PaymentMethodRequest;
import com.braintreegateway.Result;
import com.braintreegateway.Transaction;
import com.braintreegateway.TransactionRequest;
import com.hair.business.beans.entity.AddOn;
import com.hair.business.beans.entity.Customer;
import com.hair.business.beans.entity.PaymentMethod;
import com.x.business.utilities.Assert;

import org.slf4j.Logger;

import java.math.BigDecimal;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * Braintree implementation
 *
 * For consistency and ease of troubleshooting, we stick to using the payment's datastore Id as the payment method token
 *
 * Created by olukoredeaguda on 06/03/2017.
 */
public class BraintreePaymentHandlerImpl implements BraintreePaymentHandler {

    private final Logger logger = getLogger(this.getClass());

    private final BraintreeGateway gateway;
    private final ClientTokenRequest clientTokenRequest = new ClientTokenRequest();

    @Inject
    public BraintreePaymentHandlerImpl(Provider<BraintreeGateway> gatewayProvider) {
        this.gateway = gatewayProvider.get();
    }

    @Override
    public Transaction authorizeTransaction(Long customerId, String paymentMethodToken, double amount, boolean isSettled) {
        TransactionRequest request = new TransactionRequest()
                .customerId(customerId.toString())
                .amount(BigDecimal.valueOf(amount))
                .paymentMethodToken(paymentMethodToken)
                .options()
                .submitForSettlement(isSettled)
                .done();

        Result result = gateway.transaction().sale(request);

        if (!result.isSuccess()){
            logger.error("Braintree authorization failed with message " + result.getMessage());
        }
        return (Transaction) result.getTarget();
    }

    @Override
    public Transaction settleTransaction(String transactionId, double amount) {
        Result<Transaction> result = gateway.transaction()
                .submitForSettlement(transactionId, BigDecimal.valueOf(amount));

        if (!result.isSuccess()) {
            logger.error("Braintree settle transaction request failed " + result.getMessage());
        }

        return result.getTarget();
    }

    @Override
    public Transaction settleTransaction(String paymentMethodToken, List<AddOn> addOnItems) {
        double total = 0.0;
        for (AddOn addOn: addOnItems) {
            total += addOn.getAmount();
        }

        TransactionRequest request = new TransactionRequest()
                .amount(BigDecimal.valueOf(total))
                .paymentMethodToken(paymentMethodToken)
                .options()
                    .submitForSettlement(true)
                    .done();

        Result<Transaction> result = gateway.transaction().sale(request);

        if (!result.isSuccess()) {
            logger.error("Braintree settle non-preauthorized transaction request failed " + result.getMessage());
        }

        return result.getTarget();
    }

    @Override
    public String generateClientToken(String customerId) {
        clientTokenRequest.customerId(customerId);

        return gateway.clientToken().generate(clientTokenRequest);
    }

    @Override
    public void createCustomer(Customer customer) {
        Assert.notNull(customer, customer.getPayment(), customer.getPayment().getDefaultPaymentMethod(), customer.getPayment().getDefaultPaymentMethod().getToken());

        String nonceFromTheClient = fetchNonce(customer.getPayment().getDefaultPaymentMethod().getToken());

        CustomerRequest request = new CustomerRequest()
                .id(customer.getId().toString())
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .email(customer.getEmail())
                .paymentMethodNonce(nonceFromTheClient);


        Result<com.braintreegateway.Customer> result = gateway.customer().create(request);

        if (!result.isSuccess()){
            logger.error("Braintree create customer request failed: " + result.getMessage());
        }

    }

    @Override
    public void refund(String transactionId) {
        Result<Transaction> result = gateway.transaction().refund(transactionId);

        if (!result.isSuccess()){
            logger.error("Braintree refund request failed: " + result.getMessage());
        }
    }

    @Override
    public Customer addPaymentMethod(Long customerId, PaymentMethod paymentMethod, boolean isDefault) {
        String paymentNonce = fetchNonce(paymentMethod.getToken());
        PaymentMethodRequest request = new PaymentMethodRequest()
                .customerId(customerId.toString())
                .paymentMethodNonce(paymentNonce)
                .options()
                    .verifyCard(true)
                    .failOnDuplicatePaymentMethod(true)
                    .makeDefault(isDefault)
                .done();
        Result<? extends com.braintreegateway.PaymentMethod> result = gateway.paymentMethod().create(request);

        if (!result.isSuccess()){
            logger.error("Braintree add new payment method request failed " + result.getMessage());
        }

        return (Customer) result.getTarget(); //fixme

    }

    @Override
    public String fetchNonce(String paymentMethodToken) {
        Result<PaymentMethodNonce> result = gateway.paymentMethodNonce().create(paymentMethodToken);
        return result.getTarget().getNonce();
    }
}
