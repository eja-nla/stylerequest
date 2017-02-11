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

    public static final String CREATE="/create";
    public static final String UPDATE = "/update";
    public static final String STYLE_URI="/style";
    public static final String PAYMENT_URI = "/payment";

    public static final String CREATE_MERCHANT_ENDPOINT = CREATE + MERCHANT_URI;
    public static final String CREATE_CUSTOMER_ENDPOINT = CREATE + CUSTOMER_URI;
    public static final String PUBLISH_STYLE_ENDPOINT = CREATE + STYLE_URI;
    public static final String UPDATE_PAYMENT_PATH = UPDATE + PAYMENT_URI;


    public static final String PAYPAL_URI_BASE = "/paypal";

    public static final String STYLE_REQUEST_PATH = "/request" + STYLE_URI;

    public static final String ID ="id";

    public static final String EMAIL="email";

    public static final String ADMIN_URI="admin";

}
