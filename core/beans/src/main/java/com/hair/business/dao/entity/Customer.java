package com.hair.business.dao.entity;


import static com.hair.business.dao.constants.EntityConstants.CUSTOMER_INDEX;
import static com.hair.business.dao.constants.EntityConstants.CUSTOMER_TYPE;

import com.hair.business.dao.abstracts.AbstractActorEntity;

import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Mapping;

/**
 * Created by Olukorede Aguda on 25/04/2016.
 *
 * Represents a placed style request from client to a merchant
 */
@Document(indexName = CUSTOMER_INDEX, type = CUSTOMER_TYPE)
@Mapping(mappingPath = "/es/customer_mapping.json")
public class Customer extends AbstractActorEntity {

    @Field(type = FieldType.Nested)
    private Payment payment;

    @Field(type = FieldType.Nested)
    private Location location;

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
