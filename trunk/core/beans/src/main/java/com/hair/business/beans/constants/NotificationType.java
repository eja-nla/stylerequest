package com.hair.business.beans.constants;

/**
 * Created by Olukorede Aguda on 21/06/2016.
 */
public enum NotificationType {

    EMAIL ("email"),  // email notifs go to merchants and customers
    PUSH ("push"), // push notifs go to devices e.g. to send app updates. Embody updates as json in message would suffice for now
    PUSH_EMAIL ("push and email");

    public final String name;

    NotificationType(String s) {
        name = s;
    }

    @Override
    public String toString(){
        return this.name;
    }
}
