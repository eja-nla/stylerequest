package com.x.business.notif;

import com.hair.business.beans.entity.Customer;

/**
 * Created by Olukorede Aguda on 21/06/2016.
 */
public interface CustomerNotifyer extends Notifyer {

    /**
     * Emails a customer with details
     *
     * Gets notifications from TaskQueue
     * */
    String sendEmailNotification(Customer customer, Notification notif);

    /**
     * Sends push notification
     *
     * Gets notifications from TaskQueue
     * */
    String sendPushNotification(Customer customer, Notification notif);
}
