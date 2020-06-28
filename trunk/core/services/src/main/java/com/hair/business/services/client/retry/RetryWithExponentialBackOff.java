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
public final class RetryWithExponentialBackOff {
    static final Logger logger = LoggerFactory.getLogger(RetryWithExponentialBackOff.class);

    private static final int[] FIBONACCI = new int[] { 1, 1, 2, 3, 5, 8, 13 };

    // All exceptions that require retry must be pre-registered.
    private static final List<Class<? extends Exception>> RETRY_WORTHY_EXCEPTIONS = Arrays.asList(
            SSLHandshakeException.class,
            SocketTimeoutException.class
    );

    private RetryWithExponentialBackOff() {}


    public static <T> T execute(ExponentialBackOffFunction<T> fn) {
        return execute(fn, FIBONACCI);
    }

    public static <T> T execute(ExponentialBackOffFunction<T> fn, int[] fib) {
        for (int attempt = 0; attempt < fib.length; attempt++) {
            try {
                return fn.execute();
            } catch (Exception e) {
                handleFailure( attempt, e);
            }
        }
        throw new RuntimeException("Final Retry attempt failed");
    }


    private static void handleFailure(int attempt, Exception e) {
        if (e.getCause() != null && !RETRY_WORTHY_EXCEPTIONS.contains(e.getCause().getClass() )) {
            throw new RuntimeException(e);
        }
        logger.info("Retrying attempt {} due to exception message {}", attempt, e.getMessage());
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
