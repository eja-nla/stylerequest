package com.hair.business.dao.es.abstractRepository;

import com.hair.business.dao.es.repository.StyleRequestRepository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Created by olukoredeaguda on 25/04/2016.
 */
public interface AbstractRepository extends ElasticsearchRepository<StyleRequestRepository, String> {
}
