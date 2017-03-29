package com.hair.business.services.com.hair.business.client;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.hair.business.services.com.hair.business.client.retry.ExponentialBackOff;
import com.x.business.exception.RestRequestException;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.SerializableEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

/**
 * Abstract client
 *
 * Created by olukoredeaguda on 19/03/2017.
 */
public abstract class AbstractClient<T>{

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final CloseableHttpClient client;
    private final ObjectMapper objectMapper;

    public AbstractClient(CloseableHttpClient client, ObjectMapper objectMapper) {

        this.client = client;
        this.objectMapper = objectMapper;
    }

    protected <V> V doGet(final String url, final Map<String, String> headers, Class<V> vClass) throws IOException {
        int responseCode = -1;
        final HttpGet request = new HttpGet(url);
        headers.forEach(request::addHeader);

        final HttpResponse response = ExponentialBackOff.execute(() -> client.execute(request));

        if (response != null) {
            responseCode = response.getStatusLine().getStatusCode();
        }
        logger.trace("Get request statusCode={} uri='{}'", responseCode, request.getURI());

        switch (responseCode) {
            case 200:
                break;
            case 404:
                throw new RestRequestException(String.format("Resource not found at: {%s}", url));
            case 400:
                throw new RestRequestException(String.format("Bad request at: {%s}", url));
            default:
                throw new RestRequestException("Get request failed from url '{}' error {}");
        }

        return objectMapper.readValue(response.getEntity().getContent(), vClass);
    }

    protected <V> V doPost(final String url, final Map<String, String> headers, final T bean, Class<V> vClass) throws IOException {
        int responseCode = -1;
        final HttpPost request = new HttpPost(url);
        headers.forEach(request::addHeader);

        String json = StringUtils.EMPTY;

        if (bean != null) {
            json = objectMapper.writeValueAsString(bean);
            request.setEntity(new SerializableEntity(json));
        }

        final HttpResponse response = client.execute(request);

        if (response != null) {
            responseCode = response.getStatusLine().getStatusCode();
        }
        logger.trace("Post request statusCode={} uri='{}'", responseCode, request.getURI());

        switch (responseCode) {
            case 200:
                break;
            case 404:
                throw new RestRequestException(String.format("Resource not found at url={%s}", url));
            case 400:
                throw new RestRequestException(String.format("Bad request from url={%s} entity={%s}", url, json));
            default:
                throw new RestRequestException(String.format("Post request failed from url='{%s}'", url));
        }

        return objectMapper.readValue(response.getEntity().getContent(), vClass);
    }

}
