package com.hair.business.beans.helper;

import com.googlecode.objectify.stringifier.Stringifier;

/**
 * Created by olukoredeaguda on 05/02/2017.
 *
 * Integer Stringifyer to help ofy map boxed Integer to string and back
 */
public class IntegerStringifyer implements Stringifier<Integer> {

    @Override
    public String toString(Integer obj) {
        return obj.toString();
    }

    @Override
    public Integer fromString(String str) {
        return new Integer(str);
    }
}
