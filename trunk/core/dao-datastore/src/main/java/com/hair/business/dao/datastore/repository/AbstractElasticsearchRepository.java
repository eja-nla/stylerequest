package com.hair.business.dao.datastore.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hair.business.beans.abstracts.AbstractPersistenceEntity;
import com.hair.business.beans.entity.GeoPointExt;

import org.elasticsearch.client.Request;
import org.elasticsearch.client.RestClient;

import java.io.IOException;
import java.io.InputStream;

import javax.inject.Provider;

/**
 * Abstract Elasticsearch Datastore repository impl.
 *
 * Created by Olukorede Aguda on 21/06/2019.
 *
 * Google fucking datastore doesn't let us query geosearch, their loss.
 * so we create an elastic datastore impl. For now, only style info will go here/be queried
 * We'll revisit datastore usage later or move completely to elastic
 */
public abstract class AbstractElasticsearchRepository<T extends AbstractPersistenceEntity> {

    private final RestClient client;
    private final ObjectMapper objectMapper;

    protected AbstractElasticsearchRepository(Provider<RestClient> clientProvider, Provider<ObjectMapper> objectMapperProvider) {
        this.client = clientProvider.get();
        this.objectMapper = objectMapperProvider.get();
    }

    private void createIndex() throws IOException {
        final Request request = new Request("PUT","/" + getIndex());
        request.setJsonEntity(this.getMapping());
        client.performRequest(request);
    }

    /**
     * Saves to the given index
     * **/
    public T saveOne(T requestEntity){
        //ES 7.x mandates use of default _doc type
        final Request saveOneRequest = new Request("POST", "/" + getIndex() + "/_doc/" + requestEntity.getPermanentId());
        try {
            String c = objectMapper.writeValueAsString(requestEntity);
            saveOneRequest.setJsonEntity(c);
            client.performRequest(saveOneRequest);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return requestEntity;
    }

    /**
     * Gets from the provided aliases
     * **/
    public T findOne(Long id, Class<T> tClass){
        final Request getOneRequest = new Request("GET", "/" + getIndex() + "/_source/" + id);
        try {
            InputStream res = client.performRequest(getOneRequest).getEntity().getContent();
            return objectMapper.readValue(res, tClass);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Initiates a scroll search
     * We return the json to upstream, no marshalling needed.
     *
     * Remember to close all user search context i.e. DELETE /_search/scroll/_all - from client when they logout
     * */
    public InputStream searchWithScroll(String queryString, int scrollTimeout, int size){
        // remember to only search style.active = true;
        //{ "query": { "term": { "active": true } } }
        final Request searchRequest = new Request("POST", "/" + getAlias() + "/_search?scroll=" + scrollTimeout + "&size=" + size);
        searchRequest.setJsonEntity(queryString);

        try {
            return client.performRequest(searchRequest).getEntity().getContent();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Initiates a search
     * We return the json to upstream, no marshalling needed.
     *
     * */
    public InputStream search(String queryString, int size, String filter){
        // remember to only search style.active = true; GET /active_hairstyles/_search?q=active:true&size=2
        final Request searchRequest = new Request("POST", "/" + getAlias() + "/_search?size=" + size + filter);
        searchRequest.setJsonEntity(queryString);

        try {
            return client.performRequest(searchRequest).getEntity().getContent();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Distance search
     * We return the json to upstream, no marshalling needed.
     * */
    public InputStream searchRadius(String query, int kilometers, GeoPointExt geoPoint, int pageSize){
        final Request searchRequest = new Request("POST", "/" + getAlias() + "/_search?scroll=1m&size=" + pageSize);
        searchRequest.setJsonEntity(String.format(query, kilometers, geoPoint.getLat(), geoPoint.getLon()));

        try {
            return client.performRequest(searchRequest).getEntity().getContent();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Convenience pass through to Elastic
     * Issues query and returns raw response
     * */
    public InputStream blindQuery(String query, String endpoint, String httpMethod){
        final Request searchRequest = new Request(httpMethod, endpoint);
        searchRequest.setJsonEntity(query);
        try {
            return client.performRequest(searchRequest).getEntity().getContent();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Fetches a scroll's result
     * We return the json to upstream, no marshalling needed.
     * */
    public InputStream fetchScroll(String newScrollTimeout, String scrollId){
        final Request searchRequest = new Request("POST", "/_search/scroll");
        searchRequest.setJsonEntity("{\n" +
                "    \"scroll\" : \"" + newScrollTimeout + "\", \n" +
                "    \"scroll_id\" : \"" + scrollId + "\" \n" +
                "}"
        );
        try {
            return client.performRequest(searchRequest).getEntity().getContent();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected void verifyIndex(){
        final Request request = new Request("HEAD","/" + getIndex());
        try {
            if (client.performRequest(request).getStatusLine().getStatusCode() != 200){
                createIndex();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected abstract String getIndex();
    protected abstract String getMapping();
    protected abstract String getAlias();

}
