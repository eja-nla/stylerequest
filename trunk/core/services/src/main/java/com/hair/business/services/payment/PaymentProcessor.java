package com.hair.business.services.payment;

import com.hair.business.beans.entity.Payment;

/**
 * Created by olukoredeaguda on 09/02/2017.
 *
 * Payment processor
 */
public interface PaymentProcessor {

    Payment pay(Long senderId, Long recipientId, double amount);

    Payment refund(Long senderId, Long recipientId, double amount);
}
