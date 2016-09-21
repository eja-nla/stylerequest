package com.hair.business.beans.abstracts;

import org.joda.time.DateTime;

/**
 * Transaction entity.
 *
 * Created by Olukorede Aguda on 25/04/2016.
 *
 */
public abstract class AbstractTransactionEntity extends AbstractPersistenceEntity {
    private DateTime lastUpdated;
    private String name;

    public DateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(DateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
