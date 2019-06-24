package com.hair.business.dao.datastore.repository;

import org.elasticsearch.action.support.ActiveShardCount;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;

/**
 * Elasticsearch Datastore repository impl.
 *
 * Created by Olukorede Aguda on 21/06/2019.
 *
 * Google fucking datastore doesn't let us query geosearch, their loss.
 * so we create an elastic datastore impl. For now, only style info will go here/be queried
 * We'll revisit datastore usage later or move completely to elastic
 */
@Named
public class ElasticsearchDatastoreRepositoryImpl {

    private RestHighLevelClient client;

    @Inject
    public ElasticsearchDatastoreRepositoryImpl(Provider<RestHighLevelClient> clientProvider) throws IOException {
        this.client = clientProvider.get();
        //createIndex("test", IOUtils.toString(new FileInputStream(new File("WEB-INF/elasticsearch/style_mapping.json"))));
    }



    public boolean createIndex(String indexName, String mapping) {
        boolean result = false;
        CreateIndexRequest request = new CreateIndexRequest(indexName);
        request.setMasterTimeout(TimeValue.timeValueMinutes(1));
        request.waitForActiveShards(ActiveShardCount.DEFAULT);
        request.source(mapping, XContentType.JSON);
        try {
            result = client.indices().create(request, RequestOptions.DEFAULT).isAcknowledged();
            if (!result) {
                result = client.indices().create(request, RequestOptions.DEFAULT).isAcknowledged(); // return redo
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
