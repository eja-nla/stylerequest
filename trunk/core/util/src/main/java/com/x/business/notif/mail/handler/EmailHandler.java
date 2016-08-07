package com.x.business.notif.mail.handler;

import com.x.business.notif.Notification;

import java.util.Collection;

/**
 * Created by Olukorede Aguda on 07/08/2016.
 */
public interface EmailHandler {

    void send(Notification notification);
    void sendBulk(Collection<Notification> notifications);
}
