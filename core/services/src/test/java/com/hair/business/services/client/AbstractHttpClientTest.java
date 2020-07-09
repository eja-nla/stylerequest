package com.hair.business.services.client;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hair.business.beans.entity.tax.ComputeTaxResponse;
import com.hair.business.services.tax.SalesTaxPalHttpClientImpl;

import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by ejanla on 7/8/20.
 */
public class AbstractHttpClientTest {

    private CloseableHttpClient client;
    private ObjectMapper objectMapper;
    private StatusLine statusLine = mock(StatusLine.class);

    private SalesTaxPalHttpClientImpl salesTaxPalHttpClient;

    @Before
    public void setUp() throws Exception {
        client = Mockito.mock(CloseableHttpClient.class);
        objectMapper = Mockito.mock(ObjectMapper.class);
        JsonNode node = mock(JsonNode.class);
        when(node.get(anyString())).thenReturn(node);
        when(node.textValue()).thenReturn("token value");
        when(objectMapper.readTree(any(InputStream.class))).thenReturn(node);

        CloseableHttpResponse closeableHttpResponse = Mockito.mock(CloseableHttpResponse.class);
        when(closeableHttpResponse.getStatusLine()).thenReturn(statusLine);
        HttpEntity entity = Mockito.mock(HttpEntity.class);
        when(entity.getContent()).thenReturn(new ByteArrayInputStream(new byte[1]));
        when(closeableHttpResponse.getEntity()).thenReturn(entity);
        when(closeableHttpResponse.getStatusLine()).thenReturn(statusLine);
        when(statusLine.getStatusCode()).thenReturn(200);

        when(objectMapper.readValue(any(InputStream.class), any(Class.class))).thenReturn(new ComputeTaxResponse());

        when(client.execute(Mockito.any(HttpGet.class))).thenReturn(closeableHttpResponse);

        salesTaxPalHttpClient =  new SalesTaxPalHttpClientImpl(client, objectMapper);
    }

    @Test
    public void doGet() throws IOException {
        ComputeTaxResponse resx = salesTaxPalHttpClient.doGet(ComputeTaxResponse.class, "/some/endpoint");
        Assert.assertThat(resx, notNullValue());
    }

    @Test
    public void doPost() throws IOException {
        InputStream resx = salesTaxPalHttpClient.doPost(new byte[1], "/some/endpoint");
        Assert.assertThat(resx, notNullValue());
    }
}