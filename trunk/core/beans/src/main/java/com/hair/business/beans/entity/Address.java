package com.hair.business.beans.entity;


import com.hair.business.beans.abstracts.AbstractActorEnablerEntity;

/**
 * Created by Olukorede Aguda on 25/04/2016.
 *
 * Represents a physical address of a client or a merchant
 */
public class Address extends AbstractActorEnablerEntity {

    private String addressLine;
    private String postCode;
    private Location location;

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

