package com.hair.business.beans.entity;

import com.googlecode.objectify.annotation.Id;
import com.hair.business.beans.abstracts.AbstractActorEnablerEntity;

import java.util.List;
import java.util.Map;

/**
 * Created by olukoredeaguda on 09/02/2017.
 *
 * Payment information object. A customer/merchant has one which contains
 * credit card or paypal or whatever their means of payment issue/collection is
 */
public class PaymentTrace extends AbstractActorEnablerEntity {

    @Id
    private Long id;

    private Long ownerId;

    private List<PaymentItem> paymentItems;

    private PaymentItem defaultPaymentMethod;

    private Map<String, String> paymentTxnIds; // the ID,comment pair of any payment API call is added here for tracking

    //state - Stripe needs this to prevent CSRF attacks.
    // It should be a unique, not guessable value that’s generated and saved on your server. Stripe passes it back to your redirect after the user finishes the onboarding flow.
    private String state;

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

    public List<PaymentItem> getPaymentItems() {
        return paymentItems;
    }

    public String getState() {
        return state;
    }

    public void setPaymentItems(List<PaymentItem> paymentItems) {
        this.paymentItems = paymentItems;
    }

    public PaymentMethod getDefaultPaymentMethod() {
        if (this.defaultPaymentMethod == null){
            return null;
        }

        return defaultPaymentMethod.getPaymentMethod();
    }

    public void setDefaultPaymentMethod(PaymentItem defaultPaymentMethod) {
        for (PaymentItem item : this.paymentItems) {
            item.setDefault(false);
        }
        defaultPaymentMethod.setDefault(true);
        this.defaultPaymentMethod = defaultPaymentMethod;
    }

    public Map<String, String> getPaymentTxnIds() {
        return paymentTxnIds;
    }

    public void setPaymentTxnIds(Map<String, String> paymentTxnIds) {
        this.paymentTxnIds = paymentTxnIds;
    }

    public void setState(String state) {
        this.state = state;
    }
}
