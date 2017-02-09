package com.hair.business.beans.constants;

/**
 * Created by Olukorede Aguda on 25/04/2016.
 *
 * Payment type
 */
public enum PaymentType {
    PAYPAL ("Paypal"),
    CARD ("Credit/Debit card"),
    CASH ("Cash");

    public final String name;

    PaymentType(String s) {
        name = s;
    }

    @Override
    public String toString(){
        return this.name;
    }
}
