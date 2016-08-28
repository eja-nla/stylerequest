package com.hair.business.beans.entity;


import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Load;
import com.hair.business.beans.abstracts.AbstractActorEnablerEntity;
import com.hair.business.beans.constants.StyleRequestState;

import org.joda.time.DateTime;

/**
 * Represents a placed style request from client to a merchant
 *
 * Created by Olukorede Aguda on 25/04/2016.
 *
 */
@Entity
@Cache
public class StyleRequest extends AbstractActorEnablerEntity {

    @Id
    private Long id;

    private @Load Ref<Style> style;

    private @Load Ref<Merchant> merchant; // with @Load, Ofy efficiently fetches for large dataset

    private @Load Ref<Customer> customer;

    private @Load Ref<Location> location;

    private @Index StyleRequestState state;

    private DateTime appointmentDateTime;

    public StyleRequest(){
    }

    public StyleRequest(Style style, Merchant merchant, Customer customer, Location location, StyleRequestState state, DateTime appointmentDateTime) {
        this();
        this.style = Ref.create(style);
        this.merchant = Ref.create(merchant);
        this.customer = Ref.create(customer);
        this.location = Ref.create(location);
        this.state = state;
        this.appointmentDateTime = appointmentDateTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Style getStyle() {
        return style.get();
    }

    public void setStyle(Style style) {
        this.style = Ref.create(style);
    }

    public Merchant getMerchant() {
        return merchant.get();
    }

    public void setMerchant(Merchant merchant) {
        this.merchant = Ref.create(merchant);
    }

    public void setCustomer(Customer customer) {
        this.customer = Ref.create(customer);

    }

    public Customer getCustomer() {
        return customer.get();
    }

    public Location getLocation() {
        return location.get();
    }

    public void setLocation(Location location) {
        this.location = Ref.create(location);
    }

    public StyleRequestState getState() {
        return state;
    }

    public void setState(StyleRequestState state) {
        this.state = state;
    }

    public DateTime getAppointmentDateTime() {
        return appointmentDateTime;
    }

    public void setAppointmentDateTime(DateTime appointmentDateTime) {
        this.appointmentDateTime = appointmentDateTime;
    }
}
