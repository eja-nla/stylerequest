package com.hair.business.rest;

/**
 * MVC shared constants.
 *
 * Created by Olukorede Aguda on 30/04/2016.
 */
public class MvcConstants {

    public static final String  REQUEST_CONTROLLER_BASE = "/api/v1";

    public static final String CUSTOMER_URI="/customer";

    public static final String IMAGE_URI="/images";

    public static final String MERCHANT_URI="/merchant";

    public static final String INFO="/info";

    private static final String CREATE="/create";
    public static final String UPDATE = "/update";
    public static final String STYLE_URI="/style";
    private static final String FIND_URI="/find";
    public static final String STYLE_REQUEST_URI="/stylerequest";
    public static final String PAYMENT_URI = "/payment";

    public static final String CREATE_MERCHANT_ENDPOINT = CREATE + MERCHANT_URI;
    public static final String CREATE_CUSTOMER_ENDPOINT = CREATE + CUSTOMER_URI;
    public static final String PUBLISH_STYLE_ENDPOINT = CREATE + STYLE_URI;
    public static final String UPDATE_PAYMENT_PATH = UPDATE + PAYMENT_URI;

    private static final String PENDING_URI = "/pending";
    private static final String ACCEPTED_URI = "/accepted";
    private static final String CANCELLED_URI = "/cancelled";
    private static final String COMPLETED_URI = "/completed";
    public static final String FIND_MERCHANT_PENDING_STYLEREQUEST_ENDPOINT = FIND_URI + MERCHANT_URI + PENDING_URI;
    public static final String FIND_MERCHANT_ACCEPTED_STYLEREQUEST_ENDPOINT = FIND_URI + MERCHANT_URI + ACCEPTED_URI;
    public static final String FIND_MERCHANT_CANCELLED_STYLEREQUEST_ENDPOINT = FIND_URI + MERCHANT_URI + CANCELLED_URI;
    public static final String FIND_MERCHANT_COMPLETED_STYLEREQUEST_ENDPOINT = FIND_URI + MERCHANT_URI + COMPLETED_URI;

    public static final String FIND_CUSTOMER_PENDING_STYLEREQUEST_ENDPOINT = FIND_URI + CUSTOMER_URI + PENDING_URI;
    public static final String FIND_CUSTOMER_ACCEPTED_STYLEREQUEST_ENDPOINT = FIND_URI + CUSTOMER_URI + ACCEPTED_URI;
    public static final String FIND_CUSTOMER_CANCELLED_STYLEREQUEST_ENDPOINT = FIND_URI + CUSTOMER_URI + CANCELLED_URI;
    public static final String FIND_CUSTOMER_COMPLETED_STYLEREQUEST_ENDPOINT = FIND_URI + CUSTOMER_URI + COMPLETED_URI;



    public static final String PAYPAL_URI_BASE = "/paypal";

    public static final String STYLE_REQUEST_PATH = "/request" + STYLE_URI;

    public static final String ID ="id";

    public static final String EMAIL="email";

    public static final String ADMIN_URI="admin";

}
