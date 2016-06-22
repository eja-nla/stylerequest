package com.hair.business.beans.abstracts;

import com.hair.business.beans.entity.Device;

/**
 * Abstract actor entity e.g. Customer, Merchant etc.
 *
 * Created by Olukorede Aguda on 25/04/2016.
 */
public abstract class AbstractActorEntity extends AbstractTransactionEntity{
    private String rating;
    private String email;
    private String phone;
    private Device device;

    private boolean active;


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
