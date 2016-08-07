package com.hair.business.dao.datastore.repository;


import static com.hair.business.dao.datastore.ofy.OfyService.ofy;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Result;
import com.hair.business.dao.datastore.abstractRepository.ObjectifyRepository;

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
    public <T> Collection<T> findByQuery(List<Long> ids, Class clazz, String condition, Object object) {
        return ofy().load().type(clazz).filter(condition, object).list();
    }

    @Override
    public <E> Key<E> saveOne(E entity) {
        return ofy().save().entity(entity).now();
    }

    @Override
    public <E> Result<Map<Key<E>, E>> saveMany(Collection<E> entities) {
        return ofy().save().entities(entities);
    }

    @Override
    public <E> Result<Map<Key<E>, E>> saveFew(E... entities) {
        return ofy().save().entities(entities);
    }

    @Override
    public <T> void delete(T entity) {
        ofy().delete().entity(entity);
    }

}
