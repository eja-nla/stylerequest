package com.hair.business.dao.constants;

/**
 * Created by olukoredeaguda on 25/04/2016.
 */
public enum MerchantType {
    PAYPAL ("Paypal"),
    CARD ("Credit/Debit card"),
    APPLEPAY ("apple Pay");

    public final String name;

    MerchantType(String s) {
        name = s;
    }

    @Override
    public String toString(){
        return this.name;
    }
}
