package com.hair.business.rest.resources.customer;

import static com.hair.business.rest.MvcConstants.CREATE_CUSTOMER_ENDPOINT;
import static com.hair.business.rest.MvcConstants.CUSTOMER_URI;
import static com.hair.business.rest.MvcConstants.ID;
import static com.hair.business.rest.MvcConstants.INFO;
import static com.hair.business.rest.MvcConstants.STYLE_REQUEST_PATH;
import static com.hair.business.rest.MvcConstants.UPDATE_PAYMENT_PATH;
import static com.hair.business.rest.RestServicesConstants.REST_USER_ATTRIBUTE;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import com.google.identitytoolkit.GitkitUser;

import com.hair.business.beans.entity.Customer;
import com.hair.business.beans.entity.Payment;
import com.hair.business.beans.entity.StyleRequest;
import com.hair.business.services.StyleRequestService;
import com.hair.business.services.customer.CustomerService;
import com.x.business.utilities.Assert;

import org.joda.time.DateTime;

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
    private final StyleRequestService styleRequestService;

    private static final Logger log = Logger.getLogger(CustomerRequestServlet.class.getName());

    @Inject
    public CustomerRequestServlet(CustomerService customerService, StyleRequestService styleRequestService) {
        this.customerService = customerService;
        this.styleRequestService = styleRequestService;
    }

    @POST
    @Path(CREATE_CUSTOMER_ENDPOINT)
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    public Response createNewCustomer(@Context HttpServletRequest request, Customer customer) {
        log.fine(String.format("Creating new customer %s", customer.getEmail()));
        GitkitUser user = (GitkitUser) request.getAttribute(REST_USER_ATTRIBUTE);

        customer.setEmail(user.getEmail());
        customer.setFirstName(user.getName());
        customer.setPhotoUrl(user.getPhotoUrl());

        customerService.saveCustomer(customer);

        return Response.ok().build();
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
    public StyleRequest placeStyleRequest(@QueryParam("styleId") Long styleId, @QueryParam("customerId") Long customerId, @QueryParam("merchantId") Long merchantId, @QueryParam("dateTime") String when) {
        DateTime dateOfRequest = new DateTime(when);
        return styleRequestService.placeStyleRequest(styleId, customerId, merchantId, dateOfRequest);
    }

    @POST
    @Path(UPDATE_PAYMENT_PATH)
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    public Response updatePayment(@QueryParam("customerId") Long customerId, Payment payment) {
        try {
            Assert.validId(customerId);
            Assert.notNull(payment, "Payment cannot be null");
            return Response.ok().entity(customerService.updatePaymentInfo(customerId, payment)).build();

        } catch (IllegalArgumentException e) {

            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
}
