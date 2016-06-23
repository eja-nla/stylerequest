package com.x.business.notif;

import com.googlecode.objectify.annotation.Id;
import com.hair.business.beans.abstracts.AbstractPersistenceEntity;
import com.hair.business.beans.constants.NotificationType;
import com.hair.business.beans.entity.Payment;
import com.hair.business.beans.entity.StyleRequest;
import com.x.business.scheduler.TaskQueue;
import com.x.business.tasks.EmailTask;

import javax.inject.Named;

/**
 * Notification object
 * Created by Olukorede Aguda on 21/06/2016.
 */
public class Notification<T extends AbstractPersistenceEntity> {

    @Id
    private Long id;

    private String message;

    private Long from; // id of sender

    private Long to; // id of recipient

    private NotificationType type;

    private T value;

    @Named("app.admin.email") String adminEmail;

//    public Notification(String message, Long from, Long to, NotificationType type) {
//        this.message = message;
//        this.from = from;
//        this.to = to;
//        this.type = type;
//    }

    public Notification(T o, NotificationType type) {
        this.value = o;
        this.type = type;
    }

    public void schedule(){
        if (type.equals(NotificationType.EMAIL)){
            // add to email task queue
            if(value instanceof StyleRequest){
                StyleRequest request = (StyleRequest) value;
                EmailTask mailTask = new EmailTask(adminEmail, request.getMerchant().getEmail(), null, null, "New Style Request", null, "I'd like to fix my hair on " + request.getDate(), null, "text/html");

                TaskQueue.emailQueue().add(mailTask);
            }

            if(value instanceof Payment){
                Payment request = (Payment) value;
                EmailTask mailTask = new EmailTask(adminEmail, request.getMerchant().getEmail(), null, null, "New Style Request", null, "I'd like to fix my hair on " + request.getDate(), null, "text/html");

                TaskQueue.emailQueue().add(mailTask);
            }

        }
        if (type.equals(NotificationType.PUSH)){
            // add to push task queue
        }
        if (type.equals(NotificationType.PUSH_EMAIL)){
            // add to both
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getFrom() {
        return from;
    }

    public void setFrom(Long from) {
        this.from = from;
    }

    public Long getTo() {
        return to;
    }

    public void setTo(Long to) {
        this.to = to;
    }

    public NotificationType getType() {
        return type;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }


}
