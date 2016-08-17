package com.hair.business.services.pushNotification;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import apns.ApnsConnectionFactory;
import apns.MockApnsConnectionFactory;
import apns.MockPushNotificationService;
import apns.PushNotification;

/**
 * Created by Olukorede Aguda on 17/08/2016.
 */
public class PushNotificationServiceImplTest {

    MockPushNotificationService mpns = new MockPushNotificationService();
    ApnsConnectionFactory apnsConnectionFactory = new ApnsConnectionFactoryImpl(new MockApnsConnectionFactory());

    @Test
    public void send() throws Exception {
        PushNotification pn = new PushNotification();
        String deviceToken = "777d2ac490a17bb1d4c8a6ec7c50d4b1b9a36499acd45bf5fcac103cde038eff";

        pn.setDeviceTokens(deviceToken);

        mpns.send(pn, apnsConnectionFactory.openPushConnection());

        assertTrue(mpns.pushWasSentTo(deviceToken));

        PushNotification anotherNotif = mpns.getLastPushSentToDevice(deviceToken);

        assertThat(anotherNotif, is(notNullValue()));

    }

}