package com.hair.business.beans.entity;

import com.hair.business.beans.abstracts.AbstractActorEntity;

/**
 * An image.
 *
 * Could be a photo or video
 *
 * Created by Olukorede Aguda on 26/04/2016.
 */

public class Image extends AbstractActorEntity {

    private String url;

    private String owner;

    private long views;

    public Image(String url, String owner, long views) {
        this.url = url;
        this.owner = owner;
        this.views = views;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public long getViews() {
        return views;
    }

    public void setViews(long views) {
        this.views = views;
    }
}
