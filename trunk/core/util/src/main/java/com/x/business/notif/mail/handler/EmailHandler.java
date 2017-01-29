package com.x.business.notif.mail.handler;

import com.x.business.notif.AbstractNotification;

import java.util.Collection;

/**
 * Created by Olukorede Aguda on 07/08/2016.
 */
public interface EmailHandler {

    void send(AbstractNotification notification);
    void sendBulk(Collection<AbstractNotification> notifications);
}
