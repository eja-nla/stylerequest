package com.x.business.utilities;

/**
 * Created by Olukorede Aguda on 22/06/2016.
 */
public class Assert {

    public static void notNull(Object o, String m){
        if(null == o){
            throw new IllegalArgumentException(m);
        }
    }
}
