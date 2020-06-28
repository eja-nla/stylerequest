package com.hair.business.services.client;


import static com.hair.business.services.client.retry.RetryWithExponentialBackOff.execute;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.x.business.exception.RestRequestException;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Map;

/**
 * Abstract client
 *
 * Created by olukoredeaguda on 19/03/2017.
 */
public abstract class AbstractHttpClient<In, Out> {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final CloseableHttpClient client;
    private final ObjectMapper objectMapper;

    protected AbstractHttpClient(CloseableHttpClient client, ObjectMapper objectMapper) {
        this.client = client;
        this.objectMapper = objectMapper;
    }

    protected Out doGet(Class<Out> responseClass, String endpoint) throws IOException {
        int responseCode = -1;
        String fullUrl = getBaseUrl() + "/" + endpoint;
        final HttpGet request = new HttpGet(fullUrl);
        getHeaders().forEach(request::addHeader);

        final HttpResponse response = execute(() -> client.execute(request));

        if (response != null) {
            responseCode = response.getStatusLine().getStatusCode();
        }
        logger.trace("Get request statusCode={} uri='{}'", responseCode, request.getURI());

        switch (responseCode) {
            case 200:
                break;
            case 404:
                throw new RestRequestException(String.format("Resource not found at: {%s}", fullUrl));
            case 400:
                throw new RestRequestException(String.format("Bad request at: {%s}", fullUrl));
            default:
                throw new RestRequestException(String.format("Get request failed from url '{%s}'", fullUrl));
        }

        return objectMapper.readValue(response.getEntity().getContent(), responseClass);
    }

    protected Out doPost(final In requestBean, Class<Out> responseClass, String endpoint) throws IOException {

        if (requestBean == null) {
            return null;
        }

        byte[] jsonBytes = objectMapper.writeValueAsBytes(requestBean);

        final InputStream response = execute(() -> doPost(jsonBytes, endpoint));

        return objectMapper.readValue(response, responseClass);
    }

    protected InputStream doPost(final byte[] requestBean, String endpoint) throws IOException {
        int responseCode = -1;

        String fullUrl = getBaseUrl() + "/" + endpoint;

        final HttpPost request = new HttpPost(fullUrl);
        getHeaders().forEach(request::addHeader);

        if (requestBean != null) {
            request.setEntity(new ByteArrayEntity(requestBean));
        }

        final HttpResponse response = execute(() -> client.execute(request));

        if (response != null) {
            responseCode = response.getStatusLine().getStatusCode();
        }
        logger.info("Post request statusCode={} uri='{}'", responseCode, request.getURI());

        switch (responseCode) {
            case 200:
                break;
            case 404:
                throw new RestRequestException(String.format("Resource not found at url={%s} requestEntity={%s}", fullUrl, Arrays.toString(requestBean)));
            case 400:
                throw new RestRequestException(String.format("Bad request from url={%s} requestEntity={%s}", fullUrl, Arrays.toString(requestBean)));
            default:
                throw new RestRequestException(String.format("Post request failed from url='{%s}' requestEntity={%s}", fullUrl, Arrays.toString(requestBean)));
        }

        return response.getEntity().getContent();
    }

    protected abstract String getBaseUrl();
    protected abstract Map<String, String> getHeaders();


}
