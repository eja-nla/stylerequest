package com.hair.business.dao.es.config;

import org.elasticsearch.client.node.NodeClient;
import org.elasticsearch.node.NodeBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.data.elasticsearch.client.TransportClientFactoryBean;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.convert.MappingElasticsearchConverter;
import org.springframework.data.elasticsearch.core.mapping.SimpleElasticsearchMappingContext;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import java.util.Properties;

/**
 * Elasticsearch configuration.
 *
 * Created by Olukorede Aguda on 25/04/2016.
 */
@Configuration
@ComponentScan("com.hair.business.dao.es")
@EnableElasticsearchRepositories(basePackages = "com/hair/business/dao/es/repository")
public class DaoEsConfiguration {
    private String clientTransportSniff = Boolean.TRUE.toString();
    private String clientIgnoreClusterName = Boolean.FALSE.toString();
    private String clientPingTimeout = "5s";
    private String clientNodeSamplerInterval = "5s";

    @Bean
    public TransportClientFactoryBean client(@Value("${es.cluster.name}") String clusterName, @Value("${es.cluster.nodes}") String clusterNodes,
                                             @Value("${es.index.storetype}") String indexStoreType, @Value("${es.index.shards}") String shards,
                                             @Value("${es.index.replicas}") String replicas){
        Properties props = new Properties();

        props.put("cluster.name", clusterName);
        props.put("client.transport.sniff", clientTransportSniff);
        props.put("client.transport.ignore_cluster_name", clientIgnoreClusterName);
        props.put("cluster.transport.ping_timeout", clientPingTimeout);
        props.put("cluster.transport.nodes_sampler_interval", clientNodeSamplerInterval);

        props.put("index.store.type", indexStoreType);
        props.put("index.number_of_shards", shards);
        props.put("index.number_of_replicas", replicas);

        TransportClientFactoryBean factory = new TransportClientFactoryBean();
        factory.setClusterName(clusterName);
        factory.setClusterNodes(clusterNodes);
        factory.setProperties(props);

        return factory;
    }

    @Bean
    public MappingElasticsearchConverter getMappingElasticsearchConverter(SimpleElasticsearchMappingContext ctx){
        return new MappingElasticsearchConverter(ctx);
    }

    @Bean
    private static NodeClient getNodeClient(@Value("${es.cluster.name}") String clusterName) {
        return (NodeClient) new NodeBuilder().clusterName(clusterName).local(true).node().client();
    }

    @Bean
    public ElasticsearchTemplate elasticsearchTemplate(@Value("${es.cluster.name}") String clusterName) {
        return new ElasticsearchTemplate(getNodeClient(clusterName));
    }
}
