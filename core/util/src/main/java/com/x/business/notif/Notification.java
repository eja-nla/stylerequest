package com.x.business.notif;

import com.google.appengine.api.taskqueue.DeferredTask;

import com.hair.business.beans.abstracts.AbstractActorEntity;
import com.x.business.notif.mail.handler.EmailHandler;
import com.x.business.tasks.SendgridEmailSender;

/**
 * Notification object
 * Created by Olukorede Aguda on 21/06/2016.
 */
public abstract class Notification extends AbstractActorEntity implements DeferredTask {

    private static final EmailHandler emailHandler = new SendgridEmailSender();

    @Override
    public void run() {
        emailHandler.send(this);
    }

    public abstract String getBody();
}
