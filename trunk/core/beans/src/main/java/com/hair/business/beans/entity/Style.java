package com.hair.business.beans.entity;


import com.hair.business.beans.abstracts.AbstractActorEnablerEntity;

/**
 * Represents a style
 *
 * Created by Olukorede Aguda on 25/04/2016.
 *
 */
public class Style extends AbstractActorEnablerEntity {

    private long id;

    private long requestCount; // how many styleRequests on this ever?

    private boolean trending; // separate batch job will run queries and set this field based on a defined criteria

    private boolean active;

    private Location location;

    public Style(long requestCount, boolean trending, boolean active, Location location) {
        this.requestCount = requestCount;
        this.trending = trending;
        this.active = active;
        this.location = location;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getRequestCount() {
        return requestCount;
    }

    public void setRequestCount(long requestCount) {
        this.requestCount = requestCount;
    }

    public boolean isTrending() {
        return trending;
    }

    public void setTrending(boolean trending) {
        this.trending = trending;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
