package com.hair.business.services.client.retry;


import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertEquals;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;

import java.net.SocketTimeoutException;

/**
 * Created by ejanla on 6/27/20.
 */
public class RetryWithExponentialBackOffTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();


    @Test
    public void testNoRetry() {
        Test1 cmd = new Test1();

        String result = RetryWithExponentialBackOff.execute(cmd::get);
        assertEquals(cmd.get(), result);
    }

    @Test
    public void testRetry() throws SocketTimeoutException {
        int[] fib = new int[] {1,1,2};
        Test1 exponentialBackupFunc = Mockito.mock(Test1.class);
        Mockito.when(exponentialBackupFunc.getThrows()).thenThrow(new SocketTimeoutException());

        exception.expect(RuntimeException.class);

        exception.expectMessage(containsString("Final Retry attempt failed"));
        RetryWithExponentialBackOff.execute(exponentialBackupFunc::getThrows, fib);
    }

    class Test1{
        public String get() {
            return "d'oh";
        }
        public String getThrows() throws SocketTimeoutException {
            return "d'oh";
        }
    }
}
