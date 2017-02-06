package com.hair.business.services.metrics;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

 import java.text.DecimalFormat;
import java.util.logging.Logger;

/**
 * Created by olukoredeaguda on 01/02/2017.
 *
 * Logging interceptor
 */
public class ExecTimeLoggerInterceptor implements MethodInterceptor {

    private static final Logger log = Logger.getLogger(ExecTimeLoggerInterceptor.class.getName());
    private static final DecimalFormat decimalFormat = new DecimalFormat("#.##########");

    @Override
    public Object invoke(final MethodInvocation invocation) throws Throwable {
        //final Stopwatch stopwatch = Stopwatch.createStarted();
        long start = System.nanoTime();
        final Object returnedObject = invocation.proceed();
        long estimatedTime = (System.nanoTime() - start) / 1000000000;
        log.info("Method: " +
                invocation.getMethod().getName() +
                " of class: " + invocation.getMethod().getDeclaringClass().getName() +
                " took(s): " + decimalFormat.format(estimatedTime)
                //stopwatch.stop().toString()
        );

        return returnedObject;
    }
}