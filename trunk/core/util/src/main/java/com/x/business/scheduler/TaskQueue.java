package com.x.business.scheduler;

import com.x.business.notif.Notification;

/**
 * Created by Olukorede Aguda on 21/06/2016.
 */
public interface TaskQueue {
    //TODO implement push queue to send push notifications
    // look in https://cloud.google.com/appengine/docs/java/taskqueue/
    void addNotification (Notification notification);
}
