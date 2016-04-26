package com.hair.business.dao.es.abstractRepository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.io.Serializable;

/**
 * Abstract Elasticsearch repository wrapper
 *
 * Created by Olukorede Aguda on 25/04/2016.
 */
public interface AbstractElasticsearchRepository<T, PK extends Serializable> extends ElasticsearchRepository<T, PK> {
}
