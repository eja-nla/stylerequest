package com.x.business.tasks;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.x.business.notif.Notification;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * Created by Olukorede Aguda on 22/06/2016.
 *
 */
public class SendgridEmailHandler {

    private static final Logger logger = Logger.getLogger(SendgridEmailHandler.class.getName());

    private static final SendGrid sg = new SendGrid(System.getProperty("SENDGRID_API_KEY"));
    private static final Request request = new Request();
    private static Response response = null;

    public static synchronized void sendMail(Notification n){

        try {
            request.method = Method.POST;
            request.endpoint = "mail/send";
            request.body = n.getBody();

            response = sg.api(request);

            logger.info("SendGrid email response " + response.statusCode + " Response body " + response.body + " Response header " + response.headers);
        } catch (IOException ex) {
            logger.severe(ex.getMessage());
        } finally {
            request.reset();
        }
    }
}