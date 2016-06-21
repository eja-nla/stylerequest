package com.x.business.notif;

import com.google.appengine.api.taskqueue.DeferredTask;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.hair.business.beans.abstracts.AbstractActorEnablerEntity;
import com.hair.business.beans.constants.NotificationType;

/**
 * Notification object
 * Created by Olukorede Aguda on 21/06/2016.
 */
@Entity
public class Notification extends AbstractActorEnablerEntity implements DeferredTask {

    @Id
    private Long id;

    private String message;

    private Long from; // id of sender

    private Long to; // id of recipient

    private NotificationType type;

    public Notification(String message, Long from, Long to, NotificationType type) {
        this.message = message;
        this.from = from;
        this.to = to;
        this.type = type;
    }

    public void run() {
        // TODO depending on the notification type, send email or sent push notification
        // This should probably move elsewhere as it's no longer a plain entity

        switch (type) {
            case EMAIL:
                //do create an EmailSenderImpl to send the email based on this
                break;
            case PUSH:
                // do stuff
                break;
            case PUSH_EMAIL:
                //do stuff
                break;
            default:
                // nothing?
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getFrom() {
        return from;
    }

    public void setFrom(Long from) {
        this.from = from;
    }

    public Long getTo() {
        return to;
    }

    public void setTo(Long to) {
        this.to = to;
    }

    public NotificationType getType() {
        return type;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }


}
