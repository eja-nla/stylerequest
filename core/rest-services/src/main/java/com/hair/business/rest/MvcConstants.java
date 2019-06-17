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
    public static final String ADMIN_URI = "/admin";
    public static final String PAYMENT_URI = ADMIN_URI + "/payment";

    public static final String CREATE_MERCHANT_ENDPOINT = CREATE + MERCHANT_URI;
    public static final String CREATE_CUSTOMER_ENDPOINT = CREATE + CUSTOMER_URI;
    public static final String UPDATE_CUSTOMER_ENDPOINT = UPDATE + CUSTOMER_URI;
    public static final String PUBLISH_STYLE_ENDPOINT = CREATE + STYLE_URI;
    public static final String UPDATE_PAYMENT_PATH = CUSTOMER_URI + UPDATE + "/payment";
    public static final String UPDATE_PREFERENCES_PATH = UPDATE + "/preferences";
    public static final String GET_APP_INFO_PATH = "/info/endpoints";

    private static final String PENDING_URI = "/pending";
    private static final String ACCEPTED_URI = "/accepted";
    private static final String CANCELLED_URI = "/cancelled";
    private static final String COMPLETED_URI = "/completed";


    ///accept/stylerequest
    public static final String ACCEPT_REQUEST_ENDPOINT = "/accept" + STYLE_REQUEST_URI;
    public static final String CANCEL_REQUEST_ENDPOINT = "/cancel" + STYLE_REQUEST_URI;
    public static final String COMPLETE_REQUEST_ENDPOINT = "/complete" + STYLE_REQUEST_URI;

    public static final String FIND_MERCHANT_PENDING_STYLEREQUEST_ENDPOINT = FIND_URI + MERCHANT_URI + PENDING_URI;
    public static final String FIND_MERCHANT_ACCEPTED_STYLEREQUEST_ENDPOINT = FIND_URI + MERCHANT_URI + ACCEPTED_URI;
    public static final String FIND_MERCHANT_CANCELLED_STYLEREQUEST_ENDPOINT = FIND_URI + MERCHANT_URI + CANCELLED_URI;
    public static final String FIND_MERCHANT_COMPLETED_STYLEREQUEST_ENDPOINT = FIND_URI + MERCHANT_URI + COMPLETED_URI;

    public static final String FIND_CUSTOMER_PENDING_STYLEREQUEST_ENDPOINT = FIND_URI + CUSTOMER_URI + PENDING_URI;
    public static final String FIND_CUSTOMER_ACCEPTED_STYLEREQUEST_ENDPOINT = FIND_URI + CUSTOMER_URI + ACCEPTED_URI;
    public static final String FIND_CUSTOMER_CANCELLED_STYLEREQUEST_ENDPOINT = FIND_URI + CUSTOMER_URI + CANCELLED_URI;
    public static final String FIND_CUSTOMER_COMPLETED_STYLEREQUEST_ENDPOINT = FIND_URI + CUSTOMER_URI + COMPLETED_URI;



    private static final String BRAINTREE_URI_BASE = "/braintree";
    public static final String BRAINTREE_AUTHORIZE_URI_ENDPOINT = BRAINTREE_URI_BASE + "/authorize";
    public static final String BRAINTREE_CAPTURE_URI_ENDPOINT = BRAINTREE_URI_BASE + "/capture";
    public static final String BRAINTREE_TOKEN_URI_ENDPOINT = BRAINTREE_URI_BASE + "/token/issue";
    public static final String BRAINTREE_REFUND_SR_URI_ENDPOINT = BRAINTREE_URI_BASE + "/refund/stylerequest";
    public static final String BRAINTREE_REFUND_TX_URI_ENDPOINT = BRAINTREE_URI_BASE + "/refund/transaction";

    public static final String STYLE_REQUEST_PATH = "/request" + STYLE_URI;

    public static final String STYLE_SEARCH_PATH = FIND_URI + "/zipcodes/";

    public static final String ID ="id";

    public static final String EMAIL="email";

}
