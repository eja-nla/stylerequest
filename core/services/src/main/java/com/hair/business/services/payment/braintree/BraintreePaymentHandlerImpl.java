package com.hair.business.services.payment.braintree;

import com.braintreegateway.BraintreeGateway;
import com.braintreegateway.ClientTokenRequest;
import com.braintreegateway.Customer;
import com.braintreegateway.CustomerRequest;
import com.braintreegateway.PaymentMethodNonce;
import com.braintreegateway.PaymentMethodRequest;
import com.braintreegateway.Result;
import com.braintreegateway.Transaction;
import com.braintreegateway.TransactionRequest;
import com.hair.business.beans.entity.PaymentMethod;

import java.math.BigDecimal;
import java.util.logging.Logger;

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
    private static final Logger logger = Logger.getLogger(BraintreePaymentServiceImpl.class.getName());

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
            logger.severe("Braintree authorization failed with message " + result.getErrors());
        }
        return (Transaction) result.getTarget();
    }

    @Override
    public Transaction settleTransaction(String transactionId) {
        Result<Transaction> result = gateway.transaction().submitForSettlement(transactionId);

        if (!result.isSuccess()) {
            logger.severe("Braintree settle transaction request failed " + result.getErrors().toString());
        }

        return result.getTarget();
    }

    @Override
    public String generateClientToken(String customerId) {
        clientTokenRequest.customerId(customerId);

        return gateway.clientToken().generate(clientTokenRequest);
    }

    @Override
    public Customer createCustomer(com.hair.business.beans.entity.Customer customer) {
        CustomerRequest request = new CustomerRequest()
                .customerId(customer.getId().toString())
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .email(customer.getEmail());
        Result<Customer> result = gateway.customer().create(request);

        if (!result.isSuccess()){
            logger.severe("Braintree create customer request failed " + result.getErrors().toString());
        }

        return result.getTarget();
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
            logger.severe("Braintree add new payment method request failed " + result.getErrors());
        }

        return (Customer) result.getTarget();
    }

    @Override
    public String fetchNonce(String paymentMethodToken) {
        Result<PaymentMethodNonce> result = gateway.paymentMethodNonce().create(paymentMethodToken);
        return result.getTarget().getNonce();
    }
}
