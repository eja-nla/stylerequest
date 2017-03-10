package com.hair.business.beans.entity;

import com.googlecode.objectify.annotation.Serialize;
import com.hair.business.beans.abstracts.AbstractActorEnablerEntity;
import com.hair.business.beans.constants.PaymentType;

import java.io.Serializable;

/**
 * Created by olukoredeaguda on 09/02/2017.
 *
 *
 */
@Serialize
public class PaymentItem extends AbstractActorEnablerEntity implements Serializable {

    private PaymentType type;

    private PaymentMethod paymentMethod;

    private boolean isDefault;

    public PaymentItem() {}

    public PaymentItem(PaymentType type, PaymentMethod paymentMethod, boolean isDefault) {
        this.type = type;
        this.paymentMethod = paymentMethod;
        this.isDefault = isDefault;
    }

    public PaymentType getType() {
        return type;
    }

    public void setType(PaymentType type) {
        this.type = type;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }
}
