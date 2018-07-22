package com.x.business.scheduler;

import com.google.appengine.api.datastore.Transaction;
import com.google.appengine.api.taskqueue.DeferredTask;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.RetryOptions;
import com.google.appengine.api.taskqueue.TaskHandle;
import com.google.appengine.api.taskqueue.TaskOptions;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * Created by Olukorede Aguda on 07/08/2016.
 */
public abstract class AbstractTaskQueue implements TaskQueue {

    Map<String, DeferredTask> tasks = new HashMap<>(50_000);
    // clear this map once the 'run' call of the task completes.

    @Override
    public void add(DeferredTask payload) {
        this.add(null, payload);
    }

    @Override
    public void add(Transaction txn, DeferredTask payload) {
        getQueue().add(txn, TaskOptions.Builder.withPayload(payload).retryOptions(RetryOptions.Builder.withTaskRetryLimit(5).minBackoffSeconds(1)));
    }

    /** Allows any number of tasks; automatically partitions as necessary */
    @Override
    public void add(Iterable<? extends DeferredTask> payloads) {
//        Iterable<TaskOptions> opts = StreamSupport.stream(payloads.spliterator(), false).map(TaskOptions.Builder::withPayload).collect(Collectors.toList());
//
//        Iterable<List<TaskOptions>> partitioned = Iterables.partition(opts, QueueConstants.maxTasksPerAdd());
//
//        partitioned.forEach(list -> getQueue().add(null, list));
        for (DeferredTask p : payloads) {
            add(null, p);
        }
    }

    @Override
    public void remove(DeferredTask payload) {
        getQueue().deleteTaskAsync(new TaskHandle(TaskOptions.Builder.withPayload(payload), getQueue().getQueueName()));
    }


    protected abstract Queue getQueue();
}
