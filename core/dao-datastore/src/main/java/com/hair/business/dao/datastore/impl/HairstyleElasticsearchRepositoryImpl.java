package com.hair.business.dao.datastore.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hair.business.beans.entity.Style;
import com.hair.business.dao.datastore.repository.AbstractElasticsearchRepository;

import org.apache.commons.io.IOUtils;
import org.elasticsearch.client.RestClient;
import org.joda.time.DateTime;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;

/**
 * Created by Kore Aguda on 6/24/19.
 */
@Named
public class HairstyleElasticsearchRepositoryImpl extends AbstractElasticsearchRepository<Style> {

    private static final DateTime dateTime = DateTime.now();
    private static final String styleIndexName = "hairstyles" + "_" + dateTime.getMonthOfYear() + "_" + dateTime.getYear();

    public static final String DISTANCE_QUERY = "{\n" +
            "    \"query\": {\n" +
            "        \"bool\" : {\n" +
            "            \"must\" : {\n" +
            "                \"term\" : { \"active\" : true }\n" +
            "            },\n" +
            "            \"filter\" : {\n" +
            "                \"geo_distance\" : {\n" +
            "                    \"distance\" : \"%skm\",\n" +
            "                    \"location.geoPoint\" : {\n" +
            "                        \"lat\" : %s,\n" +
            "                        \"lon\" : %s\n" +
            "                    }\n" +
            "                }\n" +
            "            }\n" +
            "        }\n" +
            "    }\n" +
            "}";

    private static final String STYLE_QUERY = "{\n" +
            "  \"query\": {\n" +
            "    \"bool\" : {\n" +
            "      \"must\" : {\n" +
            "        \"term\" : { \"active\" : true }\n" +
            "      },\n" +
            "      \"should\" : [\n" +
            "        { \"term\" : { \"name\" : \"%s\" } },\n" +
            "        { \"term\" : { \"description\" : \"%s\" } }\n" +
            "      ],\n" +
            "      \"minimum_should_match\" : 1,\n" +
            "      \"boost\" : 1.0\n" +
            "    }\n" +
            "  }\n" +
            "}";

    private static final String GEO_STYLE_QUERY = "{\n" +
            "  \"query\": {\n" +
            "    \"bool\" : {\n" +
            "      \"must\" : {\n" +
            "        \"term\" : { \"active\" : true }\n" +
            "      },\n" +
            "      \"should\" : [\n" +
            "        { \"term\" : { \"name\" : \"%s\" } },\n" +
            "        { \"term\" : { \"description\" : \"%s\" } }\n" +
            "      ],\n" +
            "      \"filter\": {\n" +
            "        \"geo_distance\" : { \n" +
            "           \"distance\" : \"%skm\",\n" +
            "           \"location.geoPoint\" : {\n" +
            "              \"lat\" : \"%s\",\n" +
            "              \"lon\" : \"%s\"\n" +
            "           }\n" +
            "         }\n" +
            "      },\n" +
            "      \"minimum_should_match\" : 1,\n" +
            "      \"boost\" : 1.0\n" +
            "    }\n" +
            "  }\n" +
            "}";

    @Inject
    public HairstyleElasticsearchRepositoryImpl(Provider<RestClient> clientProvider, Provider<ObjectMapper> objectMapperProvider) {
        super(clientProvider, objectMapperProvider);

        verifyIndex();
    }

    public InputStream search(String term){
        return super.search(String.format(STYLE_QUERY, term, term), 100, "&filter_path=hits.hits._source");
    }

    /**
     * Search by term, limiting results to a distance
     * */
    public InputStream geoTermSearch(String term, int radius, double lat, double lon){
        //search by name and/or description based on
        return super.search(String.format(GEO_STYLE_QUERY, term, term, radius, lat, lon), 100, "&filter_path=hits.hits._source");
    }

    @Override
    protected String getIndex() {
        return styleIndexName;
    }

    @Override
    protected String getAlias() {
        return "active_hairstyles";
    }

    @Override
    protected String getMapping(){
        try {
            String mapping = IOUtils.toString(new FileInputStream(new File("WEB-INF/elasticsearch/hairstyle_mapping.json")));
            return String.format(mapping, getAlias());
        } catch (IOException e) {
            throw new RuntimeException("Mappings file for " + styleIndexName + " could not be loaded.");
        }
    }
}
