package com.hair.business.beans.entity;


import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.hair.business.beans.abstracts.AbstractActorEnablerEntity;

/**
 * Created by Olukorede Aguda on 25/04/2016.
 *
 * Represents a location of a client or a merchant
 */
@Entity
@Cache
public class Location extends AbstractActorEnablerEntity {

    @Id
    private Long id;
    private String city;
    private String state;
    private String countryCode; // e.g. USA, USA
    private GeoPointExt geo_point; // has the long and lat

//    private String current;
//    private Collection<String> previous;

    public Location(){
    }

    public Location(String city, String state, String countryCode, GeoPointExt geo_point) {
        this.city = city;
        this.state = state;
        this.countryCode = countryCode;
        this.geo_point = geo_point;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public GeoPointExt getGeo_point() {
        return geo_point;
    }

    public void setGeo_point(GeoPointExt geo_point) {
        this.geo_point = geo_point;
    }
}

