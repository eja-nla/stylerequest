package com.hair.business.services.metrics;

import static org.slf4j.LoggerFactory.getLogger;

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

    @Override
    public Object invoke(final MethodInvocation invocation) throws Throwable {
        //final Stopwatch stopwatch = Stopwatch.createStarted();
        long start = System.nanoTime();
        final Object returnedObject = invocation.proceed();
        long estimatedTime = (System.nanoTime() - start) / 1000000000;
        logger.info("Method: " +
                invocation.getMethod().getName() +
                " of class: " + invocation.getMethod().getDeclaringClass().getName() +
                " took(s): " + estimatedTime
                //stopwatch.stop().toString()
        );

        return returnedObject;
    }
}