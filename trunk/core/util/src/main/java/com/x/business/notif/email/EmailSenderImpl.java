package com.x.business.notif.email;

import com.google.appengine.api.taskqueue.DeferredTask;

import com.x.business.notif.Notification;

import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

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
 * Email sender impl
 *
 * Created by Olukorede Aguda on 21/06/2016.
 */
public class EmailSenderImpl implements EmailSender, DeferredTask {


    public void sendEmail(Notification notification, byte[] attachmentData){
        String htmlBody = StringUtils.EMPTY;
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        Multipart mp = new MimeMultipart();

        MimeBodyPart htmlPart = new MimeBodyPart();

        try {

            htmlPart.setContent(htmlBody, "text/html");
            mp.addBodyPart(htmlPart);

            MimeBodyPart attachment = new MimeBodyPart();
            InputStream attachmentDataStream = new ByteArrayInputStream(attachmentData);
            attachment.setFileName("manual.pdf");
            attachment.setContent(attachmentDataStream, "application/html");
            mp.addBodyPart(attachment);


            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress("koredyte@gmail.com", "Example.com Admin"));
            msg.addRecipient(Message.RecipientType.TO, new InternetAddress("koredyte@gmail.com", "Mr. agudao from Amyrrh"));
            msg.setSubject("Your Example.com account has been activated");

            msg.setContent(mp);

            Transport.send(msg);
        } catch (AddressException e) {
            // ...
        } catch (MessagingException e) {
            // ...
        } catch (UnsupportedEncodingException e) {
            // ...
        } catch (IOException e){

        }
    }

    public void run() {

    }
}
