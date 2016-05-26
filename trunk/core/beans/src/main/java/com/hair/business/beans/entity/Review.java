package com.hair.business.beans.entity;

import com.hair.business.beans.abstracts.AbstractActorEntity;

/**
 * Review.
 *
 * Created by Olukorede Aguda on 26/04/2016.
 */

public class Review extends AbstractActorEntity {

    private String authorDisplayName; // writer of the review
    private String ownerDisplayName; // for who the review is meant

    private long authorId; // to look them up for further subsequent operations
    private long ownerId;

    private int stars;
    private String comment;


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
