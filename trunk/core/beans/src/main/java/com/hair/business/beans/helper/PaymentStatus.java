package com.hair.business.beans.helper;

/**
 * Created by Olukorede Aguda on 04/03/2017.
 *
 * StyleRequestPayment status
 */
public enum PaymentStatus {
    SETTLED ("Settled"),
    AUTHORIZED ("Authorized"),
    PENDING ("Pending");

    public final String name;

    PaymentStatus(String s) {
        name = s;
    }

    @Override
    public String toString(){
        return this.name;
    }
}
