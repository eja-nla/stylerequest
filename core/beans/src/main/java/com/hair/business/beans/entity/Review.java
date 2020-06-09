package com.hair.business.beans.entity;

import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.condition.IfNotNull;
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

    @Index private long authorId; // to look them up for further subsequent operations
    @Index private long ownerId;

    @Index private double overallStars;
    @Index private double easyToFindStars;  //how easy is navigating to them or them to you?
    @Index private double professionalismStars; // how is their courtesy? Are they kind, understanding and friendly?
    @Index private double skilledStars; // how is good is their work, their fading/cut/trim?


    @Index({IfNotNull.class}) private String comment;

    public Review() {
    }

    public Review(String authorDisplayName, String ownerDisplayName, long authorId, long ownerId, int overallStars,  int skilledStars, String comment) {
        this();
        this.authorDisplayName = authorDisplayName;
        this.ownerDisplayName = ownerDisplayName;
        this.authorId = authorId;
        this.ownerId = ownerId;
        this.overallStars = overallStars;
        this.skilledStars = skilledStars;
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

    public double getOverallStars() {
        return (professionalismStars + easyToFindStars) / 2;
    }

    public void setOverallStars(int overallStars) {
        this.overallStars = overallStars;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public double getEasyToFindStars() {
        return easyToFindStars;
    }

    public void setEasyToFindStars(double easyToFindStars) {
        this.easyToFindStars = easyToFindStars;
    }

    public double getProfessionalismStars() {
        return professionalismStars;
    }

    public void setProfessionalismStars(int professionalismStars) {
        this.professionalismStars = professionalismStars;
    }

    public double getSkilledStars() {
        return skilledStars;
    }

    public void setSkilledStars(int skilledStars) {
        this.skilledStars = skilledStars;
    }
}
