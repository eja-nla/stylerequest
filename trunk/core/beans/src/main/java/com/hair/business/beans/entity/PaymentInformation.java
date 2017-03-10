package com.hair.business.beans.entity;

import com.googlecode.objectify.annotation.Id;
import com.hair.business.beans.abstracts.AbstractActorEnablerEntity;

import java.util.List;

/**
 * Created by olukoredeaguda on 09/02/2017.
 *
 * Payment information object. A customer/merchant has one which contains
 * credit card or paypal or whatever their means of payment issue/collection is
 */
public class PaymentInformation extends AbstractActorEnablerEntity {

    @Id
    private Long id;

    private Long ownerId;

    private List<PaymentItem> paymentMethods;

    private PaymentItem defaultPaymentMethod;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public List<PaymentItem> getPaymentMethods() {
        return paymentMethods;
    }

    public void setPaymentMethods(List<PaymentItem> paymentMethods) {
        this.paymentMethods = paymentMethods;
    }

    public PaymentItem getDefaultPaymentMethod() {
        return defaultPaymentMethod;
    }

    public void setDefaultPaymentMethod(PaymentItem defaultPaymentMethod) {
        for (PaymentItem item : this.paymentMethods) {
            item.setDefault(false);
        }
        defaultPaymentMethod.setDefault(true);
        this.defaultPaymentMethod = defaultPaymentMethod;
    }
}
