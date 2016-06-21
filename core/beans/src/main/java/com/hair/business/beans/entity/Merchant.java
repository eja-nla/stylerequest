package com.hair.business.beans.entity;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.hair.business.beans.abstracts.AbstractActorEntity;

/**
 * Created by Olukorede Aguda on 25/04/2016.
 *
 * Represents a merchant entity
 */
@Entity
public class Merchant extends AbstractActorEntity {

    @Id
    private Long id;

    private Location location;

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
