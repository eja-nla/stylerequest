package com.hair.business.dao.datastore.repository;


import static com.hair.business.dao.datastore.ofy.OfyService.ofy;

import com.google.appengine.repackaged.com.google.common.base.Defaults;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Result;
import com.googlecode.objectify.TxnType;
import com.googlecode.objectify.cmd.Query;
import com.googlecode.objectify.cmd.QueryKeys;
import com.hair.business.dao.datastore.abstractRepository.ObjectifyRepository;
import com.hair.business.dao.datastore.stereotype.DatastoreTransaction;
import com.x.business.utilities.Assert;

import java.util.ArrayList;
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
    public <T> Long allocateId(Class<T> clazz) {
        return ofy().factory().allocateId(clazz).getId();
    }

    @Override
    public <T> T findOne(Long id, Class<T> clazz) {

        return ofy().load().type(clazz).id(id).now();
    }

    @Override
    public <T> Map<Long, T> findMany(List<Long> ids, Class<T> clazz) {
        return ofy().load().type(clazz).ids(ids);
    }

    @Override
    public <T> List<T> findByQuery(Class<T> clazz, String condition, Object value) {
        final QueryKeys<T> keys = ofy().load().type(clazz).filter(condition, value).keys();
        final List<Long> resultKeys = new ArrayList<>();
        keys.forEach(key -> resultKeys.add(key.getId()));
        return new ArrayList<>(findMany(resultKeys, clazz).values());

    }

    @Override
    public <T> List<T> findByQuery(Class<T> clazz, List<String> conditions, List<Object> values) {
        final List<Long> resultKeys = peekByQuery(clazz, conditions, values);
        return new ArrayList<>(findMany(resultKeys, clazz).values());
    }

    public <T> Long peekOne(Long id, Class<T> clazz) {
        Key<T> key = ofy().load().type(clazz).filterKey(Key.create(clazz, id)).keys().first().now();

        if (key == null) {
            return Defaults.defaultValue(Long.TYPE);
        }
        return key.getId();
    }


    @Override
    public <T> List<Long> peekByQuery(Class<T> clazz, List<String> conditions, List<Object> values) {
        final QueryKeys<T> keys = buildQuery(clazz, conditions, values).keys();
        final List<Long> resultKeys = new ArrayList<>();
        keys.forEach(key -> resultKeys.add(key.getId()));
        return resultKeys;
    }

    @Override
    @DatastoreTransaction(TxnType.REQUIRED)
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
        Assert.notNull(entities, "entities must not be null");
        for (E entity : entities) {
            Assert.hasPermanentId(entity);
        }
        return ofy().save().entities(entities);
    }

    @Override
    public <T> void delete(T... entities) {
        ofy().delete().entities(entities);
    }

    private <T> Query<T> buildQuery(Class<T> clazz, List<String> conditions, List<Object> conditionsValues) {
        if (conditions.size() != conditionsValues.size() && (conditions.size() != 0 && conditionsValues.size() != 0)){
            throw new IllegalArgumentException("Conditions and supplied values sizes must match and must not be empty.");
        }

        Query<T> results = ofy().load().type(clazz);
        for (int i = 0; i < conditions.size(); i++) {
            results = results.filter(conditions.get(i), conditionsValues.get(i));
        }

        return results;
    }

}
