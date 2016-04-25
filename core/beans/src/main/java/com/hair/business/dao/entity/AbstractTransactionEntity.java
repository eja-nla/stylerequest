package com.hair.business.dao.entity;

import org.joda.time.DateTime;
import org.springframework.data.domain.Auditable;

/**
 * Transaction entity.
 * Created by olukoredeaguda on 25/04/2016.
 *
 */
public abstract class AbstractTransactionEntity extends AbstractPersistentEntity {
    private DateTime lastUpdated;
    private DateTime created;
    private String name;


    public DateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(DateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public DateTime getCreated() {
        return created;
    }

    public void setCreated(DateTime created) {
        this.created = created;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
