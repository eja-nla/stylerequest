package com.hair.business.services.payment.braintree;

import com.braintreegateway.Transaction;
import com.hair.business.beans.entity.AddOn;
import com.hair.business.beans.entity.Customer;
import com.hair.business.beans.entity.PaymentMethod;

import java.util.List;

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

    /**
     * Settles a pre-authorized transaction
     * */
    Transaction settleTransaction(String transactionId, double amount);

    /**
     * Authorizes and settles a one time non-preauthorized transaction
     *
     *
     * */
    Transaction settleTransaction(String paymentMethodToken, List<AddOn> addOns);

    Customer addPaymentMethod(Long customerId, PaymentMethod payment, boolean isDefault);

    /**
     * Get a nonce for this payment method.
     * Client will send payment method directly to braintree in exchange for a nonce.
     * */
    String fetchNonce(String paymentMethodToken);

    /**
     *
     * Including a customerId when generating the client token lets returning customers
     * select from previously used payment method options, improving user experience
     *
     * */
    String generateClientToken(String customerId);

    /**
     * Stores a new customer's info in Braintree's vault.
     *
     * */

    void createCustomer(Customer customer);

    void refund(String transactionId);

}
