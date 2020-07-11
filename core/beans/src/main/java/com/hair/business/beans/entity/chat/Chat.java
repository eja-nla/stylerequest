package com.hair.business.beans.entity.chat;


import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.hair.business.beans.abstracts.AbstractActorEnablerEntity;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Chat between two actors
 *
 * Created by Olukorede Aguda on 10/07/2020.
 *
 */
@Entity
public class Chat extends AbstractActorEnablerEntity {

    @Id
    private Long id;
    private Long customerId;
    private Long merchantId;
    private Long styleRequestId;
    private List<Message> messages;

    public Chat() {
        this.messages = new ArrayList<>(2);
    }

    private DateTime startedOn;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DateTime getStartedOn() {
        return startedOn;
    }

    public void setStartedOn(DateTime startedOn) {
        this.startedOn = startedOn;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public Long getStyleRequestId() {
        return styleRequestId;
    }

    public void setStyleRequestId(Long styleRequestId) {
        this.styleRequestId = styleRequestId;
    }
}
