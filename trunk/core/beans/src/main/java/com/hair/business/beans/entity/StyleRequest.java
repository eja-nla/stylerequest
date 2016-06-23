package com.hair.business.beans.entity;


import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Load;
import com.hair.business.beans.abstracts.AbstractActorEntity;
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
public class StyleRequest extends AbstractActorEntity {

    @Id
    private Long id;

    private @Load Ref<Style> style;

    private @Load Ref<Merchant> merchant; // with @Load, Ofy efficiently fetches for large dataset

    private @Load Ref<Customer> customer;

    private Location location;

    private @Index StyleRequestState state;

    private DateTime date;

    public StyleRequest(){}

    public StyleRequest(Style style, Merchant merchant, Customer customer, Location location, StyleRequestState state, DateTime date) {
        this();
        this.style = Ref.create(style);
        this.merchant = Ref.create(merchant);
        this.customer = Ref.create(customer);
        this.location = location;
        this.state = state;
        this.date = date;
        this.setCreated(DateTime.now());
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

    public void setStyle(Ref<Style> style) {
        this.style = style;
    }

    public Merchant getMerchant() {
        return merchant.get();
    }

    public void setMerchant(Ref<Merchant> merchant) {
        this.merchant = merchant;
    }

    public void setCustomer(Ref<Customer> customer) {
        this.customer = customer;
    }

    public Customer getCustomer() {
        return customer.get();
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public StyleRequestState getState() {
        return state;
    }

    public void setState(StyleRequestState state) {
        this.state = state;
    }

    public DateTime getDate() {
        return date;
    }

    public void setDate(DateTime date) {
        this.date = date;
    }
}
