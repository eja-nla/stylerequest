package com.hair.business.dao.datastore.stereotype;

import com.googlecode.objectify.TxnType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by olukoredeaguda on 21/01/2017.
 *
 * Transaction stereotype for Ofy
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface DatastoreTransaction {
    TxnType value();
}
