package com.hair.business.beans.entity;

import static org.joda.time.DateTime.now;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.hair.business.beans.abstracts.AbstractActorEntity;

/**
 * Created by Olukorede Aguda on 25/04/2016.
 *
 * Represents a placed style request from client to a merchant
 */
@Entity
public class Customer extends AbstractActorEntity {

    @Id
    private Long id;

    private Payment payment;

    private Location location;


    public Customer(String name, String rating, String email, String phone){
        this.setName(name);
        this.setRating(rating);
        this.setEmail(email);
        this.setPhone(phone);
        this.setActive(true);
        this.setCreated(now());
        this.setLastUpdated(now());

    }

    public Customer(String name, String rating, String email, String phone, Location location, Payment payment) {
        this(name, rating, email, phone);
        this.payment = payment;
        this.location = location;

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
