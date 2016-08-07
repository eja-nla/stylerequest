package com.x.business.scheduler;

import com.google.appengine.api.datastore.Transaction;
import com.google.appengine.api.taskqueue.DeferredTask;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueConstants;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.google.common.collect.Iterables;

import java.util.List;

/**
 * Created by Olukorede Aguda on 07/08/2016.
 */
public class EmailTaskQueueImpl implements TaskQueue {

    private static final String EMAIL_QUEUE = "email-queue";
    private final Queue queue;

    public EmailTaskQueueImpl() {
        this.queue = QueueFactory.getQueue(EMAIL_QUEUE);
    }

    public EmailTaskQueueImpl(Queue queue) {
        this.queue = queue;
    }

    public void add(DeferredTask payload) {
        this.add(null, payload);
    }

    public void add(Transaction txn, DeferredTask payload) {
        queue.add(txn, TaskOptions.Builder.withPayload(payload));
    }

    /** Allows any number of tasks; automatically partitions as necessary */
    public void add(Iterable<? extends DeferredTask> payloads) {
        Iterable<TaskOptions> opts = Iterables.transform(payloads, task -> TaskOptions.Builder.withPayload(task));

        Iterable<List<TaskOptions>> partitioned = Iterables.partition(opts, QueueConstants.maxTasksPerAdd());

        for (List<TaskOptions> piece: partitioned)
            queue.add(null, piece);
    }
}
