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

    private static final Logger logger = getLogger(ExecTimeLoggerInterceptor.class);
    private static final Stopwatch stopwatch = Stopwatch.createStarted();

    @Override
    public Object invoke(final MethodInvocation invocation) throws Throwable {
        try {
            stopwatch.start();
            final Object returnedObject = invocation.proceed(); // what if the executing method is async?
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