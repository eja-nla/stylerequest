package com.hair.business.services.payment.paypal;

import com.hair.business.beans.entity.Customer;
import com.hair.business.beans.entity.StyleRequest;
import com.hair.business.beans.entity.StyleRequestPayment;
import com.hair.business.services.payment.PaymentService;
import com.x.business.exception.PaypalPaymentException;

/**
 * Created by olukoredeaguda on 09/02/2017.
 *
 * Paypal payment processor
 */
public interface PaypalPaymentService extends PaymentService {
//public interface PaypalPaymentService {
    /**
     * Fires an authorization request to Paypal
     * */
    StyleRequestPayment authorizePayment(StyleRequest styleRequest, Customer customer) throws PaypalPaymentException;

    /**
     * Captures a pre-authorized Paypal payment
     * */
    StyleRequestPayment capturePreauthorizedPayment(String authorizationId, double totalAmount, boolean isFinalCapture) throws PaypalPaymentException;
}
