package com.hair.business.beans.constants;

/**
 * Created by Olukorede Aguda on 21/06/2016.
 */
public enum StyleRequestNotificationType {

    PENDING ("Your new style is scheduled"),
    ACCEPTED ("Your style request has been accepted"),
    CANCELLED ("We are sorry, our merchant just cancelled your style request"),
    COMPLETED ("Your style request is now completed");

    public final String name;

    StyleRequestNotificationType(String s) {
        name = s;
    }

    @Override
    public String toString(){
        return this.name;
    }
}
