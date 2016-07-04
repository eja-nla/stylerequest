package com.x.business.notif;

import com.google.appengine.api.taskqueue.DeferredTask;

import com.googlecode.objectify.annotation.Id;
import com.hair.business.beans.abstracts.AbstractActorEntity;
import com.hair.business.beans.constants.NotificationType;
import com.hair.business.beans.entity.Payment;
import com.hair.business.beans.entity.StyleRequest;
import com.sendgrid.Attachments;
import com.sendgrid.Email;
import com.sendgrid.Personalization;
import com.x.business.tasks.SendgridEmailHandler;

import java.util.Optional;

import javax.inject.Named;

/**
 * Notification object
 * Created by Olukorede Aguda on 21/06/2016.
 */
public class Notification extends AbstractActorEntity implements DeferredTask {

    @Id
    private Long id;

    private String message;

    private Email from;

    private Email to;

    private Attachments attachments;

    private Personalization[] personalizations;

    private NotificationType type;

    private static @Named("app.admin.email") String adminEmail; // FIXME: 03/07/2016 Can't inject static

    public Notification(Long id, String message, Email from, Email to, Attachments attachments, Personalization[] personalizations, NotificationType type) {
        this.id = id;
        this.message = message;
        this.from = from;
        this.to = to;
        this.attachments = attachments;
        this.personalizations = personalizations;
        this.type = type;
    }

    public Notification(StyleRequest styleRequest, NotificationType type) {
        super();

        this.message = "You've got a new Style Request"; // TODO should be a template html with this injected message
        this.from = new Email(Optional.ofNullable(adminEmail).orElse("koredyte@gmail.com"), "Style Request");
        this.to = new Email(styleRequest.getMerchant().getEmail(), styleRequest.getMerchant().getName());
        this.attachments = new Attachments();

        this.type = type;
    }

    public Notification(Payment payment, NotificationType type) {
        super();

        this.message = "You've got a new Payment"; // TODO should be a template html with this injected message
        this.from = new Email(Optional.ofNullable(adminEmail).orElse("koredyte@gmail.com"), "Style Request");
        this.to = new Email(payment.getMerchant().getEmail(), payment.getMerchant().getName());
        this.attachments = new Attachments();

        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public void run() {
        SendgridEmailHandler.sendMail(this);
    }
}
