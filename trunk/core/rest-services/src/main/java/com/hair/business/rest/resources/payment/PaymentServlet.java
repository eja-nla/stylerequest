//package com.hair.business.rest.resources.payment;
//
//import static com.hair.business.rest.MvcConstants.PAYMENT_URI;
//import static com.hair.business.rest.MvcConstants.PAYPAL_URI_ENDPOINT;
//import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
//
//import com.hair.business.beans.entity.StyleRequestPayment;
//import com.hair.business.rest.resources.AbstractRequestServlet;
//import com.hair.business.services.payment.PaymentProcessor;
//import com.hair.business.services.payment.paypal.PaypalPaymentProcessor;
//
//import java.io.IOException;
//
//import javax.inject.Inject;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.ws.rs.Consumes;
//import javax.ws.rs.POST;
//import javax.ws.rs.Path;
//import javax.ws.rs.Produces;
//import javax.ws.rs.core.Context;
//import javax.ws.rs.core.Response;
//
///**
// * Created by olukoredeaguda on 10/02/2017.
// *
// * Ideally, this shouldn't be a servlet but on a second thought, it would be useful
// * if we ever have a need to manually reconcile certain payments e.g. to re
// *
// * Paypal payment servlet
// */
//
//@Path(PAYMENT_URI)
//public class PaymentServlet extends AbstractRequestServlet {
//
//    private final PaymentProcessor PaypalPaymentProcessor;
//
//    @Inject
//    public PaymentServlet(PaypalPaymentProcessor PaypalPaymentProcessor) {
//        this.PaypalPaymentProcessor = PaypalPaymentProcessor;
//    }
//
//    @POST
//    @Path(PAYPAL_URI_ENDPOINT)
//    @Consumes(APPLICATION_JSON)
//    @Produces(APPLICATION_JSON)
//    public Response authorisePayment(@Context HttpServletRequest httpServletRequest, @Context HttpServletResponse httpServletResponse) {
//
//        try {
//            StyleRequestPayment payment = PaypalPaymentProcessor.holdPayment(null, null, 2, 2);
//            httpServletRequest.getRequestDispatcher("response.jsp").forward(httpServletRequest, httpServletResponse);
//            return Response.ok(payment).build();
//        } catch (ServletException e) {
//            return Response.status(Response.Status.BAD_REQUEST).entity(generateErrorResponse(e)).build();
//        } catch (IOException e) {
//            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(generateErrorResponse(e)).build();
//        }
//
//    }
//
//    @POST
//    @Path(PAYPAL_URI_ENDPOINT)
//    @Consumes(APPLICATION_JSON)
//    @Produces(APPLICATION_JSON)
//    public Response captureAuthorisedPayment(@Context HttpServletRequest httpServletRequest, @Context HttpServletResponse httpServletResponse) {
//
//        try {
//            StyleRequestPayment payment = PaypalPaymentProcessor.releasePayment(null, 2, false);
//            httpServletRequest.getRequestDispatcher("response.jsp").forward(httpServletRequest, httpServletResponse);
//            return Response.ok(payment).build();
//        } catch (ServletException e) {
//            return Response.status(Response.Status.BAD_REQUEST).entity(generateErrorResponse(e)).build();
//        } catch (IOException e) {
//            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(generateErrorResponse(e)).build();
//        }
//
//    }
//}
