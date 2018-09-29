package com.x.business.utilities;

import com.google.appengine.repackaged.com.google.common.base.Defaults;

import com.hair.business.beans.abstracts.AbstractPersistenceEntity;

import org.apache.commons.lang3.Validate;
import org.joda.time.DateTime;

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

    public static void notNull(Object... args){
        for (int i = 0; i < args.length; i++) {
            if(null == args[i]){
                throw new IllegalArgumentException(NULL_MESSAGE);
            }
        }

    }

    public static void validId(Long key){
        notNull(key);
        if(Objects.equals(key, Defaults.defaultValue(Long.TYPE))){
            throw new IllegalArgumentException(String.format("Invalid ID value %s", key));
        }
    }

    public static void hasPermanentId(Object persistentEntity) {
        notNull(persistentEntity, "Persistent Entity must not be null");

        if (((AbstractPersistenceEntity) persistentEntity).getPermanentId() == null) {
            throw new IllegalArgumentException("Persistent Entity must have a permanentId value");
        }
    }

    public static void dateInFuture(DateTime dateTime) {
        notNull(dateTime, "Null date not allowed.");
        if (dateTime.isBeforeNow()) {
            throw new IllegalArgumentException(String.format("Date %s must be in the future.", dateTime.toString()));
        }
    }

    public static void validIds(Long... ids) {
        for (Long id : ids) {
            validId(id);
        }
    }
}
