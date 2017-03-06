package com.x.business.tasks;

import static com.google.appengine.repackaged.com.google.common.collect.Lists.partition;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.x.business.notif.AbstractStyleRequestNotificationTask;
import com.x.business.notif.mail.handler.EmailHandler;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by Olukorede Aguda on 22/06/2016.
 *
 */
public class SendgridStyleRequestEmailHandler implements EmailHandler {

    private static final Logger logger = Logger.getLogger(SendgridStyleRequestEmailHandler.class.getName());

    private static final SendGrid sg = new SendGrid(System.getProperty("sendgrid.api.key"));
    private static final Request request = new Request();
    private static Response response = null;

    public SendgridStyleRequestEmailHandler(){
        request.method = Method.POST;
        request.endpoint = "mail/send";
    }

    @Override
    public void send(AbstractStyleRequestNotificationTask notification, boolean sendMerchantCopy) {

        send(notification.getCustomerEmailBody());

        if (sendMerchantCopy) {
            send(notification.getMerchantEmailBody());
        }
    }

    @Override
    public void sendBulk(Collection notifications) {
        partition(notifications instanceof List ? (List) notifications : new ArrayList<>(notifications), 1000).forEach(
                listOfNotifications -> ((List) listOfNotifications).forEach(notification -> send((AbstractStyleRequestNotificationTask) notification, false))
        );
    }

    private void send(String body){
        try {
            request.body = body;
            response = sg.api(request);

            logger.fine("SendGrid email response " + response.statusCode + " Response body " + response.body + " Response header " + response.headers);
        } catch (IOException ex) {

            logger.severe(ex.getMessage());
        } finally {
            request.body = StringUtils.EMPTY;
        }
    }
}
