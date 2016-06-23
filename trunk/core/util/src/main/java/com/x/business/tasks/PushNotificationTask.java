package com.x.business.tasks;

import com.google.appengine.api.taskqueue.DeferredTask;

/**
 * Created by Olukorede Aguda on 22/06/2016.
 */
public class PushNotificationTask implements DeferredTask {


    @Override
    public void run() {
        send();
    }

    public void send(){
        //TODO implement
    }

}
