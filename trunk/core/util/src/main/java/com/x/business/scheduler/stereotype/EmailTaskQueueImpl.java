package com.x.business.scheduler.stereotype;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;

import com.x.business.scheduler.AbstractTaskQueue;

import javax.inject.Named;

/**
 * Created by Olukorede Aguda on 07/08/2016.
 */
@Named
public class EmailTaskQueueImpl extends AbstractTaskQueue {

    private final Queue queue;
    private final String queueName = "email-queue";

    public EmailTaskQueueImpl() {
        super();
        this.queue = QueueFactory.getQueue(queueName);
    }

}
