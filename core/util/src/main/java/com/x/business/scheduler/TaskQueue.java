package com.x.business.scheduler;

import com.google.appengine.api.datastore.Transaction;
import com.google.appengine.api.taskqueue.DeferredTask;

/**
 * Created by Olukorede Aguda on 07/08/2016.
 */
public interface TaskQueue {

    void add(DeferredTask payload);

    void add(Transaction txn, DeferredTask payload);

    /** Allows any number of tasks; automatically partitions as necessary */
    void add(Iterable<? extends DeferredTask> payloads);

}
