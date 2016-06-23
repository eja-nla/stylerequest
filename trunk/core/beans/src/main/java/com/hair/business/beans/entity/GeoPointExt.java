package com.hair.business.beans.entity;

/**
 * Created by Olukorede Aguda on 23/06/2016.
 *
 * Wrapper around GeoPoint due to Guice no-arg nagging. Should revisit
 */
public class GeoPointExt {

    private double latitude;
    private double longitude;

    public GeoPointExt(){}

    public GeoPointExt(double latitude, double longitude) {
        this();
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
