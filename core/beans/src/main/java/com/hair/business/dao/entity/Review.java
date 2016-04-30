package com.hair.business.dao.entity;

import static com.hair.business.dao.constants.EntityConstants.REVIEWS_INDEX;
import static com.hair.business.dao.constants.EntityConstants.REVIEW_TYPE;

import com.hair.business.dao.abstracts.AbstractActorEntity;

import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Mapping;

/**
 * Review.
 *
 * Created by Olukorede Aguda on 26/04/2016.
 */

@Document(indexName = REVIEWS_INDEX, type = REVIEW_TYPE)
@Mapping(mappingPath = "/es/reviews_mapping.json")
public class Review extends AbstractActorEntity {

    private String author;
    private int stars;
    private String comment;

}
