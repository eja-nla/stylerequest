package com.hair.business.services.metrics;

import static org.slf4j.LoggerFactory.getLogger;

import com.google.appengine.repackaged.com.google.common.base.Stopwatch;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;

/**
 * Created by olukoredeaguda on 01/02/2017.
 *
 * Execution time logger interceptor
 */
public class ExecTimeLoggerInterceptor implements MethodInterceptor {

    private final Logger logger = getLogger(this.getClass());
    private final Stopwatch stopwatch = Stopwatch.createStarted();

    @Override
    public Object invoke(final MethodInvocation invocation) throws Throwable {
        stopwatch.reset();
        try {
            stopwatch.start();
            final Object returnedObject = invocation.proceed();
            stopwatch.stop();
            logger.info("Method: '{}' of class: '{}' took: '{}'",
                    invocation.getMethod().getName(),
                    invocation.getMethod().getDeclaringClass().getName(),
                    stopwatch.toString()
            );
            return returnedObject;
        } finally {
            stopwatch.reset();
        }

    }
}