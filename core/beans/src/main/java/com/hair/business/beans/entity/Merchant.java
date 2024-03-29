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
 * Represents a merchant entity
 */
@Entity
@Cache
public class Merchant extends AbstractActorEntity {

    @Id
    private Long id;

    private String businessName;

    private String description;

    @Stringify(IntegerStringifyer.class) // Map of allowed scores and their weights(i.e. count)
    private Map<Integer, Integer> ratings;
    private double score;

    public Merchant(){}

    public Merchant(String firstname, String lastname, String businessName, String email, String phone, Device device){
        this();
        this.setFirstName(firstname);
        this.setLastName(lastname);
        this.businessName = businessName;
        this.ratings = new HashMap<>();
        this.setPreferences(new Preferences());
        this.setEmail(email);
        this.setPhone(phone);
        this.setDevice(device);
        this.setActive(true);
    }

    public Merchant(String firstName, String lastName, String businessName, String email, String phone, Device device, Address address) {
        this(firstName, lastName, businessName, email, phone, device);
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

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
