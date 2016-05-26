package com.hair.business.beans.entity;

import com.hair.business.beans.abstracts.AbstractActorEntity;

/**
 * Created by Olukorede Aguda on 25/04/2016.
 *
 * Represents a placed style request from client to a merchant
 */
public class Customer extends AbstractActorEntity {

    private Payment payment;

    private Location location;

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
