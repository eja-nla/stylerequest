package com.hair.business.services.payment.stripe;

import com.hair.business.beans.entity.PaymentTrace;

/**
 * Braintree payment service
 *
 * Created by olukoredeaguda on 06/03/2017.
 */
public interface StripePaymentService {

    String createPaymentIntent(int amount, String customeStripeId, PaymentTrace paymentTrace);

    String createCustomer(String internalCustomerId);

    String createMerchantAccount(String internalMerchantId, String stripeId);

    String authorize(String stripeCustomerId, int amount, String merchantStripeId, String chargeDescription, PaymentTrace paymentTrace);

    void refund(int amount, String paymentIntentId, PaymentTrace paymentTrace);

    void cancelPayment(String paymentIntentId, PaymentTrace paymentTrace);

    void capture(int amount, String preauthToken, PaymentTrace paymentTrace);

    void chargeNow(String stripeCustomerId, int amount, String merchantStripeId, PaymentTrace paymentTrace);
}
