package com.hair.business.rest.resources.customer;

import static com.hair.business.rest.MvcConstants.CREATE_CUSTOMER_ENDPOINT;
import static com.hair.business.rest.MvcConstants.CUSTOMER_URI;
import static com.hair.business.rest.MvcConstants.ID;
import static com.hair.business.rest.MvcConstants.INFO;
import static com.hair.business.rest.MvcConstants.STYLE_REQUEST_PATH;
import static com.hair.business.rest.MvcConstants.UPDATE_PAYMENT_PATH;
import static com.hair.business.rest.MvcConstants.UPDATE_PREFERENCES_PATH;
import static com.hair.business.rest.RestServicesConstants.REST_USER_ATTRIBUTE;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.slf4j.LoggerFactory.getLogger;

import com.google.identitytoolkit.GitkitUser;

import com.hair.business.beans.constants.PaymentType;
import com.hair.business.beans.constants.Preferences;
import com.hair.business.beans.entity.Customer;
import com.hair.business.beans.entity.PaymentMethod;
import com.hair.business.rest.resources.AbstractRequestServlet;
import com.hair.business.services.StyleRequestService;
import com.hair.business.services.customer.CustomerService;
import com.hair.business.services.payment.PaymentService;

import org.joda.time.DateTime;
import org.slf4j.Logger;

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
public class CustomerRequestServlet extends AbstractRequestServlet {

    private final CustomerService customerService;
    private final StyleRequestService styleRequestService;
    private final PaymentService paymentService;

    private static final Logger log = getLogger(CustomerRequestServlet.class);

    @Inject
    public CustomerRequestServlet(CustomerService customerService, StyleRequestService styleRequestService, PaymentService paymentService) {
        this.customerService = customerService;
        this.styleRequestService = styleRequestService;
        this.paymentService = paymentService;
    }

    @POST
    @Path(CREATE_CUSTOMER_ENDPOINT)
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    public Response createNewCustomer(final @Context HttpServletRequest request, final Customer customer) {
        log.debug("Creating new customer '{}'", customer.getEmail());
        GitkitUser user = (GitkitUser) request.getAttribute(REST_USER_ATTRIBUTE);

        customer.setEmail(user.getEmail());
        customer.setFirstName(user.getName());
        customer.setPhotoUrl(user.getPhotoUrl());

        customerService.saveCustomer(customer);

        return Response.ok().build();
        //todo on signup, store their payment details with braintree
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
    public Response placeStyleRequest(@QueryParam("styleId") Long styleId, @QueryParam("customerId") Long customerId, @QueryParam("merchantId") Long merchantId, @QueryParam("dateTime") String when) {
        try {
            DateTime dateOfRequest = new DateTime(when);
            return Response.ok(styleRequestService.placeStyleRequest(styleId, customerId, merchantId, dateOfRequest)).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(generateErrorResponse(e)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(generateErrorResponse(e)).build();
        }
    }

    @POST
    @Path(UPDATE_PAYMENT_PATH)
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    public Response updatePayment(@QueryParam("customerId") Long customerId, PaymentMethod paymentMethod, PaymentType paymentType, boolean isDefault) {
        try {
            paymentService.updatePayment(customerId, paymentMethod, paymentType, isDefault);
            return Response.ok().build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(generateErrorResponse(e)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(generateErrorResponse(e)).build();
        }
    }

    @POST
    @Path(UPDATE_PREFERENCES_PATH)
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    public Response updatePreferences(@QueryParam("customerId") Long customerId, Preferences preferences) {
        try {
            return Response.ok().entity(customerService.updatePreferences(customerId, preferences)).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(generateErrorResponse(e)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(generateErrorResponse(e)).build();
        }
    }
}
