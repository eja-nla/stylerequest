package com.x.business.tasks;

import static com.google.appengine.repackaged.com.google.common.collect.Lists.partition;
import static org.slf4j.LoggerFactory.getLogger;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.x.business.notif.AbstractStyleRequestNotificationTask;
import com.x.business.notif.mail.handler.EmailHandler;
import com.x.business.scheduler.TaskQueue;
import com.x.business.scheduler.stereotype.ApnsTaskQueue;

import org.slf4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Olukorede Aguda on 22/06/2016.
 *
 */
public class SendgridStyleRequestEmailHandler implements EmailHandler {

    private static final Logger logger = getLogger(SendgridStyleRequestEmailHandler.class);

    private static final SendGrid sg = new SendGrid(System.getProperty("sendgrid.api.key"));
    private static final String endpoint = "mail/send";
    private static final ThreadLocal<Request> request = ThreadLocal.withInitial(Request::new);
    private static final List<Request> failedRequests = new ArrayList<>();
    private final TaskQueue emailTaskQueue;

    @Inject
    public SendgridStyleRequestEmailHandler(@ApnsTaskQueue TaskQueue emailTaskQueue){
        this.emailTaskQueue = emailTaskQueue;
    }

    @Override
    public void send(AbstractStyleRequestNotificationTask notification, boolean sendMerchantCopy) {

        try {
            send(notification.getCustomerEmailBody());

            if (sendMerchantCopy) {
                send(notification.getMerchantEmailBody());
            }
        } finally {
            emailTaskQueue.remove(notification);
        }

    }

    @Override
    public void sendBulk(Collection notifications) {
        List partitions = partition(notifications instanceof List ? (List) notifications : Arrays.asList(notifications), 1000);
        for (int i = 0; i < partitions.size(); i++) {
            send((AbstractStyleRequestNotificationTask) partitions.get(i), false);
        }
//        partitions.forEach(
//                listOfNotifications -> ((List) listOfNotifications).forEach(notification -> send((AbstractStyleRequestNotificationTask) notification, false))
//        );
    }

    private void send(String body){
        Request r = request.get();
        r.endpoint = endpoint;
        r.method = Method.POST; // do these inits elsewhere, once. Maybe in threadlocal's get?
        try {
            r.body = body;
            Response response = sg.api(r);

            logger.debug("SendGrid email response: {} Response body: {} Response header: {}", response.statusCode, response.body, response.headers);
        } catch (IOException ex) {

            logger.error(ex.getMessage());
        } catch (Exception e) {
            // put the failed requests into some store to resend them once the problem is resolved
            // add task to continously drain this
            //emailTaskQueue.add(new D);
            failedRequests.add(clone(request));
        } finally {
            r.body = "";
        }
    }

    private Request clone(ThreadLocal<Request> request) {
        Request req = new Request();
        req.baseUri = request.get().baseUri;
        req.body = request.get().body;
        req.endpoint = request.get().endpoint;
        req.headers = request.get().headers;
        req.method = request.get().method;
        req.queryParams = request.get().queryParams;
        return req;
    }
}
