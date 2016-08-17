package com.hair.business.services.pushNotification;

import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import apns.ApnsConnection;
import apns.ApnsConnectionFactory;
import apns.ApnsException;
import apns.ApnsRuntimeException;
import apns.CannotOpenConnectionException;
import apns.DefaultApnsConnectionFactory;
import apns.keystore.KeyStoreProvider;
import apns.keystore.WrapperKeyStoreProvider;

/**
 * Created by Olukorede Aguda on 16/08/2016.
 *
 * Instantiates the DefaultApnsConnectionFactory
 */
public class ApnsConnectionFactoryImpl implements ApnsConnectionFactory {

    private static volatile ApnsConnectionFactory sApnsConnectionFactory;

    public ApnsConnectionFactoryImpl() {
        this.sApnsConnectionFactory = getApnsConnection();
    }

    public ApnsConnectionFactoryImpl(ApnsConnectionFactory sApnsConnectionFactory) {
        this.sApnsConnectionFactory = sApnsConnectionFactory;
    }

    protected ApnsConnectionFactory getApnsConnection(){
        DefaultApnsConnectionFactory.Builder builder = DefaultApnsConnectionFactory.Builder.get();
        KeyStoreProvider ksp;
        try {
            KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
            keystore.load(ClassLoader.getSystemResourceAsStream(System.getProperty("keystore.filename")), System.getProperty("keystore.password").toCharArray());
            ksp = new WrapperKeyStoreProvider(keystore, System.getProperty("keystore.password").toCharArray());

            builder.setSandboxKeyStoreProvider(ksp);

            sApnsConnectionFactory = builder.build();

        } catch (KeyStoreException e) {
            throw new ApnsRuntimeException("Could not get Keystore instance while initializing APNS connection factory", e);
        } catch (CertificateException e) {
            throw new ApnsRuntimeException("Could not get Keystore instance while initializing APNS connection factory", e);
        } catch (NoSuchAlgorithmException e) {
            throw new ApnsRuntimeException("Could not get Keystore instance while initializing APNS connection factory", e);
        } catch (IOException e) {
            throw new ApnsRuntimeException("Could not get Keystore instance while initializing APNS connection factory", e);
        }
        catch (ApnsException e) {
            throw new ApnsRuntimeException("Could not create APNS connection factory", e);
        }

        return sApnsConnectionFactory;
    }

    @Override
    public ApnsConnection openPushConnection() throws CannotOpenConnectionException {
        return sApnsConnectionFactory.openPushConnection();
    }

    @Override
    public ApnsConnection openFeedbackConnection() throws CannotOpenConnectionException {
        return sApnsConnectionFactory.openFeedbackConnection();
    }
}
