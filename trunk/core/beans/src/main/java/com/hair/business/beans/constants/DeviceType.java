package com.hair.business.beans.constants;

/**
 * Created by Olukorede Aguda on 25/04/2016.
 */
public enum DeviceType {
    IOS ("ios"),
    ANDROID ("android"),
    SYMBIAN ("symbian"),
    OTHERS ("others"),
    BLACKBERRY ("blackberry");

    public final String name;

    DeviceType(String s) {
        name = s;
    }

    @Override
    public String toString(){
        return this.name;
    }
}
