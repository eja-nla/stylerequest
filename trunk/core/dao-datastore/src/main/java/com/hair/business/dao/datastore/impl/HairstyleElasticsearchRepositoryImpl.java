package com.hair.business.dao.datastore.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hair.business.beans.entity.Style;
import com.hair.business.dao.datastore.repository.AbstractElasticsearchRepository;

import org.elasticsearch.client.RestClient;
import org.joda.time.DateTime;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;

/**
 * Created by ejanla on 6/24/19.
 */
@Named
public class HairstyleElasticsearchRepositoryImpl extends AbstractElasticsearchRepository<Style> {

    private static final DateTime dateTime = DateTime.now();
    private static final String styleIndexName = "hairstyles" + "_" + dateTime.getMonthOfYear() + "_" + dateTime.getYear();

    @Inject
    public HairstyleElasticsearchRepositoryImpl(Provider<RestClient> clientProvider, Provider<ObjectMapper> objectMapperProvider) {
        super(clientProvider, objectMapperProvider);

        verifyIndex();
    }

    @Override
    protected String getIndex() {
        return styleIndexName;
    }

    @Override
    protected String getType() {
        return "hairstyles";
    }

    @Override
    protected String getAlias() {
        return "active_hairstyles";
    }

    @Override
    protected String getMapping(){
        return "{ \"settings\" : { \"number_of_shards\" : \"3\", \"number_of_replicas\" : \"2\" }, \"mappings\" : { \"" + this.getType() + "\" : { \"properties\" : { \"permanentId\" : { \"type\" : \"long\" }, \"version\" : { \"type\" : \"integer\" }, \"createdDate\" : { \"type\" : \"date\" }, \"id\" : { \"type\" : \"long\" }, \"name\" : { \"type\" : \"text\" }, \"requestCount\" : { \"type\" : \"integer\" }, \"trending\" : { \"type\" : \"boolean\" }, \"active\" : { \"type\" : \"boolean\" }, \"publisherId\" : { \"type\" : \"long\" }, \"zipcode\" : { \"type\" : \"keyword\" }, \"location\" : { \"properties\" : { \"permanentId\" : { \"type\" : \"long\" }, \"version\" : { \"type\" : \"integer\" }, \"createdDate\" : { \"type\" : \"date\" }, \"id\" : { \"type\" : \"long\" }, \"city\" : { \"type\" : \"text\" }, \"state\" : { \"type\" : \"text\" }, \"countryCode\" : { \"type\" : \"keyword\" }, \"geoPoint\" : { \"properties\" : { \"id\" : { \"type\" : \"long\" }, \"lat\" : { \"type\" : \"geo_point\" }, \"lon\" : { \"type\" : \"geo_point\" } } } } }, \"styleImages\" : { \"properties\" : { \"url\" : { \"type\" : \"text\" }, \"owner\" : { \"type\" : \"text\" }, \"views\" : { \"type\" : \"integer\" } } } } } }, \"aliases\" : { \"" + this.getAlias() + "\": {} } }";
    }
}
