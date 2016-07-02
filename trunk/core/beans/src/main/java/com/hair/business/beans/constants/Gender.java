package com.hair.business.beans.constants;

/**
 * Created by Olukorede Aguda on 28/06/2016.
 */
public enum Gender {
    M ("Male"),
    F ("Female"),
    NONE ("None");

    public final String name;

    Gender(String s) {
        name = s;
    }

    @Override
    public String toString(){
        return this.name;
    }
}
