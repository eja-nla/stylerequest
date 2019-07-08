package com.hair.business.beans.entity;

import com.hair.business.beans.abstracts.AbstractBean;

/**
 * Created by Olukorede Aguda on 23/06/2016.
 *
 * Wrapper around GeoPoint due to Guice no-arg nagging. Should revisit
 */
public class GeoPointExt extends AbstractBean {

    private double lat;
    private double lon;

    public GeoPointExt(){}

    public GeoPointExt(double lat, double lon) {
        this();
        this.lat = lat;
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }
}
