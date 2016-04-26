package com.hair.business.services;

import com.hair.business.dao.es.abstractRepository.AbstractRepository;
import com.hair.business.services.stereotype.FindService;

import java.io.Serializable;

/**
 * Abstract Persistence service
 *
 * Created by Olukorede Aguda on 26/04/2016.
 */
public abstract class AbstractFindService<R extends AbstractRepository<T, PK>, T, PK extends Serializable> implements FindService<T, PK> {

    protected final R repository;

    public AbstractFindService(R repository) {
        this.repository = repository;
    }

    public T find(PK pk){
        return repository.findOne(pk);
    }

    public Iterable<T> find(){
        return repository.findAll();
    }
}
