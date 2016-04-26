package com.hair.business.services.stereotype;

import java.io.Serializable;

/**
 * Repository find interface
 *
 * Created by Olukorede Aguda on 25/04/2016.
 */
public interface FindService<T, PK extends Serializable> {
    T find(PK pk);

    Iterable<T> find();
}
