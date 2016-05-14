package com.hair.business.services.marshaller;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.ext.ContextResolver;

/**
 * Jackson object mapper provider.
 *
 * Created by Olukorede Aguda on 30/04/2016.
 */
@Named
public class ObjectMapperProvider implements ContextResolver<ObjectMapper> {

    private final ObjectMapper mapper;

    @Inject
    public ObjectMapperProvider(ObjectMapper mapper) {
        this.mapper = mapper;
    }


    public ObjectMapper getContext(Class<?> aClass) {
        return mapper;
    }
}
