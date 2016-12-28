package com.x.business.tasks;

import static com.google.appengine.repackaged.com.google.common.collect.Lists.partition;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.x.business.notif.Notification;
import com.x.business.notif.mail.handler.EmailHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by Olukorede Aguda on 22/06/2016.
 *
 */
public class SendgridEmailHandler implements EmailHandler {

    private static final Logger logger = Logger.getLogger(SendgridEmailHandler.class.getName());

    private static final SendGrid sg = new SendGrid(System.getProperty("SENDGRID_API_KEY"));
    private static final Request request = new Request();
    private static Response response = null;

    @Override
    public synchronized void send(Notification notification) {
        try {
            request.method = Method.POST;
            request.endpoint = "mail/send";
            request.body = notification.getBody();

            response = sg.api(request);

            logger.info("SendGrid email response " + response.statusCode + " Response body " + response.body + " Response header " + response.headers);
        } catch (IOException ex) {
            logger.severe(ex.getMessage());
        } finally {
            request.reset();
        }
    }

    /**
     * Lets partition the giant collection into smaller collections
     * */
    @Override
    public void sendBulk(Collection<Notification> notifications) {
        partition(notifications instanceof List ? (List) notifications : new ArrayList<>(notifications), 1000).forEach(
                listOfNotifications -> ((List) listOfNotifications).forEach(notification -> send((Notification) notification))
        );
    }
}
