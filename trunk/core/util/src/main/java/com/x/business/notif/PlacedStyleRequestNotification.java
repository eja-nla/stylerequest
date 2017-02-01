package com.x.business.notif;

import com.hair.business.beans.entity.StyleRequest;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Logger;

/**
 * AbstractNotification object
 * Created by Olukorede Aguda on 21/06/2016.
 */
public class PlacedStyleRequestNotification extends AbstractNotification {

    private String body;

    private static String emailBody = null;
    private static final String senderEmail = System.getProperty("SENDGRID_FROM_EMAIL");

    private static final Logger LOGGER = Logger.getLogger(PlacedStyleRequestNotification.class.getName());

    static {

        String new_stylerequest_templateFile = System.getProperty("SENDGRID_PLACED_STYLE_EMAIL_TEMPLATE_FILE");

        try {
            emailBody = new String(Files.readAllBytes(Paths.get(new_stylerequest_templateFile)), StandardCharsets.ISO_8859_1);
        } catch (IOException e){
            LOGGER.severe(String.format("Unable to load email template file '%s'. Reason: %s", new_stylerequest_templateFile, e.getMessage()));
        }

    }

    public PlacedStyleRequestNotification(StyleRequest styleRequest) {

        this.body = String.format(emailBody,
                styleRequest.getMerchant().getEmail(),
                styleRequest.getCustomer().getName().split(StringUtils.SPACE)[0],
                styleRequest.getStyle().getName(),
                styleRequest.getAppointmentStartTime().toDate(),
                styleRequest.getMerchant().getName(),
                senderEmail
        );
    }


    @Override
    public String getBody() {
        return body;
    }
}
