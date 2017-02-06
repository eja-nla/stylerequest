package com.hair.business.beans.entity;

import static org.joda.time.DateTime.now;

import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Stringify;
import com.hair.business.beans.abstracts.AbstractActorEntity;
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

    private Location location;

    @Stringify(IntegerStringifyer.class) // Map of allowed scores and their weights(i.e. count)
    private Map<Integer, Integer> ratings;
    private double score;

    public Merchant(){}

    public Merchant(String name, String email, String phone, Device device){
        this();
        this.setName(name);
        this.ratings = new HashMap<>();
        this.setEmail(email);
        this.setPhone(phone);
        this.setDevice(device);
        this.setActive(true);
        this.setLastUpdated(now());

    }

    public Merchant(String name, String email, String phone, Device device, Location location) {
        this(name, email, phone, device);
        this.location = location;

    }

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
