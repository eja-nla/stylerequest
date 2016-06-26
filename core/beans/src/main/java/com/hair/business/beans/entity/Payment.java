package com.hair.business.beans.entity;


import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Load;
import com.hair.business.beans.abstracts.AbstractActorEnablerEntity;
import com.hair.business.beans.constants.MerchantType;

import org.joda.time.DateTime;

/**
 * Represents a payment between two entities.
 *
 *  Created by Olukorede Aguda on 25/04/2016.
 *
 */
@Entity
public class Payment extends AbstractActorEnablerEntity {

    @Id
    private Long id;

    private long amount;
    private @Load Ref<Customer> customer;
    private @Load Ref<Merchant> merchant;
    private boolean settled;
    private MerchantType type;
    private long date;

    public Payment(){}

    public Payment(long amount, Customer customer, Merchant merchant, boolean settled, MerchantType type, DateTime date) {
        this();
        this.amount = amount;
        this.customer = Ref.create(customer);
        this.merchant = Ref.create(merchant);
        this.settled = settled;
        this.type = type;
        this.date = date.getMillis();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public Customer getCustomer() {
        return customer.get();
    }

    public void setCustomer(Ref<Customer> customer) {
        this.customer = customer;
    }

    public Merchant getMerchant() {
        return merchant.get();
    }

    public void setMerchant(Ref<Merchant> merchant) {
        this.merchant = merchant;
    }

    public boolean isSettled() {
        return settled;
    }

    public void setSettled(boolean settled) {
        this.settled = settled;
    }

    public MerchantType getType() {
        return type;
    }

    public void setType(MerchantType type) {
        this.type = type;
    }

    public long getDate() {
        return date;
    }

    public void setDate(DateTime date) {
        this.date = date.getMillis();
    }
}
