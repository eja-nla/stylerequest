package com.hair.business.beans.entity;


import com.braintreegateway.Transaction;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Serialize;
import com.hair.business.beans.abstracts.AbstractActorEnablerEntity;
import com.hair.business.beans.constants.PaymentType;
import com.hair.business.beans.helper.PaymentStatus;

/**
 * Represents a payment between two entities.
 *
 *  Created by Olukorede Aguda on 25/04/2016.
 *
 */
@Entity
public class StyleRequestPayment extends AbstractActorEnablerEntity {

    @Id
    private Long id;

    private Double amount;
    private Long customerId;
    private Long merchantId;
    private boolean settled;
    private PaymentType type;
    private @Index PaymentStatus paymentStatus;
    private @Serialize Transaction payment;
    private @Serialize Transaction authorization;

    public StyleRequestPayment(){}

    public StyleRequestPayment(Double amount, Long customerId, Long merchantId, boolean settled, PaymentType type) {
        this();
        this.amount = amount;
        this.customerId = customerId;
        this.merchantId = merchantId;
        this.settled = settled;
        this.type = type;
    }

    public StyleRequestPayment(double amount, Long customerId, Long merchantId, boolean isSettled, Transaction transaction) {
        super();

        this.amount = amount;
        this.customerId = customerId;
        this.merchantId = merchantId;
        this.settled = isSettled;
        this.payment = transaction;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public boolean isSettled() {
        return settled;
    }

    public void setSettled(boolean settled) {
        this.settled = settled;
    }

    public PaymentType getType() {
        return type;
    }

    public void setType(PaymentType type) {
        this.type = type;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public Transaction getPayment() {
        return payment;
    }

    public void setPayment(Transaction payment) {
        this.payment = payment;
    }

    public Transaction getAuthorization() {
        return authorization;
    }

    public void setAuthorization(Transaction authorization) {
        this.authorization = authorization;
    }
}
