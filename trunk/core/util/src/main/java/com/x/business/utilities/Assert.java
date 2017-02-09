package com.x.business.utilities;

import com.google.appengine.repackaged.com.google.common.base.Defaults;

import com.hair.business.beans.abstracts.AbstractPersistenceEntity;

import org.apache.commons.lang3.Validate;

import java.util.Objects;

/**
 * Created by Olukorede Aguda on 22/06/2016.
 *
 * Convenience Assert
 */
public class Assert extends Validate {

    private static final String NULL_MESSAGE = "Entity must not be null";

    public static void notNull(Object o, String m){
        if(null == o){
            throw new IllegalArgumentException(m);
        }
    }

    public static Object notNull(Object o){
        if(null == o){
            throw new IllegalArgumentException(NULL_MESSAGE);
        }

        return o;
    }

    public static void notNull(Object... args){
        for(Object arg : args) {
            notNull(arg);
        }
    }

    public static void isFound(Object entity, String message){
        if(null == entity){
            throw new IllegalArgumentException(message);
        }
    }

    public static void validId(Long key){
        if(Objects.equals(key, Defaults.defaultValue(Long.TYPE))){
            throw new IllegalArgumentException("Invalid ID value");
        }
    }

    public static void hasPermanentId(Object persistentEntity) {
        notNull(persistentEntity, "Persistent Entity must not be null");

        if (((AbstractPersistenceEntity) persistentEntity).getPermanentId() == null) {
            throw new IllegalArgumentException("Persistent Entity must have a permanentId value");
        }
    }

}
