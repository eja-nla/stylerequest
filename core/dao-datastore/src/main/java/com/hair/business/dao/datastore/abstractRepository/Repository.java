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
public interface Repository extends ObjectifyRepository {

    /**
     * Allocates an id
     * */
    <T> Long allocateId(Class<T>  clazz);

    /**
     * finds entity with given id
     */
    <T> T findOne(Long id, Class<T> clazz);

    /**
     * finds entities with given id
     */
    <T> Map<Long, T> findMany(List<Long> ids, Class<T> clazz);

    /**
     * returns entities matching given condition
     * */
    <T> List<T> findByQuery(Class<T> clazz, String condition, Object value);

    /**
     * returns entities matching given conditions
     * */
    <T> List<T> findByQuery(Class<T> clazz, List<String> conditions, List<Object> values);

    /**
     * peeks the entity with given id
     *
     * Returns the entity's ID if found, com.google.common.base.Defaults.defaultValue(Long.TYPE) otherwise
     */
    <T> Long peekOne(Long id, Class<T> clazz);

    /**
     *
     * Peeks data store for IDs of entities matching given conditions
     */
    <T> List<Long> peekByQuery(Class<T> clazz, List<String> conditions, List<Object> values);

    /**
     * saves a single entity, updating entity's lastUpdateTime
     * */
    <E> Key<E> saveOne(E entity);

    /**
     * Saves multiple entities, updating entity's lastUpdateTime
     * */
    <E> Result<Map<Key<E>, E>> saveMany(Collection<E> entities);

    /**
     * Array backed entity save suitable for fewer entities, updating entity's lastUpdateTime
     */
    <E> Result<Map<Key<E>, E>> saveFew(E... entities);

    /**
     * updates an entity, updating entity's lastUpdateTime
     * */
    <T> void update(T entity);

    /**
     * Deletes the given entity
     * */
    <T> void delete(T... entities);

    Object getFactory();

}
