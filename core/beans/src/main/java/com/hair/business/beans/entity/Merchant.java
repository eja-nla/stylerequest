package com.hair.business.beans.entity;

import static org.joda.time.DateTime.now;

import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.hair.business.beans.abstracts.AbstractActorEntity;

/**
 * Created by Olukorede Aguda on 25/04/2016.
 *
 * Represents a merchant entity
 */
@Entity
@Cache
public class Merchant extends AbstractActorEntity {

    @Id
    private Long id;

    private Location location;

    public Merchant(){}

    public Merchant(String name, int rating, String email, String phone, Device device){
        this();
        this.setName(name);
        this.setRating(rating);
        this.setEmail(email);
        this.setPhone(phone);
        this.setDevice(device);
        this.setActive(true);
        this.setLastUpdated(now());

    }

    public Merchant(String name, int rating, String email, String phone, Device device, Location location) {
        this(name, rating, email, phone, device);
//        this.payment = payment;
        this.location = location;

    }

//    public Payment getPayment() {
//        return payment;
//    }
//
//    public void setPayment(Payment payment) {
//        this.payment = payment;
//    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
