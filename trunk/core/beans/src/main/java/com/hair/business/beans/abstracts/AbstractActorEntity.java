package com.hair.business.beans.abstracts;

import com.hair.business.beans.constants.Gender;
import com.hair.business.beans.entity.Device;

/**
 * Abstract actor entity e.g. Customer, Merchant etc.
 *
 * Created by Olukorede Aguda on 25/04/2016.
 */
public abstract class AbstractActorEntity extends AbstractTransactionEntity{

    private String email;
    private String phone;
    private Device device;
    private String photoUrl;
    private Gender gender;
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

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }
}
