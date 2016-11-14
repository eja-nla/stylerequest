package com.x.business.scheduler;

import com.google.appengine.api.datastore.Transaction;
import com.google.appengine.api.taskqueue.DeferredTask;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueConstants;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.google.common.collect.Iterables;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Created by Olukorede Aguda on 07/08/2016.
 */
public class AbstractTaskQueue implements TaskQueue {

    private final Queue queue = QueueFactory.getQueue("email-queue");

    @Override
    public void add(DeferredTask payload) {
        this.add(null, payload);
    }

    @Override
    public void add(Transaction txn, DeferredTask payload) {
        queue.add(txn, TaskOptions.Builder.withPayload(payload));
    }

    /** Allows any number of tasks; automatically partitions as necessary */
    @Override
    public void add(Iterable<? extends DeferredTask> payloads) {
        Iterable<TaskOptions> opts = StreamSupport.stream(payloads.spliterator(), false).map(TaskOptions.Builder::withPayload).collect(Collectors.toList());

        Iterable<List<TaskOptions>> partitioned = Iterables.partition(opts, QueueConstants.maxTasksPerAdd());

        for (List<TaskOptions> piece: partitioned)
            queue.add(null, piece);
    }
}