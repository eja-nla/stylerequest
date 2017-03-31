package com.x.business.notif.mail.handler;

import com.x.business.notif.AbstractStyleRequestNotificationTask;

import java.util.Collection;

/**
 * Created by Olukorede Aguda on 07/08/2016.
 *
 *
 */
public interface EmailHandler<T extends AbstractStyleRequestNotificationTask> {

    void send(T notification, boolean sendMerchantCopy);
    void sendBulk(Collection<T> notifications);
}
