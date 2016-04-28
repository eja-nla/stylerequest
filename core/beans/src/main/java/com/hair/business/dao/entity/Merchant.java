package com.hair.business.dao.entity;



import static com.hair.business.dao.constants.EntityConstants.*;

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
@Document(indexName = MERCHANT_INDEX, type = MERCHANT_TYPE)
@Mapping(mappingPath = "/es/merchant_mapping.json")
public class Merchant extends AbstractActorEntity {

    @Field(type = FieldType.Nested)
    private Location location;

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
