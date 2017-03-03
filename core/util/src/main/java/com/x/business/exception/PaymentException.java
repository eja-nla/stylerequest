package com.x.business.exception;

/**
 * Created by Olukorede Aguda on 20/09/2016.
 *
 * Payment exception
 */
public class PaymentException extends RuntimeException {

    public PaymentException(String s) {
        super(s);
    }

    public PaymentException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public PaymentException(Throwable throwable) {
        super(throwable);
    }
}
