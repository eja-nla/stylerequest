package com.x.business.exception;

/**
 * Created by Olukorede Aguda on 20/09/2016.
 *
 * Entity not found exception
 */
public class DuplicateEntityException extends RuntimeException {

    public DuplicateEntityException(String s) {
        super(s);
    }

    public DuplicateEntityException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public DuplicateEntityException(Throwable throwable) {
        super(throwable);
    }
}
