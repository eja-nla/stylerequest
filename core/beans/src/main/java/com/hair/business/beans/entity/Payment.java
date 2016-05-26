package com.hair.business.beans.entity;


import com.hair.business.beans.abstracts.AbstractActorEnablerEntity;
import com.hair.business.beans.constants.MerchantType;

/**
 * Represents a payment between two entities.
 *
 *  Created by Olukorede Aguda on 25/04/2016.
 *
 */
public class Payment extends AbstractActorEnablerEntity {

    private long amount;
    private long from;
    private long to;
    private boolean settled;
    private MerchantType type;

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public long getFrom() {
        return from;
    }

    public void setFrom(long from) {
        this.from = from;
    }

    public long getTo() {
        return to;
    }

    public void setTo(long to) {
        this.to = to;
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
}
