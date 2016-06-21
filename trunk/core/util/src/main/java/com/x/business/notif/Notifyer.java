package com.x.business.notif;

/**
 * Created by Olukorede Aguda on 21/06/2016.
 */
public interface Notifyer {

    /**
     * Sends a notification to the given entity
     * Returns a notification ID
     * */
    String notifyEntity();
}
