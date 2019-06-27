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

    private static final String DISTANCE_QUERY = "{\n" +
            "    \"query\": {\n" +
            "        \"bool\" : {\n" +
            "            \"must\" : {\n" +
            "                \"match_all\" : {}\n" +
            "            },\n" +
            "            \"filter\" : {\n" +
            "                \"geo_distance\" : {\n" +
            "                    \"distance\" : \"%skm\",\n" +
            "                    \"pin.location\" : {\n" +
            "                        \"lat\" : %s,\n" +
            "                        \"lon\" : %s\n" +
            "                    }\n" +
            "                }\n" +
            "            }\n" +
            "        }\n" +
            "    }\n" +
            "}";

    private final RestClient client;
    private final ObjectMapper objectMapper;

    public AbstractElasticsearchRepository(Provider<RestClient> clientProvider, Provider<ObjectMapper> objectMapperProvider) {
        this.client = clientProvider.get();
        this.objectMapper = objectMapperProvider.get();
    }

    private void createIndex() throws IOException {
        final Request request = new Request("PUT","/" + getIndex());
        request.setJsonEntity(this.getMapping());

        client.performRequest(request);
    }

    /**
     * Saves to the given index and type
     * **/
    public T saveOne(T requestEntity){
        final Request saveOneRequest = new Request("PUT", "/" + getIndex() + "/" + getType() + "/" + requestEntity.getPermanentId());
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
        final Request getOneRequest = new Request("GET", "/" + getAlias() + "/" + getType() + "/" + id);
        try {
            InputStream res = client.performRequest(getOneRequest).getEntity().getContent();
            return objectMapper.treeToValue(objectMapper.readTree(res).get("_source"), tClass);
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
    public String searchWithScroll(String queryString, String scrollTimeout, int size){
        final Request searchRequest = new Request("POST", "/" + getAlias() + "/_search?scroll=" + scrollTimeout + "&size=" + size);
        searchRequest.setJsonEntity(queryString);

        try {
            return objectMapper.writeValueAsString(searchRequest.getEntity().getContent());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Distance search
     * We return the json to upstream, no marshalling needed.
     * */
    public String searchRadius(int kilometers, GeoPointExt geoPoint){
        final Request searchRequest = new Request("GET", "/" + getAlias() + "/" + getType());
        searchRequest.setJsonEntity(String.format(DISTANCE_QUERY, kilometers, geoPoint.getLat(), geoPoint.getLon()));

        try {
            return objectMapper.writeValueAsString(searchRequest.getEntity().getContent());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Convenience pass through to Elastic
     * Issues query and returns raw response
     * */
    public String searchBlind(String query, String endpoint, String httpMethod){
        final Request searchRequest = new Request(httpMethod, endpoint);
        searchRequest.setJsonEntity(query);
        try {
            return objectMapper.writeValueAsString(searchRequest.getEntity().getContent());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Fetches a scroll's result
     * We return the json to upstream, no marshalling needed.
     * */
    public String fetchScroll(String newScrollTimeout, String scrollId){
        final Request searchRequest = new Request("POST", "/_search/scroll");
        searchRequest.setJsonEntity("{\n" +
                "    \"scroll\" : \"" + newScrollTimeout + "\", \n" +
                "    \"scroll_id\" : \"" + scrollId + "\" \n" +
                "}"
        );
        try {
            return objectMapper.writeValueAsString(searchRequest.getEntity().getContent());
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
    protected abstract String getType();
    protected abstract String getAlias();

}
