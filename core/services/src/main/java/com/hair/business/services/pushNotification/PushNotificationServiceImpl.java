package com.hair.business.services.pushNotification;

import apns.ApnsConnection;
import apns.CannotUseConnectionException;
import apns.DefaultPushNotificationService;
import apns.PayloadException;
import apns.PushNotification;
import apns.PushNotificationService;

/**
 * Created by Olukorede Aguda on 17/08/2016.
 */
public class PushNotificationServiceImpl implements PushNotificationService {

    PushNotificationService pushNotificationService;

    public PushNotificationServiceImpl() {
        pushNotificationService = new DefaultPushNotificationService();
    }

    public PushNotificationServiceImpl(PushNotificationService pushNotificationService) {
        this.pushNotificationService = pushNotificationService;
    }

    @Override
    public void send(PushNotification pushNotification, ApnsConnection connection) throws CannotUseConnectionException, PayloadException {
        pushNotificationService.send(pushNotification, connection);
    }
}
