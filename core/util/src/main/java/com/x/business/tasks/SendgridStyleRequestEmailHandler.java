package com.x.business.tasks;

import static com.google.appengine.repackaged.com.google.common.collect.Lists.partition;
import static org.slf4j.LoggerFactory.getLogger;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.x.business.notif.AbstractStyleRequestNotificationTask;
import com.x.business.notif.mail.handler.EmailHandler;

import org.slf4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Olukorede Aguda on 22/06/2016.
 *
 */
public class SendgridStyleRequestEmailHandler implements EmailHandler {

    private final Logger logger = getLogger(this.getClass());

    private static final SendGrid sg = new SendGrid(System.getProperty("sendgrid.api.key"));
    private static final String endpoint = "mail/send";

    public SendgridStyleRequestEmailHandler(){
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
        Request request = new Request();
        request.endpoint = endpoint;
        request.method = Method.POST;
        try {
            request.body = body;
            Response response = sg.api(request);

            logger.info("SendGrid email response: {} Response body: {} Response header: {}", response.statusCode, response.body, response.headers);
        } catch (IOException ex) {

            logger.error(ex.getMessage());
        }
    }
}
