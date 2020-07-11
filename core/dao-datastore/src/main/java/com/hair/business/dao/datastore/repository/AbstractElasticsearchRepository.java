package com.hair.business.dao.datastore.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hair.business.beans.abstracts.AbstractPersistenceEntity;
import com.hair.business.beans.entity.GeoPointExt;
import com.x.business.exception.EntityNotFoundException;

import org.elasticsearch.client.Request;
import org.elasticsearch.client.RestClient;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.atomic.AtomicLong;

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
    private final AtomicLong atomicLong = new AtomicLong();

    protected AbstractElasticsearchRepository(Provider<RestClient> clientProvider, Provider<ObjectMapper> objectMapperProvider) {
        this.client = clientProvider.get();
        this.objectMapper = objectMapperProvider.get();
    }

    private void createIndex(String indexName, String mapping) throws IOException {
        final Request request = new Request("PUT","/" + indexName);
        request.setJsonEntity(mapping);
        client.performRequest(request);
    }

    /**
     * Convenient method to help migrate entities from the activeIndex to the archivedIndex when we no longer need it
     * e.g. when a Chat is created, we put in active and facilitate the conversation between two actors. When the chat ends, we archive it
     *
     * */
    public T archive(Long id, Class<T> tClass){
        final Request getOneRequest = new Request("GET", "/" + getActiveIndex() + "/_source/" + id);
        T response;
        try {
            InputStream res = client.performRequest(getOneRequest).getEntity().getContent(); // fetch from active
            response = objectMapper.readValue(res, tClass);
            response.setActive(false);
            saveOne(response, getArchivedIndex());  // write to archived index
            deleteOne(id); // delete from active
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return response;
    }

    /**
     * Saves to the given index
     * **/
    public T saveOne(T requestEntity){
        return saveOne(requestEntity, getActiveIndex());
    }

    /**
     * Saves to the given index
     * **/
    private T saveOne(T requestEntity, String index){
        //ES 7.x mandates use of default _doc type
        final Request saveOneRequest = new Request("POST", "/" + index + "/_doc/" + requestEntity.getPermanentId());
        try {
            requestEntity.setVersion(atomicLong.incrementAndGet());
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
        final Request getOneRequest = new Request("GET", "/" + getActiveIndex() + "/_source/" + id);
        try {
            InputStream res = client.performRequest(getOneRequest).getEntity().getContent();
            return objectMapper.readValue(res, tClass);
        } catch (IOException e) {
            throw new EntityNotFoundException(e);
        }
    }

    /**
     * Deletes from the provided aliases
     * Must not be exposed. Use archive method instead
     * **/
    private void deleteOne(Long id){
        final Request deleteOneRequest = new Request("DELETE", "/" + getActiveIndex() + "/_doc/" + id);
        try {
            client.performRequest(deleteOneRequest).getEntity().getContent();
        } catch (IOException e) {
            throw new EntityNotFoundException(e);
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
        final Request searchRequest = new Request("POST", "/" + getActiveAlias() + "/_search?scroll=" + scrollTimeout + "&size=" + size);
        searchRequest.setJsonEntity(queryString);

        try {
            return client.performRequest(searchRequest).getEntity().getContent();
        } catch (IOException e) {
            throw new EntityNotFoundException(e);
        }
    }

    /**
     * Initiates a search
     * We return the json to upstream, no marshalling needed.
     *
     * */
    public InputStream search(String queryString, int size, String filter){
        // remember to only search style.active = true; GET /active_hairstyles/_search?q=active:true&size=2
        final Request searchRequest = new Request("POST", "/" + getActiveAlias() + "/_search?size=" + size + filter);
        searchRequest.setJsonEntity(queryString);

        try {
            return client.performRequest(searchRequest).getEntity().getContent();
        } catch (IOException e) {
            throw new EntityNotFoundException(e);
        }
    }

    /**
     * Distance search
     * We return the json to upstream, no marshalling needed.
     * */
    public InputStream searchRadius(String query, int kilometers, GeoPointExt geoPoint, int pageSize){
        final Request searchRequest = new Request("POST", "/" + getActiveAlias() + "/_search?scroll=1m&size=" + pageSize);
        searchRequest.setJsonEntity(String.format(query, kilometers, geoPoint.getLat(), geoPoint.getLon()));

        try {
            return client.performRequest(searchRequest).getEntity().getContent();
        } catch (IOException e) {
            throw new EntityNotFoundException(e);
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
            throw new EntityNotFoundException(e);
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
            throw new EntityNotFoundException(e);
        }
    }

    protected void verifyIndex(){
        final Request request = new Request("HEAD","/" + getActiveIndex());
        try {
            if (client.performRequest(request).getStatusLine().getStatusCode() != 200){
                //we haven't created this index before, so we create the active and archived indices
                // with the same mapping but different aliases
                createIndex(getActiveIndex(), getMapping(getActiveAlias()));
                createIndex(getArchivedIndex(), getMapping(getArchivedAlias()));
            }
        } catch (IOException e) {
            throw new EntityNotFoundException(e);
        }
    }

    /**
     * We store in ActiveIndex by default
     * Whenever we deep the entity to no longer be active,
     * we fetch the latest from ActiveIndex and move to ArchivedIndex
     * */
    protected abstract String getMapping(String alias);

    protected abstract String getActiveIndex();
    protected abstract String getArchivedIndex();

    protected abstract String getArchivedAlias();
    protected abstract String getActiveAlias();

}
