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
        return ofy().load().type(clazz).filter(condition, value).list();
    }

    @Override
    public <T> List<T> findByQuery(Class<T> clazz, List<String> conditions, List<Object> values) {
        return buildQuery(clazz, conditions, values).list();
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
