package com.hair.business.services.payment.braintree;

import com.braintreegateway.Transaction;
import com.hair.business.beans.constants.PaymentType;
import com.hair.business.beans.entity.AddOn;
import com.hair.business.beans.entity.PaymentMethod;
import com.hair.business.beans.entity.StyleRequest;

import java.util.List;

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
    Transaction createTransaction(String nonce, Long customerId, double amount, boolean isSettled);

    /**
     * Settles a pre-authorized transaction
     * */
    Transaction settleTransaction(String transactionId, double amount);

    /**
     * Authorizes and settles a one time non-preauthorized transaction
     *
     *
     * */
    Transaction settleTransaction(String nonce, String paymentMethodToken, List<AddOn> addOns);

    boolean addPaymentMethod(String nonce, String customerId, PaymentMethod payment);

    StyleRequest authorize(String nonce, Long styleRequestId, Long customerId);

    StyleRequest deductPreAuthPayment(String nonce, Long styleRequestId, double totalAmount);

    void deductNonPreAuthPayment(String nonce, String paymentToken, List<AddOn> items);

    void updatePayment(Long customerId, PaymentMethod paymentMethod, PaymentType paymentType, String nonce, boolean isDefault);

    /**
     *
     * Including a customerId when generating the client token lets returning customers
     * select from previously used payment method options, improving user experience
     *
     * */
    String issueClientToken(String customerId);

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

    String createProfile(String id, String firstName, String lastName, String email, String nonce);


    void refund(String transactionId);

}
