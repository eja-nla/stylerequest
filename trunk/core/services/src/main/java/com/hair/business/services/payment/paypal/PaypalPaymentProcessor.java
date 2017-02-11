package com.hair.business.services.payment.paypal;

import com.hair.business.services.payment.PaymentProcessor;
import com.paypal.api.payments.Payment;

/**
 * Created by olukoredeaguda on 09/02/2017.
 *
 * Paypal payment processor
 */
public interface PaypalPaymentProcessor extends PaymentProcessor {

    Payment pay(Long payerId, Long recipientId, String guid);

    void refund(String saleId, String currency, Double totalAmount);
}
