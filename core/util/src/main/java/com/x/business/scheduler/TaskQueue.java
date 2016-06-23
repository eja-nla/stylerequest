package com.x.business.scheduler;

import com.google.appengine.api.datastore.Transaction;
import com.google.appengine.api.taskqueue.DeferredTask;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueConstants;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.google.common.base.Function;
import com.google.common.collect.Iterables;

import com.x.business.tasks.EmailTask;

import java.io.InputStream;
import java.util.List;

/**
 * Task queue impl of deferred tasks
 *
 * Created by Olukorede Aguda on 21/06/2016.
 */
public class TaskQueue {
    // look in https://cloud.google.com/appengine/docs/java/taskqueue/

    private static final String CUSTOMER_QUEUE = "customer-email-queue";
    private static final String MERCHANT_QUEUE = "email-queue";
    private static final String STYLEREQUEST_QUEUE = "stylerequest-queue";

    public static class QueueHelper {
        private final Queue queue;

        public QueueHelper(Queue queue) {
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
            Iterable<TaskOptions> opts = Iterables.transform(payloads, new Function<DeferredTask, TaskOptions>() {
                @Override
                public TaskOptions apply(DeferredTask task) {
                    return TaskOptions.Builder.withPayload(task);
                }
            });

            Iterable<List<TaskOptions>> partitioned = Iterables.partition(opts, QueueConstants.maxTasksPerAdd());

            for (List<TaskOptions> piece: partitioned)
                queue.add(null, piece);
        }
    }

    public void scheduleEmail(String from, String to, String cc, String bcc, String subject, InputStream attachment, String message, String filename, String contentType){
        EmailTask emailTask = new EmailTask(from, to, cc, bcc, subject, attachment, message, filename, contentType);

        customerQueue().add(emailTask);

    }

    public void schedulePushNotification(){}

    public static QueueHelper customerQueue() {
        return new QueueHelper(QueueFactory.getQueue(CUSTOMER_QUEUE));
    }

    public static QueueHelper emailQueue() {
        return new QueueHelper(QueueFactory.getQueue(MERCHANT_QUEUE));
    }

    public static QueueHelper styleRequestQueue() {
        return new QueueHelper(QueueFactory.getQueue(STYLEREQUEST_QUEUE));
    }
}
