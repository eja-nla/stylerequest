package com.hair.business.beans.constants;

/**
 *
 * Created by Olukorede Aguda on 25/04/2016.
 */
public enum StyleRequestState {

    PENDING ("pending"),   // user has placed request, waiting for the merchant to accept
    ACCEPTED ("accepted"), // merchant acknowledges and a market is made
    IN_PROGRESS ("In Progress"), // changes to in-progress automatically once the start time clocks
    COMPLETED ("completed"), // as marked by merchant
    CANCELLED ("cancelled"); // as issued by merchant or client. Terminal state


    public final String name;

    StyleRequestState(String s) {
        name = s;
    }

    @Override
    public String toString(){
        return this.name;
    }
}
