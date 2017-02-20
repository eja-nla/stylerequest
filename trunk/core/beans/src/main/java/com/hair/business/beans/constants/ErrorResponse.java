package com.hair.business.beans.constants;

import com.hair.business.beans.abstracts.AbstractBean;

/**
 * Created by olukoredeaguda on 12/02/2017.
 *
 *
 */
public class ErrorResponse extends AbstractBean {

    private static String status = "failed";
    private String message;

    public ErrorResponse(String message) {
        this.message = message;
    }
}
