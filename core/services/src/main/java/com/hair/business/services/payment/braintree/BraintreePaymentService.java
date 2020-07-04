package com.hair.business.services.payment.braintree;

import com.braintreegateway.Result;
import com.braintreegateway.Transaction;
import com.hair.business.beans.constants.PaymentType;
import com.hair.business.beans.entity.PaymentMethod;
import com.hair.business.beans.entity.StyleRequest;

import java.math.BigDecimal;

/**
 * Braintree payment service
 *
 * Created by olukoredeaguda on 06/03/2017.
 */
public interface BraintreePaymentService {

    /**
     * Creates a transaction, which:
     * if isSettled == true, returns a final payment
     * else, returns an authorization that will need to be completed later.
     *
     *
     * */
    Transaction createTransaction(String nonce, String orderId, String customerId, double totalAmount, double taxAmount, boolean isSettled);

    /**
     * Authorizes and settles a one time non-preauthorized transaction
     *
     * Handy for one off payments
     * */
    Transaction settleTransaction(String nonce, double amount);

    /**
     * Add new payment method to an existing customer
     * */
    boolean addPaymentMethod(String nonce, PaymentMethod payment);

    /**
     * Authorize a stylerequest payment
     * */
    StyleRequest authorize(String nonce, StyleRequest styleRequest);

    /**
     * Retroactively authorize stylerequest payment
     */
    StyleRequest authorize(String nonce, final Long styleRequestId);

    /**
     *
     * */
    StyleRequest settlePreAuthPayment(StyleRequest styleRequest);

    /**
     * Convenient refund call for whole or part of a known transaction instead from as part of a style request
     *
     * If the amount is zero, we treat as full refund
     * */
    Result refund(String transactionId, BigDecimal amount);

    void updatePayment(Long customerId, PaymentMethod paymentMethod, PaymentType paymentType, String nonce, boolean isDefault);

    /**
     *
     * A client token may or may not have a customer ID e.g. ID will be empty during signup.
     * Not that 'entity' here means
     * the payment entity which could either be a customer or a merchant that is the
     * subject/owner of the transaction.
     *
     * Including a customerId when generating the client token lets returning customers
     * select from previously used payment method options, improving user experience
     *
     * */
    String issueClientToken(String entityID);

    /**
     * Stores a new customer's info in Braintree's vault.
     *
     * A Braintree client-side integration sends payment information
     * – like a credit card or a PayPal authorization – to Braintree
     * in exchange for a payment method nonce, a one time use value
     * that represents that payment method. By the time we have the nonce, we've already got their payment method in the vault.
     *
     * returns their ID
     **/

    String createProfile(String paymentId, String nonce);
}
