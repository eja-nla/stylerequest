package com.hair.business.services.pushNotification;

import apns.PushNotificationService;

/**
 * Created by olukoredeaguda on 07/10/2018.
 */
public interface PushNotificationServiceInternal extends PushNotificationService {

    /**
     * @param deviceId user's device ID
     * @param alert to scheduleSend
     * User only ever needs one of alert, badge, sound, or silent
     * See https://developer.apple.com/documentation/usernotifications/setting_up_a_remote_notification_server/generating_a_remote_notification#Payload Key Reference
     * */
    void scheduleSend(String deviceId, String alert);
}
