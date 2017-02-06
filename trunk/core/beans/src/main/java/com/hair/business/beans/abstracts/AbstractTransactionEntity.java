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
    private String firstName;
    private String lastName;

    public DateTime getLastUpdated() {
        return lastUpdated;
    }

    protected void setLastUpdated(DateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    protected void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
