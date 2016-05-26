package com.hair.business.beans.entity;

import com.hair.business.beans.abstracts.AbstractActorEntity;

/**
 * Created by Olukorede Aguda on 25/04/2016.
 *
 * Represents a merchant entity
 */
public class Merchant extends AbstractActorEntity {

    private Location location;

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
