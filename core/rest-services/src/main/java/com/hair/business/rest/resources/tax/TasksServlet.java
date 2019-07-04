package com.hair.business.rest.resources.tax;

import com.hair.business.rest.resources.AbstractRequestServlet;
import com.hair.business.services.tax.SalesTaxPalHttpClientImpl;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

/**
 * Customer request controller.
 *
 * Created by Olukorede Aguda on 30/04/2016.
 */
@Named
@Path("/tasks")
public class TasksServlet extends AbstractRequestServlet {

    private SalesTaxPalHttpClientImpl salesTaxPalHttpClient;

    @Inject
    public TasksServlet(SalesTaxPalHttpClientImpl salesTaxPalHttpClient) {
        this.salesTaxPalHttpClient = salesTaxPalHttpClient;
    }

    @GET
    @Path("/taxapi/renew")
    public Response renewApiKey() throws IOException {
        salesTaxPalHttpClient.renewApiKey();
        return Response.ok().build();
    }

}
