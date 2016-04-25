package com.hair.business.dao.entity;


import static com.hair.business.dao.constants.EntityConstants.LOCATION_INDEX;
import static com.hair.business.dao.constants.EntityConstants.LOCATION_TYPE;

import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Mapping;

import java.util.Collection;

/**
 * Created by Olukorede Aguda on 25/04/2016.
 *
 * Represents a location of a client or a merchant
 */
@Document(indexName = LOCATION_INDEX, type = LOCATION_TYPE)
@Mapping(mappingPath = "/es/location_mapping.json")
public class Location extends AbstractActorEnablerEntity {

    private String current;
    private Collection<String> previous;

    public String getCurrent() {
        return current;
    }

    public void setCurrent(String current) {
        this.current = current;
    }

    public Collection<String> getPrevious() {
        return previous;
    }

    public void setPrevious(Collection<String> previous) {
        this.previous = previous;
    }
}

