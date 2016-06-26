package com.hair.business.beans.abstracts;

import org.joda.time.DateTime;

/**
 * Transaction entity.
 *
 * Created by Olukorede Aguda on 25/04/2016.
 *
 */
public abstract class AbstractTransactionEntity extends AbstractPersistenceEntity {
    private long lastUpdated;
    private long created;
    private String name;

    public long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(DateTime lastUpdated) {
        this.lastUpdated = lastUpdated.getMillis();
    }

    public long getCreated() {
        return created;
    }

    public void setCreated(DateTime created) {
        this.created = created.getMillis();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
