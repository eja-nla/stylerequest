package com.hair.business.beans.entity;

import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Stringify;
import com.hair.business.beans.abstracts.AbstractActorEntity;
import com.hair.business.beans.constants.Preferences;
import com.hair.business.beans.helper.IntegerStringifyer;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Olukorede Aguda on 25/04/2016.
 *
 * Represents a placed style request from client to a merchant
 */
@Entity
@Cache
public class Customer extends AbstractActorEntity {

    @Id
    private Long id;

    @Stringify(IntegerStringifyer.class) // Map of allowed scores and their weights(i.e. count)
    private Map<Integer, Integer> ratings;
    private double score;

    public Customer() {
        this.ratings = new HashMap<>();
        this.setPreferences(new Preferences());
        this.setActive(true);
    }

    protected Customer(String firstname, String lastname, String email, String phone, Device device){
        this();
        this.setFirstName(firstname);
        this.setLastName(lastname);
        this.setEmail(email);
        this.setPhone(phone);
        this.setDevice(device);
    }

    public Customer(String firstname, String lastname, String email, String phone, Device device, Address address) {
        this(firstname, lastname, email, phone, device);
        this.setAddress(address);

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Map<Integer, Integer> getRatings() {
        return ratings;
    }

    public void setRatings(Map<Integer, Integer> ratings) {
        this.ratings = ratings;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }
}
