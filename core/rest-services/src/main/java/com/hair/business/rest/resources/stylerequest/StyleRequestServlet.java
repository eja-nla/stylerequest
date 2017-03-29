package com.hair.business.rest.resources.stylerequest;

import static com.hair.business.rest.MvcConstants.FIND_CUSTOMER_ACCEPTED_STYLEREQUEST_ENDPOINT;
import static com.hair.business.rest.MvcConstants.FIND_CUSTOMER_CANCELLED_STYLEREQUEST_ENDPOINT;
import static com.hair.business.rest.MvcConstants.FIND_CUSTOMER_COMPLETED_STYLEREQUEST_ENDPOINT;
import static com.hair.business.rest.MvcConstants.FIND_CUSTOMER_PENDING_STYLEREQUEST_ENDPOINT;
import static com.hair.business.rest.MvcConstants.FIND_MERCHANT_ACCEPTED_STYLEREQUEST_ENDPOINT;
import static com.hair.business.rest.MvcConstants.FIND_MERCHANT_CANCELLED_STYLEREQUEST_ENDPOINT;
import static com.hair.business.rest.MvcConstants.FIND_MERCHANT_COMPLETED_STYLEREQUEST_ENDPOINT;
import static com.hair.business.rest.MvcConstants.FIND_MERCHANT_PENDING_STYLEREQUEST_ENDPOINT;
import static com.hair.business.rest.MvcConstants.STYLE_REQUEST_URI;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import com.hair.business.beans.entity.StyleRequest;
import com.hair.business.rest.resources.AbstractRequestServlet;
import com.hair.business.services.StyleRequestService;

import org.joda.time.DateTime;

import java.util.Collection;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Style request controller.
 *
 * Created by Olukorede Aguda on 12/02/2017.
 */
@Path(STYLE_REQUEST_URI)
public class StyleRequestServlet extends AbstractRequestServlet {

    private final StyleRequestService styleRequestService;

    @Inject
    public StyleRequestServlet(StyleRequestService styleRequestService) {
        this.styleRequestService = styleRequestService;
    }

    @POST
    @Path(FIND_MERCHANT_ACCEPTED_STYLEREQUEST_ENDPOINT)
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    public Response findMerchantAcceptedAppointments(@QueryParam("merchantId") Long merchantId, @QueryParam("lower") String lower, @QueryParam("upper") String upper) {
        final Collection<StyleRequest> styleRequests;
        try {
            final DateTime lowerLimit = new DateTime(lower);
            final DateTime upperLimit = new DateTime(upper);
            styleRequests = styleRequestService.findMerchantAcceptedAppointments(merchantId, lowerLimit, upperLimit);
        } catch (IllegalArgumentException e){
            return Response.status(Response.Status.BAD_REQUEST).entity(generateErrorResponse(e)).build();
        } catch (Exception e){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(generateErrorResponse(e)).build();
        }
        return Response.ok(styleRequests, MediaType.APPLICATION_JSON).build();
    }

    @POST
    @Path(FIND_MERCHANT_CANCELLED_STYLEREQUEST_ENDPOINT)
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    public Response findMerchantCancelledAppointments(@QueryParam("merchantId") Long merchantId, @QueryParam("lower") String lower, @QueryParam("upper") String upper) {
        final Collection<StyleRequest> styleRequests;
        try {
            final DateTime lowerLimit = new DateTime(lower);
            final DateTime upperLimit = new DateTime(upper);
            styleRequests = styleRequestService.findMerchantCancelledAppointments(merchantId, lowerLimit, upperLimit);
        } catch (IllegalArgumentException e){
            return Response.status(Response.Status.BAD_REQUEST).entity(generateErrorResponse(e)).build();
        } catch (Exception e){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(generateErrorResponse(e)).build();
        }
        return Response.ok(styleRequests, MediaType.APPLICATION_JSON).build();
    }

    @POST
    @Path(FIND_MERCHANT_PENDING_STYLEREQUEST_ENDPOINT)
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    public Response findMerchantPendingAppointments(@QueryParam("merchantId") Long merchantId, @QueryParam("lower") String lower, @QueryParam("upper") String upper) {
        final Collection<StyleRequest> styleRequests;
        try {
            final DateTime lowerLimit = new DateTime(lower);
            final DateTime upperLimit = new DateTime(upper);
            styleRequests = styleRequestService.findMerchantPendingAppointments(merchantId, lowerLimit, upperLimit);
        } catch (IllegalArgumentException e){
            return Response.status(Response.Status.BAD_REQUEST).entity(generateErrorResponse(e)).build();
        } catch (Exception e){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(generateErrorResponse(e)).build();
        }
        return Response.ok(styleRequests, MediaType.APPLICATION_JSON).build();
    }

