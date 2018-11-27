package com.hair.business.beans.entity;


import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.hair.business.beans.abstracts.AbstractActorEnablerEntity;

import java.util.List;

/**
 * Represents a style
 *
 * Created by Olukorede Aguda on 25/04/2016.
 *
 */
@Entity
@Cache
public class Style extends AbstractActorEnablerEntity {

    @Id
    private Long id;

    private String name;

    private long requestCount;

    private boolean trending; // separate batch job will run queries and set this field based on a defined criteria

    private boolean active;

    private Long publisherId;

    private Location location;

    private List<Image> styleImages;

    private int durationEstimate; // how long to make this style, in minutes

    private double price;

    public Style(){
        this.trending = true;
        this.active = true;
    }

    public Style(String name, int durationEstimate, Long publisherId, Location location, List<Image> styleImages) {
        this();
        this.name = name;
        this.durationEstimate = durationEstimate;
        this.publisherId = publisherId;
        this.location = location;
        this.styleImages = styleImages;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public List<Image> getStyleImages() {
        return styleImages;
    }

    public void setStyleImages(List<Image>  styleImages) {
        this.styleImages = styleImages;
    }

    public int getDurationEstimate() {
        return durationEstimate;
    }

    public void setDurationEstimate(int durationEstimate) {
        this.durationEstimate = durationEstimate;
    }

    public Long getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(Long publisherId) {
        this.publisherId = publisherId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
