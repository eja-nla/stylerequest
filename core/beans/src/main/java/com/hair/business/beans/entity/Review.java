package com.hair.business.beans.entity;

import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.hair.business.beans.abstracts.AbstractActorEnablerEntity;

/**
 * Review.
 *
 * Created by Olukorede Aguda on 26/04/2016.
 */
@Entity
@Cache
public class Review extends AbstractActorEnablerEntity {

    @Id
    private Long id;
    private String authorDisplayName; // writer of the review
    private String ownerDisplayName; // for who the review is meant

    private long authorId; // to look them up for further subsequent operations
    private long ownerId;

    private int stars;
    private String comment;

    public Review() {
    }

    public Review(String authorDisplayName, String ownerDisplayName, long authorId, long ownerId, int stars, String comment) {
        this();
        this.authorDisplayName = authorDisplayName;
        this.ownerDisplayName = ownerDisplayName;
        this.authorId = authorId;
        this.ownerId = ownerId;
        this.stars = stars;
        this.comment = comment;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAuthorDisplayName() {
        return authorDisplayName;
    }

    public void setAuthorDisplayName(String authorDisplayName) {
        this.authorDisplayName = authorDisplayName;
    }

    public String getOwnerDisplayName() {
        return ownerDisplayName;
    }

    public void setOwnerDisplayName(String ownerDisplayName) {
        this.ownerDisplayName = ownerDisplayName;
    }

    public long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(long authorId) {
        this.authorId = authorId;
    }

    public long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(long ownerId) {
        this.ownerId = ownerId;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
