package com.hair.business.app.provider;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.inject.Provider;

/**
 * Object mapper provider.
 *
 * Created by Olukorede Aguda on 26/06/2016.
 */
public class ObjectMapperProvider implements Provider<ObjectMapper> {

    @Override
    public ObjectMapper get() {

        final ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        return mapper;
    }
}
