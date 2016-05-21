package com.hair.business.services.marshaller;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.ws.rs.ext.ContextResolver;

/**
 * Jackson object mapper provider.
 *
 * Created by Olukorede Aguda on 30/04/2016.
 */
public class ObjectMapperProvider implements ContextResolver<ObjectMapper> {

    private final ObjectMapper mapper;

    public ObjectMapperProvider(ObjectMapper mapper) {
        this.mapper = mapper;
    }


    public ObjectMapper getContext(Class<?> aClass) {
        return mapper;
    }
}
