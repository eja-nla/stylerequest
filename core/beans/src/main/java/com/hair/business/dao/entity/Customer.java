package com.hair.business.dao.entity;


import static com.hair.business.dao.constants.EntityConstants.CUSTOMER_INDEX;
import static com.hair.business.dao.constants.EntityConstants.CUSTOMER_TYPE;
import static com.hair.business.dao.constants.EntityConstants.STYLE_REQUEST_INDEX;
import static com.hair.business.dao.constants.EntityConstants.STYLE_REQUEST_TYPE;

import com.hair.business.dao.constants.StyleRequestState;

import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Mapping;

/**
 * Created by olukoredeaguda on 25/04/2016.
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

}
