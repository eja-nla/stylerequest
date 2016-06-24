package com.hair.business.beans.entity;


import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.hair.business.beans.abstracts.AbstractActorEnablerEntity;

import org.joda.time.DateTime;

import java.util.Collection;

/**
 * Represents a style
 *
 * Created by Olukorede Aguda on 25/04/2016.
 *
 */
@Entity
public class Style extends AbstractActorEnablerEntity {

    @Id
    private Long id;

    private long requestCount; // how many styleRequests on this ever?

    private boolean trending; // separate batch job will run queries and set this field based on a defined criteria

    private boolean active;

    private Merchant publisher;

    private Location location;

    private DateTime date;

    private Collection<Image> styleImages; // a style can have multiple images e.g. showing the sides, the back, the front etc.

    public Style(){}

    public Style(long requestCount, Merchant publisher, boolean trending, boolean active, Location location) {
        this();
        this.requestCount = requestCount;
        this.publisher = publisher;
        this.trending = trending;
        this.active = active;
        this.location = location;
        this.date = DateTime.now();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Merchant getPublisher() {
        return publisher;
    }

    public void setPublisher(Merchant publisher) {
        this.publisher = publisher;
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

    public Collection<Image> getStyleImages() {
        return styleImages;
    }

    public void setStyleImages(Collection<Image>  styleImages) {
        this.styleImages = styleImages;
    }

    public DateTime getDate() {
        return date;
    }

    public void setDate(DateTime date) {
        this.date = date;
    }
}