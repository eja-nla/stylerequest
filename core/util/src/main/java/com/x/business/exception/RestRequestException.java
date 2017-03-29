package com.x.business.exception;

/**
 * Created by Olukorede Aguda on 20/09/2016.
 *
 * Entity not found exception
 */
public class RestRequestException extends RuntimeException {

    public RestRequestException(String s) {
        super(s);
    }

    public RestRequestException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public RestRequestException(Throwable throwable) {
        super(throwable);
    }
}
