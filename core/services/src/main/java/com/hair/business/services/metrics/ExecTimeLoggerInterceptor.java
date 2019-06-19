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

    private static final Logger logger = getLogger(ExecTimeLoggerInterceptor.class);
//    private static final ThreadLocal<Stopwatch> stopwatch = ThreadLocal.withInitial(Stopwatch::createStarted);

    @Override
    public Object invoke(final MethodInvocation invocation) throws Throwable {
        //fixme redo
//        Stopwatch watch = stopwatch.get();
//        if (watch.isRunning()) watch.reset();
//        watch.start();
//        final Object returnedObject = invocation.proceed(); // what if the executing method is async?
//        watch.stop();
//
//        logger.info("Method: '{}' of class: '{}' took: '{}'",
//                invocation.getMethod().getName(),
//                invocation.getMethod().getDeclaringClass().getName(),
//                watch.toString()
//        );
//
        return invocation.proceed();
    }
}