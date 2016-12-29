package com.x.business.notif;

import com.google.appengine.api.taskqueue.DeferredTask;

import com.googlecode.objectify.annotation.Id;
import com.hair.business.beans.abstracts.AbstractActorEntity;
import com.hair.business.beans.constants.NotificationType;
import com.hair.business.beans.entity.StyleRequest;
import com.sendgrid.Attachments;
import com.sendgrid.Email;
import com.sendgrid.Personalization;
import com.x.business.notif.mail.handler.EmailHandler;
import com.x.business.tasks.SendgridEmailSender;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

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

    private String body;

    private static String emailBody = null;

    private static final String adminEmail = System.getProperty("SENDGRID_FROM_EMAIL");

    private static final EmailHandler emailHandler = new SendgridEmailSender();

    static {
        try {
            emailBody = new String(Files.readAllBytes(Paths.get(System.getProperty("SENDGRID_NEW_STYLE_EMAIL_TEMPLATE_FILE"))), StandardCharsets.ISO_8859_1);
        } catch (IOException e){
        }

    }
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

        this.body = String.format(emailBody,
                styleRequest.getMerchant().getEmail(),
                styleRequest.getCustomer().getName().split(StringUtils.SPACE)[0],
                styleRequest.getStyle().getName(),
                styleRequest.getAppointmentDateTime(),
                styleRequest.getMerchant().getName(),
                adminEmail
        );
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    @Override
    public void run() {
        emailHandler.send(this);
    }

}
