package com.hair.business.dao.es.abstractRepository;


import java.io.Serializable;


/**
 * Abstract Repository interface
 *
 * Underlying impl can simply be changed by making this interface extend a new Abstract{New impl}Repository wrapper
 *
 * Created by Olukorede Aguda on 25/04/2016.
 */
public interface AbstractRepository<T, PK extends Serializable> extends AbstractElasticsearchRepository<T, PK> {
}
