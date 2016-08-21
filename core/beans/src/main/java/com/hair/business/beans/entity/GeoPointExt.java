package com.hair.business.beans.entity;

import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.hair.business.beans.abstracts.AbstractBean;

/**
 * Created by Olukorede Aguda on 23/06/2016.
 *
 * Wrapper around GeoPoint due to Guice no-arg nagging. Should revisit
 */
@Entity
@Cache
public class GeoPointExt extends AbstractBean {

    @Id
    private Long id;
    private double latitude;
    private double longitude;

    public GeoPointExt(){}

    public GeoPointExt(double latitude, double longitude) {
        this();
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
