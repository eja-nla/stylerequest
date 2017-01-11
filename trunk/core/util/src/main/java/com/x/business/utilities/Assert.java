package com.x.business.utilities;

import com.hair.business.beans.abstracts.AbstractPersistenceEntity;
import com.x.business.exception.EntityNotFoundException;

import org.apache.commons.lang3.Validate;

/**
 * Created by Olukorede Aguda on 22/06/2016.
 */
public class Assert extends Validate {

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

    public static void hasPermanentId(Object persistentEntity) {
        notNull(persistentEntity, "Persistent Entity must not be null");

        if (((AbstractPersistenceEntity) persistentEntity).getPermanentId() == null) {
            throw new IllegalArgumentException("Persistent Entity must have a permanentId value");
        }
    }

}
