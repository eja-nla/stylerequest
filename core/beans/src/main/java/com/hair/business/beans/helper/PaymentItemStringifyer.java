package com.hair.business.beans.helper;

import com.braintreegateway.PaymentMethod;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.googlecode.objectify.stringifier.Stringifier;

import java.io.IOException;

/**
 * Created by olukoredeaguda on 05/02/2017.
 *
 * Integer Stringifyer to help ofy map boxed Integer to string and back
 */
public class PaymentItemStringifyer implements Stringifier<PaymentMethod> {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String toString(PaymentMethod obj) {
        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return obj.toString();
    }

    @Override
    public PaymentMethod fromString(String str) {
        try {
            return mapper.readValue(str, PaymentMethod.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
