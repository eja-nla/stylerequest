package com.hair.business.beans.constants;

/**
 * Created by Olukorede Aguda on 25/04/2016.
 */
public enum MerchantType {
    PAYPAL ("Paypal"),
    CARD ("Credit/Debit card"),
    APPLEPAY ("Apple Pay");

    public final String name;

    MerchantType(String s) {
        name = s;
    }

    @Override
    public String toString(){
        return this.name;
    }
}
