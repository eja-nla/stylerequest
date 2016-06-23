package com.x.business.tasks;

import com.google.appengine.api.taskqueue.DeferredTask;

import com.x.business.utilities.Assert;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Optional;
import java.util.Properties;
import java.util.logging.Logger;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 * Created by Olukorede Aguda on 22/06/2016.
 */
public class EmailTask implements DeferredTask {

    static final Logger logger = Logger.getLogger(EmailTask.class.getName());

    final String from;
    final String to;
    final String cc;
    final String bcc;
    final String subject;
    final InputStream attachment;
    final String message;
    final String filename;
    final String contentType;

    public EmailTask(String from, String to, String cc, String bcc, String subject, InputStream attachment, String message, String filename, String contentType) {
        this.from = from;
        this.to = to;
        this.cc = cc;
        this.bcc = bcc;
        this.subject = subject;
        this.attachment = attachment;
        this.message = message;
        this.filename = filename;
        this.contentType = contentType;
    }


    @Override
    public void run() {
        send();
    }

    public void send(){
        Assert.notNull(to, "TO address is required.");
        Assert.notNull(message, "Email message is required.");
        Assert.notNull(subject, "Subject is required.");

        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        Multipart mp = new MimeMultipart();

        MimeBodyPart htmlPart = new MimeBodyPart();

        try {

            mp.addBodyPart(htmlPart);

            MimeBodyPart attachment = new MimeBodyPart();
            attachment.setFileName(filename);
            attachment.setContent(attachment, contentType);
            mp.addBodyPart(attachment);


            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(from, "Example.com Admin"));
            msg.addRecipient(Message.RecipientType.TO, new InternetAddress(to, "To personal"));

            Optional.of(cc).ifPresent(d -> {
                try {
                    msg.addRecipient(Message.RecipientType.CC, new InternetAddress(cc, "CC personal"));
                } catch (MessagingException | UnsupportedEncodingException e) {
                    logger.severe(e.getMessage());
                }
            });

            Optional.of(bcc).ifPresent(d -> {
                try {
                    msg.addRecipient(Message.RecipientType.BCC, new InternetAddress(bcc, "BCC personal"));
                } catch (MessagingException | UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            });
            msg.setSubject(subject);

            msg.setContent(mp);

            Transport.send(msg);
        } catch (AddressException e) {
            logger.severe(e.getMessage());
        } catch (MessagingException e) {
            logger.severe(e.getMessage());
        } catch (UnsupportedEncodingException e) {
            logger.severe(e.getMessage());
        } catch (IOException e){
            logger.severe(e.getMessage());
        }
    }

}
