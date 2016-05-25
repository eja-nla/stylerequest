package com.hair.business.rest.resources.customer;

import static com.hair.business.rest.MvcConstants.CUSTOMER_URI;
import static com.hair.business.rest.MvcConstants.ID;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import com.hair.business.rest.MvcConstants;
import com.hair.business.services.customer.CustomerService;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

/**
 * Customer request controller.
 *
 * Created by Olukorede Aguda on 30/04/2016.
 */
@Path(CUSTOMER_URI)
public class CustomerRequestServlet {

    private CustomerService customerService;

    @Inject
    public CustomerRequestServlet(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GET
    @Path(MvcConstants.INFO)
    @Produces(APPLICATION_JSON)
    public String getCustomerInfo(@QueryParam(ID) String customerId) {
        return "yay, you're here with id " + customerId + " app name " + customerService.getSomeProperty();

    }
}
