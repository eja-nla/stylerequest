package com.x.business.tasks;

import com.hair.business.beans.abstracts.AbstractActorEntity;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * Created by Olukorede Aguda on 22/06/2016.
 *
 */
public class SendgridEmailHandler {

    private static final Logger logger = Logger.getLogger(SendgridEmailHandler.class.getName());

    private static final SendGrid sg = new SendGrid(System.getenv("SENDGRID_API_KEY")); //TODO add API key and use getProperty() instead
    private static final Request request = new Request();

    public static synchronized <T extends AbstractActorEntity> void sendMail(T o){

        try {
            request.method = Method.POST;
            request.endpoint = "mail/send";
            request.body = o.toJson();

            Response response = sg.api(request);

            logger.info("SendGrid email response " + response.statusCode + " Response body " + response.body + " Response header " + response.headers);
        } catch (IOException ex) {
            logger.severe(ex.getMessage());
        } finally {
            request.reset();
        }
    }
}
