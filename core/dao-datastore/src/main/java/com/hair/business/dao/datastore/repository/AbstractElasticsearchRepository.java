package com.hair.business.dao.datastore.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hair.business.beans.abstracts.AbstractPersistenceEntity;
import com.hair.business.beans.entity.GeoPointExt;

import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
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

    private Response createIndex() throws IOException {
        Request request = new Request("PUT","/" + getIndex());
        request.setJsonEntity(this.getMapping());

        return client.performRequest(request);
    }

    /**
     * Saves to the given index and type
     * **/
    public T saveOne(T requestEntity){
        Request saveOneRequest = new Request("PUT", "/" + getIndex() + "/" + getType() + "/" + requestEntity.getPermanentId());
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
        Request getOneRequest = new Request("GET", "/" + getAlias() + "/" + getType() + "/" + id);
        try {
            InputStream res = client.performRequest(getOneRequest).getEntity().getContent();
            return objectMapper.treeToValue(objectMapper.readTree(res).get("_source"), tClass);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Distance search
     * We return the json, no marshalling needed.
     * */
    public String distanceSearch(int kilometers, GeoPointExt geoPoint, String scrollId){
        Request searchRequest = new Request("GET", "/" + getAlias() + "/" + getType() + "/_search?scroll=" + scrollId);
        searchRequest.setJsonEntity(String.format(DISTANCE_QUERY, kilometers, geoPoint.getLat(), geoPoint.getLon()));

        try {
            return objectMapper.readValue(searchRequest.getEntity().getContent(), String.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected void verifyIndex(){
        Request request = new Request("HEAD","/" + getIndex());
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
