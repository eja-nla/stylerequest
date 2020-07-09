package com.hair.business.beans.helper;

/**
 * Created by Olukorede Aguda on 06/07/2020.
 *
 * PaymentOperation
 */
public enum PaymentOperation {
    INTENT ("INTENT"),
    CAPTURE ("CAPTURE"),
    AUTHORIZE("AUTHORIZE"),
    REFUND ("REFUND"),
    CANCEL("CANCEL"),
    PAYNOW ("AUTHORIZE AND CAPTURE");

    public final String name;

    PaymentOperation(String s) {
        name = s;
    }

    @Override
    public String toString(){
        return this.name;
    }
}
