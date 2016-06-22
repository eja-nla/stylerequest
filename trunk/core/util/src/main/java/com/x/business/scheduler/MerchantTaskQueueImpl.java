package com.x.business.scheduler;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;

import com.x.business.notif.Notification;

/**
 * Created by Olukorede Aguda on 21/06/2016.
 */
public class MerchantTaskQueueImpl implements TaskQueue {

    private Queue taskQueue;
    private static final String QUEUE_NAME = "merchant-email-queue";

    public MerchantTaskQueueImpl() {
        this.taskQueue = QueueFactory.getQueue(QUEUE_NAME);
    }

    public void addNotification(Notification notification) {
        taskQueue.add(TaskOptions.Builder.withPayload(notification));
    }
}
