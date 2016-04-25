package com.hair.business.dao.entity;


import static com.hair.business.dao.constants.EntityConstants.PAYMENT_INDEX;
import static com.hair.business.dao.constants.EntityConstants.PAYMENT_TYPE;

import com.hair.business.dao.constants.MerchantType;

import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Mapping;

/**
 * Created by Olukorede Aguda on 25/04/2016.
 *
 * Represents a payment between two entities
 */
@Document(indexName = PAYMENT_INDEX, type = PAYMENT_TYPE)
@Mapping(mappingPath = "/es/payment_mapping.json")
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
