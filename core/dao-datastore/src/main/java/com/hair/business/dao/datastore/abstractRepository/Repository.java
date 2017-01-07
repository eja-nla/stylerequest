package com.hair.business.dao.datastore.abstractRepository;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Result;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Abstract Repository interface
 *
 * Underlying impl can simply be changed by making this interface
 * extend a new Abstract{New impl}SyncRepository wrapper
 *
 * Created by Olukorede Aguda on 25/04/2016.
 */
public interface Repository {

    /**
     * Allocates an id
     * */
    Long allocateId(Class clazz);

    /**
     * finds entity with given id
     */
    <T> T findOne(Long id, Class clazz);

    /**
     * finds entities with given id
     */
    <T> Map findMany(List<Long> ids, Class clazz);

    /**
     * returns entities matching given condition
     * */
    <T> List<T> findByQuery(Class<T> clazz, String condition, Object value);

    /**
     * returns entities matching given condition for given Id
     * */
    <T> List<T> findByQuery(Class<T> clazz, String keyCondition, Long keyValue, String condition, Object conditionValue);

    /**
     * returns entities matching given conditions
     * */
    <T> List<T> findByQuery(Class clazz, List<String> conditions, List<Object> values);

    /**
     * saves a single customer information
     * */
    <E> Key<E> saveOne(E entity);

    /**
     * Saves multiple entities
     * */
    <E> Result<Map<Key<E>, E>> saveMany(Collection<E> entities);

    /**
     * Array backed entity save suitable for fewer entities
     */
    <E> Result<Map<Key<E>, E>> saveFew(E... entities);

    /**
     * Deletes the given entity
     * */
    <T> void delete(T... entities);
}
