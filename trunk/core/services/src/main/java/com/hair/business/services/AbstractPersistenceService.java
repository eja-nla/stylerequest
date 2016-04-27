package com.hair.business.services;

import com.hair.business.dao.abstracts.AbstractPersistenceEntity;
import com.hair.business.dao.constants.EntityConstants;
import com.hair.business.dao.es.abstractRepository.AbstractRepository;
import com.hair.business.services.stereotype.PersistenceService;

import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.io.Serializable;

/**
 * Abstract Persistence service
 *
 * Created by Olukorede Aguda on 26/04/2016.
 */
public abstract class AbstractPersistenceService<R extends AbstractRepository<T, PK>, T extends AbstractPersistenceEntity, PK extends Serializable> extends AbstractFindService<R, T, PK> implements PersistenceService<T, PK> {

    public AbstractPersistenceService(R repository) {
        super(repository);
    }

    public final T save(T bean){
        Assert.notNull(bean, "Bean is null.");

        if(StringUtils.isEmpty(bean.getId())){
            return doCreate(bean);
        } else {
            return doUpdate(bean);
        }
    }

    protected T doCreate(T bean){
        Assert.notNull(bean.getId(), "Primary key should not be null.");
        Assert.isTrue(EntityConstants.VERSION_DEFAULT == bean.getVersion());

        return doSave(bean);
    }

    protected T doUpdate(T bean){
        Assert.notNull(bean.getId(), "Missing primary key id.");

        // although ES gives this OOTB, sticky versioning regardless of persistence impl
        bean.setVersion(bean.getVersion() + 1);

        return doSave(bean);
    }

    protected T doSave(T bean){

        return repository.save(bean);
    }

    public void delete(T bean){
        repository.delete(bean);
    }
}
