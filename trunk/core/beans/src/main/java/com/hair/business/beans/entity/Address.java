package com.hair.business.beans.entity;


import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Load;
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
    private String zipCode;
    private String district; //e.g. Brooklyn, Santa Clara
    private @Index @Load Location location;

    public Address() {
    }

    public Address(String addressLine, String zipCode, Location location) {
        this();
        this.addressLine = addressLine;
        this.zipCode = zipCode;
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

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }
}

