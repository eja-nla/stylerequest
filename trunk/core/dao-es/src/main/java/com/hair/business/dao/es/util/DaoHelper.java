package com.hair.business.dao.es.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;

import javax.inject.Named;

/**
 * Created by Olukorede Aguda on 25/04/2016.
 */
@Named
public class DaoHelper {
    final Logger logger = LoggerFactory.getLogger(getClass());

    private final ElasticsearchTemplate elasticsearchTemplate;


    public DaoHelper(ElasticsearchTemplate elasticsearchTemplate) {
        this.elasticsearchTemplate = elasticsearchTemplate;
    }

    /**
     * Create index with custom mapping
     * */
    public void createIndex(Class type, String mapping) {
        elasticsearchTemplate.deleteIndex(type);
        elasticsearchTemplate.createIndex(type);
        elasticsearchTemplate.putMapping(type, mapping);
        elasticsearchTemplate.refresh(type);
    }
}
