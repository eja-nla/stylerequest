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
