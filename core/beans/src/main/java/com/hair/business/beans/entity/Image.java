package com.hair.business.beans.entity;

import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.hair.business.beans.abstracts.AbstractActorEnablerEntity;

/**
 * An image.
 *
 * Could be a photo or video
 *
 * Created by Olukorede Aguda on 26/04/2016.
 */
@Entity
@Cache
public class Image extends AbstractActorEnablerEntity {

    @Id
    private Long id;

    private String url;

    private String owner;

    private long views;

    public Image() {
    }

    public Image(String url, String owner, long views) {
        this();
        this.url = url;
        this.owner = owner;
        this.views = views;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
