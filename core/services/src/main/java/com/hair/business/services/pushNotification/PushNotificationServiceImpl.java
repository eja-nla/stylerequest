package com.hair.business.services.pushNotification;

import com.x.business.scheduler.TaskQueue;
import com.x.business.scheduler.stereotype.ApnsTaskQueue;

import java.net.Socket;

import javax.inject.Inject;

import apns.ApnsConnection;
import apns.CannotUseConnectionException;
import apns.DefaultApnsConnection;
import apns.DefaultPushNotificationService;
import apns.PayloadException;
import apns.PushNotification;
import apns.PushNotificationService;

/**
 * Created by Olukorede Aguda on 17/08/2016.
 *
 * Push AbstractEmailNotification Service implementation.
 */
public class PushNotificationServiceImpl implements PushNotificationServiceInternal {

    PushNotificationService pushNotificationService;
    private static final ApnsConnection apnsConnection = new DefaultApnsConnection(new Socket());
    private final TaskQueue apnsQueue;

    @Inject
    public PushNotificationServiceImpl(@ApnsTaskQueue TaskQueue apnsQueue) {
        pushNotificationService = new DefaultPushNotificationService();
        this.apnsQueue = apnsQueue;
    }

    public PushNotificationServiceImpl() {
        pushNotificationService = new DefaultPushNotificationService();
        apnsQueue = null;
    }

    @Override
    public void send(PushNotification pushNotification, ApnsConnection connection) throws CannotUseConnectionException, PayloadException {
        pushNotificationService.send(pushNotification, apnsConnection);
    }

    @Override
    public void scheduleSend(String deviceId, String alert) {
        PushNotification pushNotification = new PushNotification() // we should pool these and reuse. Not worth incurring allocation as volume increases
                .setAlert(alert)
                .setDeviceTokens(deviceId); // Nullable?
        apnsQueue.add(new SendPushNotificationToApnsTask(pushNotification));
    }
}
