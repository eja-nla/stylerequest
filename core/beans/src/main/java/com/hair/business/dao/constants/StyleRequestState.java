package com.hair.business.dao.constants;

/**
 * Created by Olukorede Aguda on 25/04/2016.
 */
public enum StyleRequestState {

    PENDING ("pending"),
    ACCEPTED ("accepted"),
    IN_PROGRESS ("In Progress"), // changes to in-progress automatically once the start time clocks
    COMPLETED ("completed"),
    CANCELLED ("cancelled");


    public final String name;

    StyleRequestState(String s) {
        name = s;
    }

    @Override
    public String toString(){
        return this.name;
    }
}
