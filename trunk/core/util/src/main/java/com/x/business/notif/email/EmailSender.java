package com.x.business.notif.email;

import com.x.business.notif.Notification;

/**
 * Email sender interface
 *
 * Created by Olukorede Aguda on 21/06/2016.
 */
public interface EmailSender {

    void sendEmail(Notification notification, byte[] attachmentData);
}
