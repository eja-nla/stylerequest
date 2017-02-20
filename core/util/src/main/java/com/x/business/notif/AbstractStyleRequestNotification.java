package com.x.business.notif;

import com.x.business.notif.mail.handler.EmailHandler;
import com.x.business.tasks.SendgridStyleRequestEmailHandler;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Logger;

/**
 * Abstract Abstract Stylerequest Notification
 *
 * Created by Olukorede Aguda on 17/02/2017.
 */
public abstract class AbstractStyleRequestNotification extends AbstractNotification {

    private static final EmailHandler emailHandler = new SendgridStyleRequestEmailHandler();
    private static final Logger LOGGER = Logger.getLogger(PlacedStyleRequestNotification.class.getName());
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
            LOGGER.severe(String.format("Unable to load email template file %s. Reason: %s", fileName, e.getMessage()));
        }

        return template;
    }

    String getFromEmail() {
        return adminEmail;
    }
}
