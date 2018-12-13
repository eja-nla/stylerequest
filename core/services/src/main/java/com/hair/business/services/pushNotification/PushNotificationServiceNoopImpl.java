package com.hair.business.services.pushNotification;

import apns.ApnsConnection;
import apns.PushNotification;

/**
 * Created by Olukorede Aguda on 17/08/2016.
 *
 * Push AbstractEmailNotification Service implementation.
 */
public class PushNotificationServiceNoopImpl implements PushNotificationServiceInternal {

    public PushNotificationServiceNoopImpl() {
    }

    @Override
    public void send(PushNotification pushNotification, ApnsConnection connection) {
    }

    @Override
    public void scheduleSend(String deviceId, String alert) {
    }
}
