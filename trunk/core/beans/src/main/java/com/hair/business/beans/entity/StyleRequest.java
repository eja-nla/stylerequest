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

import java.util.Collections;
import java.util.List;

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
    private @Load Ref<Merchant> merchant;
    private @Load Ref<Customer> customer;
    private @Load Ref<Location> location;

    private @Index StyleRequestState state;
    private @Index DateTime appointmentStartTime;
    private @Index DateTime appointmentEndTime;
    private @Index Long customerPermanentId;
    private @Index Long merchantPermanentId;
    private @Index Long locationPermanentId;

    private StyleRequestPayment authorizedPayment;
    private StyleRequestPayment settledPayment;
    private List<AddOn> addOns;

    private @Index DateTime placedTime;
    private @Index DateTime acceptedTime;
    private @Index DateTime cancelledTime;
    private @Index DateTime completedTime;

    public StyleRequest(){
        this.addOns = Collections.emptyList();
        this.placedTime = DateTime.now();
    }

    public StyleRequest(Style style, Merchant merchant, Customer customer, Location location, StyleRequestState state, DateTime appointmentStartTime, DateTime appointmentEndTime) {
        this();
        this.style = Ref.create(style);
        this.merchant = Ref.create(merchant);
        this.customer = Ref.create(customer);
        this.location = Ref.create(location);
        this.state = state;
        this.appointmentStartTime = appointmentStartTime;
        this.appointmentEndTime = appointmentEndTime;

        this.customerPermanentId = customer.getPermanentId();
        this.merchantPermanentId = merchant.getPermanentId();
        this.locationPermanentId = location.getPermanentId();
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

    public Long getCustomerPermanentId() {
        return customerPermanentId;
    }

    public Long getMerchantPermanentId() {
        return merchantPermanentId;
    }

    public Long getLocationPermanentId() {
        return locationPermanentId;
    }

    public DateTime getAppointmentStartTime() {
        return appointmentStartTime;
    }

    public void setAppointmentStartTime(DateTime appointmentStartTime) {
        this.appointmentStartTime = appointmentStartTime;
    }

    public DateTime getAppointmentEndTime() {
        return appointmentEndTime;
    }

    public void setAppointmentEndTime(DateTime appointmentEndTime) {
        this.appointmentEndTime = appointmentEndTime;
    }

    public StyleRequestPayment getAuthorizedPayment() {
        return authorizedPayment;
    }

    public void setAuthorizedPayment(StyleRequestPayment authorizedPayment) {
        this.authorizedPayment = authorizedPayment;
    }

    public StyleRequestPayment getSettledPayment() {
        return settledPayment;
    }

    public void setSettledPayment(StyleRequestPayment settledPayment) {
        this.settledPayment = settledPayment;
    }

    public List<AddOn> getAddOns() {
        return addOns;
    }

    public void setAddOns(List<AddOn> addOns) {
        this.addOns = addOns;
    }

    public DateTime getPlacedTime() {
        return placedTime;
    }

    public void setPlacedTime(DateTime placedTime) {
        this.placedTime = placedTime;
    }

    public DateTime getAcceptedTime() {
        return acceptedTime;
    }

    public void setAcceptedTime(DateTime acceptedTime) {
        this.acceptedTime = acceptedTime;
    }

    public DateTime getCancelledTime() {
        return cancelledTime;
    }

    public void setCancelledTime(DateTime cancelledtime) {
        this.cancelledTime = cancelledtime;
    }

    public DateTime getCompletedTime() {
        return completedTime;
    }

    public void setCompletedTime(DateTime completedTime) {
        this.completedTime = completedTime;
    }
}
