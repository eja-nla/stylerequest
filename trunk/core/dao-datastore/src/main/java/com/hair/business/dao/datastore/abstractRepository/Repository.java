package com.hair.business.dao.datastore.abstractRepository;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Result;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Abstract Repository interface
 *
 * Underlying impl can simply be changed by making this interface extend a new Abstract{New impl}SyncRepository wrapper
 *
 * Created by Olukorede Aguda on 25/04/2016.
 */
public interface Repository {

    /**
     * Allocates an id
     * */
    Key<?> allocateId(Class clazz);

    /**
     * finds entity with given id
     */
    <T> T findOne(Long id, Class clazz);

    /**
     * finds entities with given id
     */
    <T> Map findMany(List<Long> ids, Class clazz);

    /**
     * returns entities matching given condition from the list of ids
     * */
    <T> Collection<T> findByQuery(List<Long> ids, Class clazz, String condition, Object object);

    /**
     * saves a single customer information
     * */
    <E> Key<E> saveOne(E entity);

    /**
     * Saves multiple entities
     * */
    <E> Result<Map<Key<E>, E>> saveMany(Collection<E> entities);

    /**
     * Deletes the given entity
     * */
    <T> void delete(T entity);
}
