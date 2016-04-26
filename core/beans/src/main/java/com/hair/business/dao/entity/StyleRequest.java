package com.hair.business.dao.entity;



import static com.hair.business.dao.constants.EntityConstants.*;

import com.hair.business.dao.abstracts.AbstractActorEnablerEntity;
import com.hair.business.dao.constants.StyleRequestState;

import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Mapping;

/**
 * Represents a placed style request from client to a merchant
 *
 * Created by Olukorede Aguda on 25/04/2016.
 *
 */
@Document(indexName = STYLE_REQUEST_INDEX, type = STYLE_REQUEST_TYPE)
@Mapping(mappingPath = "/es/style_request_mapping.json")
public class StyleRequest extends AbstractActorEnablerEntity {

    private Merchant merchant;

    private Customer customer;

    @Field(type = FieldType.Nested)
    private Payment payment;

    @Field(type = FieldType.Nested)
    private Location location;

    private boolean isActive;

    private StyleRequestState state;


}
