package com.hair.business.dao.datastore.repository;


import static com.hair.business.dao.datastore.ofy.OfyService.ofy;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Result;
import com.googlecode.objectify.cmd.Query;
import com.hair.business.dao.datastore.abstractRepository.ObjectifyRepository;
import com.x.business.utilities.Assert;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Objectify Datastore repository impl.
 *
 * Created by Olukorede Aguda on 25/04/2016.
 */
public class ObjectifyDatastoreRepositoryImpl implements ObjectifyRepository {

    @Override
    public Long allocateId(Class clazz) {
        return ofy().factory().allocateId(clazz).getId();
    }

    @Override
    public <T> T findOne(Long id, Class clazz) {

        return (T) ofy().load().type(clazz).id(id).now();
    }

    @Override
    public <T> Map<Long, T> findMany(List<Long> ids, Class clazz) {
        return ofy().load().type(clazz).ids(ids);
    }

    @Override
    public <T> List<T> findByQuery(Class<T> clazz, String condition, Object value) {
        return ofy().load().type(clazz).filter(condition, value).list();
    }

    public <T> List<T> findByQuery(Class<T> clazz, String keyCondition, Object keyValue, String condition, Object conditionValue) {
        return ofy().load().type(clazz)
                .filterKey(keyCondition, keyValue)
                .filter(condition, conditionValue)
                .list();
    }

    @Override
    public <T> List<T> findByQuery(Class clazz, List<String> conditions, List<Object> values) {
        if (conditions.size() != values.size() && (conditions.size() != 0 && values.size() != 0)){
            throw new IllegalArgumentException("Conditions and supplied values sizes must match and must not be empty.");
        }

        Query<T> results = ofy().load().type(clazz);
        for (int i = 0; i < conditions.size(); i++) {
            results = results.filter(conditions.get(i), values.get(i));
        }

        return results.list();
    }

    @Override
    public <E> Key<E> saveOne(E entity) {
        Assert.hasPermanentId(entity);
        return ofy().save().entity(entity).now();
    }

    @Override
    public <E> Result<Map<Key<E>, E>> saveMany(Collection<E> entities) {
        entities.forEach(Assert::hasPermanentId);
        return ofy().save().entities(entities);
    }

    @Override
    public <E> Result<Map<Key<E>, E>> saveFew(E... entities) {
        for (int i = 0; i < entities.length; i++) {
            Assert.hasPermanentId(entities[i]);
        }
        return ofy().save().entities(entities);
    }

    @Override
    public <T> void delete(T entity) {
        ofy().delete().entity(entity);
    }

}
