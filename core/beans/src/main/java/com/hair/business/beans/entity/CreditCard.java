package com.hair.business.beans.entity;

import com.googlecode.objectify.annotation.Id;
import com.hair.business.beans.abstracts.AbstractActorEnablerEntity;

/**
 * Created by olukoredeaguda on 09/02/2017.
 *
 *
 */
public class CreditCard extends AbstractActorEnablerEntity {

    @Id
    private Long id;

    private String type;
    private Long number;
    private int expireMonth;
    private int expireYear;
    private int cvv2;
    private String firstName;
    private String lastName;

    public CreditCard(String type, Long number, int expireMonth, int expireYear, int cvv2, String firstName, String lastName) {
        this.type = type;
        this.number = number;
        this.expireMonth = expireMonth;
        this.expireYear = expireYear;
        this.cvv2 = cvv2;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public int getExpireMonth() {
        return expireMonth;
    }

    public void setExpireMonth(int expireMonth) {
        this.expireMonth = expireMonth;
    }

    public int getExpireYear() {
        return expireYear;
    }

    public void setExpireYear(int expireYear) {
        this.expireYear = expireYear;
    }

    public int getCvv2() {
        return cvv2;
    }

    public void setCvv2(int cvv2) {
        this.cvv2 = cvv2;
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

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
