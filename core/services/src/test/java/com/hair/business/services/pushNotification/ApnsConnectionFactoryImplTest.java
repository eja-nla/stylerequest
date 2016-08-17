package com.hair.business.services.pushNotification;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isA;
import static org.hamcrest.Matchers.notNullValue;

import org.junit.Test;

import apns.ApnsConnection;
import apns.ApnsConnectionFactory;
import apns.MockApnsConnectionFactory;

/**
 * Created by Olukorede Aguda on 17/08/2016.
 */
public class ApnsConnectionFactoryImplTest {

    final ApnsConnectionFactory apnsConnection = new MockApnsConnectionFactory();

    @Test
    public void getApnsConnection() throws Exception {
        // intentionally using ApnsConnectionFactoryImpl instance instead of
        // MockApnsConnectionFactory to allow test coverage of ApnsConnectionFactoryImpl().getApnsConnection()
        System.setProperty("keystore.password", "you were really looking for a password weren't you? :P");
        System.setProperty("keystore.filename", "test.p12");
        assertThat(new ApnsConnectionFactoryImpl().getApnsConnection(), is(notNullValue()));
    }

    @Test
    public void openPushConnection() throws Exception {
        assertThat(apnsConnection.openPushConnection(), isA(ApnsConnection.class));
    }

    @Test
    public void openFeedbackConnection() throws Exception {
        assertThat(apnsConnection.openFeedbackConnection(), isA(ApnsConnection.class));
    }

}