package com.hair.business.services.pushNotification;

import com.google.appengine.api.taskqueue.DeferredTask;

import java.util.logging.Logger;

import apns.ApnsConnection;
import apns.ApnsConnectionFactory;
import apns.ApnsConnectionPool;
import apns.CannotOpenConnectionException;
import apns.CannotUseConnectionException;
import apns.PayloadException;
import apns.PushNotification;
import apns.PushNotificationService;

/**
 * Created by Olukorede Aguda on 15/08/2016.
 *
 * APNS Push notification impl
 */
public class SendPushNotificationToApnsTask implements DeferredTask {

    private static final long serialVersionUID = 1L;

    static final Logger logger = Logger.getLogger(SendPushNotificationToApnsTask.class.getName());

    private static volatile ApnsConnectionFactory sApnsConnectionFactory;
    private static volatile ApnsConnectionPool sApnsConnectionPool;
    private static volatile PushNotificationService sPushNotificationService;
    private static final int APNS_CONNECTION_POOL_CAPACITY = 5;

    private final PushNotification mPushNotification;

    public SendPushNotificationToApnsTask(PushNotification pushNotification) {
        mPushNotification = pushNotification;
    }

    @Override
    public void run() {
        try {
            trySendingPushNotification();
        } catch (CannotOpenConnectionException e) {
            throw new RuntimeException("Could not connect to APNS", e);
        } catch (CannotUseConnectionException e) {
            throw new RuntimeException("Could not scheduleSend: " + mPushNotification, e);
        } catch (PayloadException e) {
            logger.severe("Could not scheduleSend push notification (dropping task) " + e.getMessage());
        }
    }

    private void trySendingPushNotification() throws CannotOpenConnectionException, CannotUseConnectionException, PayloadException {
        ApnsConnection apnsConnection = getApnsConnectionPool().obtain();
        if (apnsConnection == null) {
            apnsConnection = openConnection();
        }

        try {
            logger.fine("Sending push notification: {} " + mPushNotification.toString());
            getPushNotificationService().send(mPushNotification, apnsConnection);
            getApnsConnectionPool().put(apnsConnection);
        } catch (CannotUseConnectionException e) {
            logger.fine("Could not scheduleSend push notification - opening new connection");
            apnsConnection = openConnection();
            logger.fine("Retrying sending push notification");
            getPushNotificationService().send(mPushNotification, apnsConnection);
            getApnsConnectionPool().put(apnsConnection);
        }
    }

    private static ApnsConnectionPool getApnsConnectionPool() {
        if (sApnsConnectionPool == null) {
            synchronized (SendPushNotificationToApnsTask.class) {
                if (sApnsConnectionPool == null) {
                    sApnsConnectionPool = new ApnsConnectionPool(APNS_CONNECTION_POOL_CAPACITY);
                }
            }
        }
        return sApnsConnectionPool;
    }

    private static PushNotificationService getPushNotificationService() {
        if (sPushNotificationService == null) {
            synchronized (SendPushNotificationToApnsTask.class) {
                if (sPushNotificationService == null) {
                    sPushNotificationService = new PushNotificationServiceImpl();
                }
            }
        }
        return sPushNotificationService;
    }

    private static ApnsConnection openConnection() throws CannotOpenConnectionException {
        logger.fine("Connecting to APNS");
        return getApnsConnectionFactory().openPushConnection();
    }

    private static ApnsConnectionFactory getApnsConnectionFactory() {
        if (sApnsConnectionFactory == null) {
            synchronized (SendPushNotificationToApnsTask.class) {
                if (sApnsConnectionFactory == null) {
                    sApnsConnectionFactory = new ApnsConnectionFactoryImpl();
                }
            }
        }
        return sApnsConnectionFactory;
    }
}