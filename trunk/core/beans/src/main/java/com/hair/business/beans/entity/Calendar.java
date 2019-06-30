package com.hair.business.beans.entity;

import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.hair.business.beans.abstracts.AbstractActorEntity;

import java.util.List;

/**
 * Created by ejanla on 6/30/19.
 */
@Entity
@Cache
public class Calendar extends AbstractActorEntity {

    @Id
    private Long id;

    private Long owningMerchantId; // could this be > 1???
    private Long owningCustomerId;

    List<Appointment> appointments;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOwningMerchantId() {
        return owningMerchantId;
    }

    public void setOwningMerchantId(Long owningMerchantId) {
        this.owningMerchantId = owningMerchantId;
    }

    public List<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
    }

    public Long getOwningCustomerId() {
        return owningCustomerId;
    }

    public void setOwningCustomerId(Long owningCustomerId) {
        this.owningCustomerId = owningCustomerId;
    }
}
