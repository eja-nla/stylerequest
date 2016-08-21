package com.hair.business.beans.entity;


import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.hair.business.beans.abstracts.AbstractActorEnablerEntity;

/**
 * Created by Olukorede Aguda on 25/04/2016.
 *
 * Represents a physical address of a client or a merchant
 */
@Entity
@Cache
public class Address extends AbstractActorEnablerEntity {

    @Id
    private Long id;

    private String addressLine;
    private String postCode;
    private Location location;

    public Address() {
    }

    public Address(String addressLine, String postCode, Location location) {
        this();
        this.addressLine = addressLine;
        this.postCode = postCode;
        this.location = location;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddressLine() {
        return addressLine;
    }

    public void setAddressLine(String addressLine) {
        this.addressLine = addressLine;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

}

