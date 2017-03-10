package com.hair.business.services.payment.braintree;

import com.braintreegateway.Transaction;
import com.hair.business.beans.entity.Customer;
import com.hair.business.beans.entity.PaymentMethod;

/**
 * Braintree payment handler
 *
 * Created by olukoredeaguda on 06/03/2017.
 */
public interface BraintreePaymentHandler {

    /**
     * Creates a transaction, which:
     * if isSettled == true, returns a final payment
     * else, returns an authorization that will need to be completed later.
     *
     *
     * */
    Transaction authorizeTransaction(Long customerId, String paymentMethodToken, double amount, boolean isSettled);

    Transaction settleTransaction(String transactionId);

    com.braintreegateway.Customer addPaymentMethod(Long customerId, PaymentMethod payment, boolean isDefault);

    String fetchNonce(String paymentMethodToken);

    String generateClientToken(String customerId);

    com.braintreegateway.Customer createCustomer(Customer customer);

}
