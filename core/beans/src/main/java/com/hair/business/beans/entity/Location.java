package com.hair.business.beans.entity;


import com.hair.business.beans.abstracts.AbstractActorEnablerEntity;

import java.util.Collection;

/**
 * Created by Olukorede Aguda on 25/04/2016.
 *
 * Represents a location of a client or a merchant
 */
public class Location extends AbstractActorEnablerEntity {

    private String city;
    private String state;
    private String country;
    private GeoPointExt geoPoint; // has the long and lat

    private String current;
    private Collection<String> previous;

    public Location(){
    }

    public Location(String city, String state, String country, GeoPointExt geoPoint) {
        this();
        this.city = city;
        this.state = state;
        this.country = country;
        this.geoPoint = geoPoint;
    }

    public String getCurrent() {
        return current;
    }

    public void setCurrent(String current) {
        this.current = current;
    }

    public Collection<String> getPrevious() {
        return previous;
    }

    public void setPrevious(Collection<String> previous) {
        this.previous = previous;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public GeoPointExt getGeoPoint() {
        return geoPoint;
    }

    public void setGeoPoint(GeoPointExt geoPoint) {
        this.geoPoint = geoPoint;
    }
}

