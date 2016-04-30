package com.hair.business.dao.entity;

import static com.hair.business.dao.constants.EntityConstants.IMAGES_INDEX;
import static com.hair.business.dao.constants.EntityConstants.IMAGES_TYPE;

import com.hair.business.dao.abstracts.AbstractActorEntity;

import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Mapping;

/**
 * Review.
 *
 * Created by Olukorede Aguda on 26/04/2016.
 */

@Document(indexName = IMAGES_INDEX, type = IMAGES_TYPE)
@Mapping(mappingPath = "/es/images_mapping.json")
public class Image extends AbstractActorEntity {

    private String url;

    private String owner;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
}
