package com.hair.business.services.stereotype;

import java.io.Serializable;

/**
 * Persistence Service.
 *
 * Created by Olukorede Aguda on 25/04/2016.
 */
public interface PersistenceService<T, PK extends Serializable> extends FindService<T, PK>  {
    T save(T bean);

    void delete(T bean);
}
