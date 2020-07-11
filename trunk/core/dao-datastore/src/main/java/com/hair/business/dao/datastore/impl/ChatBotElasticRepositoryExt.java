package com.hair.business.dao.datastore.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hair.business.beans.entity.chat.Chat;
import com.hair.business.dao.datastore.repository.AbstractElasticsearchRepository;

import org.apache.commons.io.IOUtils;
import org.elasticsearch.client.RestClient;
import org.joda.time.DateTime;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;

/**
 * Chat Elastic Repo
 * Created by ejanla on 7/10/20.
 */
@Named
public class ChatBotElasticRepositoryExt extends AbstractElasticsearchRepository<Chat> {

    private static final DateTime dateTime = DateTime.now();

    private static final String activeChatIndexName = "chat" + "_" + dateTime.getMonthOfYear() + "_" + dateTime.getYear();
    private static final String inactiveChatIndexName = "chat" + "_archived_" + dateTime.getMonthOfYear() + "_" + dateTime.getYear();

    @Inject
    protected ChatBotElasticRepositoryExt(Provider<RestClient> clientProvider, Provider<ObjectMapper> objectMapperProvider) {
        super(clientProvider, objectMapperProvider);

        verifyIndex();
    }

    @Override
    protected String getArchivedIndex() {
        return inactiveChatIndexName;
    }

    @Override
    protected String getActiveIndex() {
        return activeChatIndexName;
    }

    @Override
    protected String getMapping(String alias) {
        try {
            String mapping = IOUtils.toString(new FileInputStream(new File("WEB-INF/elasticsearch/chat_mapping.json")));
            return String.format(mapping, alias);
        } catch (IOException e) {
            throw new RuntimeException("Mappings file for " + activeChatIndexName + " could not be loaded.");
        }
    }

    @Override
    protected String getActiveAlias() {
        // when the chat is closed (stylerequest is completed or cancelled), we should move this Chat to a different index?
        // That makes this alias much lighter
        return "active_chats";
    }

    @Override
    protected String getArchivedAlias() {
        return "archived_chats";
    }
}
