package com.hair.business.rest.resources.customer;

import static com.hair.business.rest.MvcConstants.CUSTOMER_URI;
import static com.hair.business.rest.MvcConstants.ID;
import static com.hair.business.rest.MvcConstants.INFO;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import com.google.identitytoolkit.GitkitUser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hair.business.services.customer.CustomerService;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;

/**
 * Customer request controller.
 *
 * Created by Olukorede Aguda on 30/04/2016.
 */
@Path(CUSTOMER_URI)
public class CustomerRequestServlet {

    private final CustomerService customerService;
    private final ObjectMapper mapper;

    @Inject
    public CustomerRequestServlet(CustomerService customerService, ObjectMapper mapper) {
        this.customerService = customerService;
        this.mapper = mapper;
    }

    @GET
    @Path(INFO)
    @Produces(APPLICATION_JSON)
    public String getCustomerInfo(@Context HttpServletRequest request, @QueryParam(ID) Long customerId) throws JsonProcessingException {
        GitkitUser user = (GitkitUser) request.getAttribute("user");
        return null;
    }
}
