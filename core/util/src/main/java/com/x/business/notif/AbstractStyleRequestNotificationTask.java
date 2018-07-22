package com.x.business.notif;

import static org.slf4j.LoggerFactory.getLogger;

import com.google.appengine.api.datastore.Transaction;
import com.google.appengine.api.taskqueue.DeferredTask;

import com.x.business.notif.mail.handler.EmailHandler;
import com.x.business.scheduler.TaskQueue;
import com.x.business.tasks.SendgridStyleRequestEmailHandler;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Abstract Abstract Stylerequest Notification
 *
 * Created by Olukorede Aguda on 17/02/2017.
 */
public abstract class AbstractStyleRequestNotificationTask extends AbstractEmailNotification {

    private static final EmailHandler emailHandler = new SendgridStyleRequestEmailHandler(new TaskQueue() {
        @Override
        public void add(DeferredTask payload) {

        }

        @Override
        public void add(Transaction txn, DeferredTask payload) {

        }

        @Override
        public void add(Iterable<? extends DeferredTask> payloads) {

        }

        @Override
        public void remove(DeferredTask payload) {

        }
    });//emailTaskQueue);
    private static final Logger logger = getLogger(AbstractStyleRequestNotificationTask.class);
    private static final String adminEmail = System.getProperty("sendgrid.from.email");

    @Override
    public void run() {
        emailHandler.send(this, shouldSendToMerchant());
    }

    public abstract String getCustomerEmailBody();

    public abstract String getMerchantEmailBody();

    protected abstract boolean shouldSendToMerchant();

    static Pair<String, String> getTemplatePair(String customerTemplateFile, String merchantTemplateFile){
        String customerTemplate = getTemplate(customerTemplateFile);
        String merchantTemplate = getTemplate(merchantTemplateFile);

        return new ImmutablePair<>(customerTemplate, merchantTemplate);
    }

    private static String getTemplate(String fileName){

        String template = null;
        try {
            template = new String(Files.readAllBytes(Paths.get(fileName)), StandardCharsets.ISO_8859_1);
        } catch (IOException e){
            logger.error("Unable to load email template file '{}'. Reason: '{}'", fileName, e.getMessage());
        }

        return template;
    }

    String getFromEmail() {
        return adminEmail;
    }
}
