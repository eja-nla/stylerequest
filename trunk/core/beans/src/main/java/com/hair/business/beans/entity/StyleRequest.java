package com.hair.business.beans.entity;


import com.hair.business.beans.abstracts.AbstractActorEnablerEntity;
import com.hair.business.beans.constants.StyleRequestState;

/**
 * Represents a placed style request from client to a merchant
 *
 * Created by Olukorede Aguda on 25/04/2016.
 *
 */
public class StyleRequest extends AbstractActorEnablerEntity {

    private Style style;

    private Merchant merchant;

    private Customer customer;

    private Payment payment;

    private Location location;

    private boolean active;

    private StyleRequestState state;


}
