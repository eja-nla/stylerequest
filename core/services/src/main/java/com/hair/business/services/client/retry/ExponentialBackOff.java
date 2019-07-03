package com.hair.business.services.client.retry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.SocketTimeoutException;
import java.util.Arrays;
import java.util.List;

import javax.net.ssl.SSLHandshakeException;

/**
 * Exponential back off
 *
 * Created by olukoredeaguda on 19/03/2017.
 */
public final class ExponentialBackOff {
    final Logger logger = LoggerFactory.getLogger(getClass());

    private static final int[] FIBONACCI = new int[] { 1, 1, 2, 3, 5, 8, 13 };
    private static final List<Class<? extends Exception>> EXPECTED_COMMUNICATION_ERRORS = Arrays.asList(
            SSLHandshakeException.class,
            SocketTimeoutException.class
    );

    private ExponentialBackOff() {

    }

    public static <T> T execute(ExponentialBackOffFunction<T> fn) {
        for (int attempt = 0; attempt < FIBONACCI.length; attempt++) {
            try {
                return fn.execute();
            } catch (Exception e) {
                handleFailure( attempt, e);
            }
        }
        throw new RuntimeException( "Failed to communicate." );
    }

    private static void handleFailure(int attempt, Exception e) {
        if (e.getCause() != null && !EXPECTED_COMMUNICATION_ERRORS.contains(e.getCause().getClass() )) {
            throw new RuntimeException(e);
        }
        doWait(attempt);
    }

    private static void doWait(int attempt) {
        try {
            Thread.sleep( FIBONACCI[attempt] * 1000 );
        } catch (InterruptedException e) {
            throw new RuntimeException( e );
        }
    }
}
