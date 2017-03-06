package com.x.business.notif.mail.handler;

import com.x.business.notif.AbstractStyleRequestNotificationTask;

import java.util.Collection;

/**
 * Created by Olukorede Aguda on 07/08/2016.
 *
 *
 */
public interface EmailHandler {

    <R extends AbstractStyleRequestNotificationTask> void send(R notification, boolean sendMerchantCopy);
    <T extends AbstractStyleRequestNotificationTask> void sendBulk(Collection<T> notifications);
}
