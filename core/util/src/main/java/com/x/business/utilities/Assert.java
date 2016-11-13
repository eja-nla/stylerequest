package com.x.business.utilities;

import com.x.business.exception.EntityNotFoundException;

/**
 * Created by Olukorede Aguda on 22/06/2016.
 */
public class Assert {

    public static void notNull(Object o, String m){
        if(null == o){
            throw new IllegalArgumentException(m);
        }
    }

    public static void isFound(Object entity, String message){
        if(null == entity){
            throw new EntityNotFoundException(message);
        }
    }

}
