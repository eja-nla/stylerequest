package com.hair.business.services.payment.paypal;

import com.hair.business.beans.entity.Customer;
import com.hair.business.beans.entity.StyleRequest;
import com.hair.business.beans.entity.StyleRequestPayment;
import com.hair.business.services.payment.PaymentProcessor;

/**
 * Created by olukoredeaguda on 09/02/2017.
 *
 * Paypal payment processor
 */
public interface PaypalPaymentProcessor extends PaymentProcessor {

    /**
     * Fires an authorization request to Paypal
     * */
    StyleRequestPayment authorizePayment(StyleRequest styleRequest, Customer customer, double tax, double total);

    /**
     * Captures a pre-authorized Paypal payment
     * */
    StyleRequestPayment capturePreauthorizedPayment(String authorizationId, double totalAmount, boolean isFinalCapture);
}
