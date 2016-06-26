package com.hair.business.rest.provider;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * Created by Olukorede Aguda on 26/06/2016.
 */
public class JacksonJsonProvidersProvider implements Provider<JacksonJsonProvider> {

    private final ObjectMapper mapper;

    @Inject
    public JacksonJsonProvidersProvider(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public JacksonJsonProvider get() {
        return new JacksonJsonProvider(mapper);
    }
}
