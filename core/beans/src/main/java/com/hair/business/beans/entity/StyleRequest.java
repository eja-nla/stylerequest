package com.hair.business.beans.entity;


import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
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
public class StyleRequest extends AbstractActorEntity {

    @Id
    private Long id;

    private Style style;

    private Long merchantId;

    private Long customerId;

    private Location location;

    private StyleRequestState state;

    public StyleRequest(Style style, Long merchantId, Long customerId, Location location, StyleRequestState state, DateTime createdDate) {
        this.style = style;
        this.merchantId = merchantId;
        this.customerId = customerId;
        this.location = location;
        this.state = state;
        this.setCreated(createdDate);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Style getStyle() {
        return style;
    }

    public void setStyle(Style style) {
        this.style = style;
    }

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
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
}
