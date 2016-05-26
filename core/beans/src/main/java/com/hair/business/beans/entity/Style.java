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


}
