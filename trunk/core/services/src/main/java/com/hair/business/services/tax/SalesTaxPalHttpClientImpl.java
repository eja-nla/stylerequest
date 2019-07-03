package com.hair.business.services.tax;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hair.business.beans.entity.tax.ComputeTaxRequest;
import com.hair.business.beans.entity.tax.ComputeTaxResponse;
import com.hair.business.services.client.AbstractHttpClient;

import org.apache.http.impl.client.CloseableHttpClient;
import org.joda.time.DateTime;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by Kore Aguda on 6/30/19.
 */
@Named
public class SalesTaxPalHttpClientImpl extends AbstractHttpClient<ComputeTaxRequest, ComputeTaxResponse> {

    private static final String SALSTAXPAL_BASEURL = "https://api.salestaxpal.com/stp/api/rest/v1";
    private Map<String, String> headers = new HashMap<>(4);
    private ObjectMapper objectMapper;

    @Inject
    public SalesTaxPalHttpClientImpl(CloseableHttpClient client, ObjectMapper objectMapper) throws IOException {
        super(client, objectMapper);
        this.objectMapper = objectMapper;

        headers.put("Accept", "application/json");
        headers.put("Content-Type", "application/json");
        headers.put("STP_CLIENT_ID", "STP-API-CLIENT");
        renewApiKey();
    }

    public void renewApiKey() throws IOException {
        String request = "{ \"loginType\": { \"tokenAccess\": { \"accessKeyId\": \"65E7959B-018A-3AE8-9778-30BC0B8ED07B\", \"accessKeyValue\": \"01C37546-BE9E-3404-B5AE-61DFA29187BB\" } }, \"transactionDate\": " + DateTime.now().toString("yyyyMMdd") + "}";

        InputStream x = doPost(request.getBytes(), "access/token");

        String token = objectMapper.readTree(x).get("tokenAccess").get("token").textValue();

        headers.put("Authorization", "Bearer  " + token);
    }

    @Override
    protected String getBaseUrl() {
        return SALSTAXPAL_BASEURL;
    }

    @Override
    protected Map<String, String> getHeaders() {
        return headers;
    }
}
