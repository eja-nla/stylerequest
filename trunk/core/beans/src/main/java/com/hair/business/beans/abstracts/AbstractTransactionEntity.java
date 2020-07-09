package com.hair.business.beans.abstracts;

/**
 * TransactionResult entity.
 *
 * Created by Olukorede Aguda on 25/04/2016.
 *
 */
public abstract class AbstractTransactionEntity extends AbstractPersistenceEntity {

    AbstractTransactionEntity(){}

    private String firstName;
    private String lastName;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
