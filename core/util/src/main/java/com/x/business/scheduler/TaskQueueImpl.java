package com.x.business.scheduler;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;

import com.x.business.notif.Notification;

/**
 * Created by Olukorede Aguda on 21/06/2016.
 */
public class TaskQueueImpl implements TaskQueue {

    private Queue taskQueue;
    private static final String NOTIFICATION_URL = "/notifications";

    public TaskQueueImpl() {
        this.taskQueue = QueueFactory.getDefaultQueue();
    }

    public void addNotification(Notification notification) {
        taskQueue.add(TaskOptions.Builder.withPayload(notification));
    }
}
