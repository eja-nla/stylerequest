package com.x.business.config;

import com.google.inject.AbstractModule;

import com.x.business.scheduler.TaskQueue;
import com.x.business.scheduler.stereotype.ApnsTaskQueue;
import com.x.business.scheduler.stereotype.ApnsTaskQueueImpl;
import com.x.business.scheduler.stereotype.EmailTaskQueue;
import com.x.business.scheduler.stereotype.EmailTaskQueueImpl;
import com.x.business.tasks.SendgridStyleRequestEmailHandler;

import javax.inject.Singleton;


/**
 * Created by Olukorede Aguda on 21/06/2016.
 *
 * Util Module
 */
public class UtilModule extends AbstractModule {

    @Override
    protected void configure() {

        bind(SendgridStyleRequestEmailHandler.class).in(Singleton.class);

        bind(TaskQueue.class).annotatedWith(ApnsTaskQueue.class).to(ApnsTaskQueueImpl.class).in(Singleton.class);
        bind(TaskQueue.class).annotatedWith(EmailTaskQueue.class).to(EmailTaskQueueImpl.class).in(Singleton.class);

    }

}