    @POST
    @Path(FIND_MERCHANT_COMPLETED_STYLEREQUEST_ENDPOINT)
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    public Response findMerchantCompletedAppointments(@QueryParam("merchantId") Long merchantId, @QueryParam("lower") String lower, @QueryParam("upper") String upper) {
        final Collection<StyleRequest> styleRequests;
        try {
            final DateTime lowerLimit = new DateTime(lower);
            final DateTime upperLimit = new DateTime(upper);
            styleRequests = styleRequestService.findMerchantCompletedAppointments(merchantId, lowerLimit, upperLimit);
        } catch (IllegalArgumentException e){
            return Response.status(Response.Status.BAD_REQUEST).entity(generateErrorResponse(e)).build();
        }  catch (Exception e){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(generateErrorResponse(e)).build();
        }
        return Response.ok(styleRequests, MediaType.APPLICATION_JSON).build();
    }

    @POST
    @Path(FIND_CUSTOMER_ACCEPTED_STYLEREQUEST_ENDPOINT)
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    public Response findCustomerAcceptedAppointments(@QueryParam("customerId") Long customerId, @QueryParam("lower") String lower, @QueryParam("upper") String upper) {
        final Collection<StyleRequest> styleRequests;
        try {
            final DateTime lowerLimit = new DateTime(lower);
            final DateTime upperLimit = new DateTime(upper);
            styleRequests = styleRequestService.findCustomerAcceptedAppointments(customerId, lowerLimit, upperLimit);
        } catch (IllegalArgumentException e){
            return Response.status(Response.Status.BAD_REQUEST).entity(generateErrorResponse(e)).build();
        } catch (Exception e){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(generateErrorResponse(e)).build();
        }
        return Response.ok(styleRequests, MediaType.APPLICATION_JSON).build();
    }

    @POST
    @Path(FIND_CUSTOMER_CANCELLED_STYLEREQUEST_ENDPOINT)
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    public Response findCustomerCancelledAppointments(@QueryParam("customerId") Long customerId, @QueryParam("lower") String lower, @QueryParam("upper") String upper) {
        final Collection<StyleRequest> styleRequests;
        try {
            final DateTime lowerLimit = new DateTime(lower);
            final DateTime upperLimit = new DateTime(upper);
            styleRequests = styleRequestService.findCustomerCancelledAppointments(customerId, lowerLimit, upperLimit);
        } catch (IllegalArgumentException e){
            return Response.status(Response.Status.BAD_REQUEST).entity(generateErrorResponse(e)).build();
        } catch (Exception e){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(generateErrorResponse(e)).build();
        }
        return Response.ok(styleRequests, MediaType.APPLICATION_JSON).build();
    }

    @POST
    @Path(FIND_CUSTOMER_PENDING_STYLEREQUEST_ENDPOINT)
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    public Response findCustomerPendingAppointments(@QueryParam("customerId") Long customerId, @QueryParam("lower") String lower, @QueryParam("upper") String upper) {
        final Collection<StyleRequest> styleRequests;
        try {
            final DateTime lowerLimit = new DateTime(lower);
            final DateTime upperLimit = new DateTime(upper);
            styleRequests = styleRequestService.findCustomerPendingAppointments(customerId, lowerLimit, upperLimit);
        } catch (IllegalArgumentException e){
            return Response.status(Response.Status.BAD_REQUEST).entity(generateErrorResponse(e)).build();
        } catch (Exception e){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(generateErrorResponse(e)).build();
        }
        return Response.ok(styleRequests, MediaType.APPLICATION_JSON).build();
    }

    @POST
    @Path(FIND_CUSTOMER_COMPLETED_STYLEREQUEST_ENDPOINT)
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    public Response findCustomerCompletedAppointments(@QueryParam("customerId") Long customerId, @QueryParam("lower") String lower, @QueryParam("upper") String upper) {
        final Collection<StyleRequest> styleRequests;
        try {
            final DateTime lowerLimit = new DateTime(lower);
            final DateTime upperLimit = new DateTime(upper);
            styleRequests = styleRequestService.findCustomerCompletedAppointments(customerId, lowerLimit, upperLimit);
        } catch (IllegalArgumentException e){
            return Response.status(Response.Status.BAD_REQUEST).entity(generateErrorResponse(e)).build();
        } catch (Exception e){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(generateErrorResponse(e)).build();
        }
        return Response.ok(styleRequests, MediaType.APPLICATION_JSON).build();
    }

}
