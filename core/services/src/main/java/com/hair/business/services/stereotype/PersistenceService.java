package com.hair.business.services.stereotype;

import java.io.Serializable;

/**
 * Persistence Service.
 *
 * Created by Olukorede Aguda on 25/04/2016.
 */
public interface PersistenceService<TYPE, PRIMARYKEY extends Serializable> extends FindService<TYPE, PRIMARYKEY>  {
    TYPE save(TYPE bean);

    void delete(TYPE bean);
}
