package com.hair.business.beans.entity;


import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Serialize;
import com.hair.business.beans.abstracts.AbstractActorEnablerEntity;
import com.hair.business.beans.constants.PaymentType;
import com.hair.business.beans.helper.PaymentStatus;
import com.paypal.api.payments.Authorization;
import com.paypal.api.payments.Capture;
import com.paypal.api.payments.Payment;

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
    private @Serialize Payment payment;
    private @Serialize Authorization authorization;
    private @Serialize Capture capture;

    public StyleRequestPayment(){}

    public StyleRequestPayment(Double amount, Long customerId, Long merchantId, boolean settled, PaymentType type) {
        this();
        this.amount = amount;
        this.customerId = customerId;
        this.merchantId = merchantId;
        this.settled = settled;
        this.type = type;
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

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public Authorization getAuthorization() {
        return authorization;
    }

    public void setAuthorization(Authorization authorization) {
        this.authorization = authorization;
    }

    public Capture getCapture() {
        return capture;
    }

    public void setCapture(Capture capture) {
        this.capture = capture;
    }
//    public void setPaymentStatus(PaymentStatus paymentStatus) {
//        this.paymentStatus = paymentStatus;
//    }
//
//    public PaymentExt getPayment() {
//        return payment;
//    }
//
//    public void setPayment(PaymentExt payment) {
//        this.payment = payment;
//    }
//
//    public AuthorizationExt getAuthorization() {
//        return authorization;
//    }
//
//    public void setAuthorization(AuthorizationExt authorization) {
//        this.authorization = authorization;
//    }
//
//    public CaptureExt getCapture() {
//        return capture;
//    }
//
//    public void setCapture(CaptureExt capture) {
//        this.capture = capture;
//    }

    //    public com.paypal.api.payments.Payment getPayment() {
//        return payment;
//    }
//
//    public void setPayment(com.paypal.api.payments.Payment payment) {
//        this.payment = payment;
//    }
//
//    public Authorization getAuthorization() {
//        return authorization;
//    }
//
//    public void setAuthorization(Authorization authorization) {
//        this.authorization = authorization;
//    }
//
//    public Capture getCapture() {
//        return capture;
//    }
//
//    public void setCapture(Capture capture) {
//        this.capture = capture;
//    }
}
