package com.hair.business.rest.resources.customer;

import static com.hair.business.rest.MvcConstants.CREATE;
import static com.hair.business.rest.MvcConstants.CUSTOMER_URI;
import static com.hair.business.rest.MvcConstants.ID;
import static com.hair.business.rest.MvcConstants.INFO;
import static com.hair.business.rest.MvcConstants.STYLE_REQUEST_PATH;
import static com.hair.business.rest.RestServicesConstants.DATE_TIME_FORMATTER;
import static com.hair.business.rest.RestServicesConstants.REST_USER_ATTRIBUTE;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import com.google.identitytoolkit.GitkitUser;

import com.hair.business.beans.entity.Customer;
import com.hair.business.beans.entity.StyleRequest;
import com.hair.business.services.customer.CustomerService;

import java.util.logging.Logger;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

/**
 * Customer request controller.
 *
 * Created by Olukorede Aguda on 30/04/2016.
 */
@Path(CUSTOMER_URI)
public class CustomerRequestServlet {

    private final CustomerService customerService;

    private static final Logger log = Logger.getLogger(CustomerRequestServlet.class.getName());

    @Inject
    public CustomerRequestServlet(CustomerService customerService) {
        this.customerService = customerService;
    }

    @POST
    @Path(CREATE)
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    public Response createNewCustomer(@Context HttpServletRequest request, Customer customer) {
        log.fine(String.format("Creating new customer %s", customer.getEmail()));
        GitkitUser user = (GitkitUser) request.getAttribute(REST_USER_ATTRIBUTE);

        customer.setEmail(user.getEmail());
        customer.setName(user.getName());
        customer.setPhotoUrl(user.getPhotoUrl());

        customerService.saveCustomer(customer);

        return Response.created(null).build();
    }

    @GET
    @Path(INFO)
    @Produces(APPLICATION_JSON)
    public Customer getCustomerInfo(@QueryParam(ID) Long customerId) {
        return customerService.findCustomer(customerId);
    }


    @POST
    @Path(STYLE_REQUEST_PATH)
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    //Style style, Customer customer, Merchant merchant, Location location, DateTime dateTime
    public StyleRequest placeStyleRequest(@QueryParam("styleId") Long styleId, @QueryParam("customerId") Long customerId, @QueryParam("merchantId") Long merchantId, @QueryParam("dateTime") String when) {
        StyleRequest styleRequest = customerService.placeStyleRequest(styleId, customerId, merchantId, DATE_TIME_FORMATTER.parseDateTime(when));
        return styleRequest;
    }
}
