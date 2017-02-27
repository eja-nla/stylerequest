package com.hair.business.rest.resources.payment;

import static com.hair.business.rest.MvcConstants.PAYMENT_URI;

import com.hair.business.rest.resources.AbstractRequestServlet;
import com.hair.business.services.payment.paypal.PaypalPaymentProcessor;

import javax.inject.Inject;
import javax.ws.rs.Path;

/**
 * Created by olukoredeaguda on 10/02/2017.
 *
 * Paypal payment servlet
 */

@Path(PAYMENT_URI)
public class PaypalPaymentServlet extends AbstractRequestServlet {

    private final PaypalPaymentProcessor PaypalPaymentProcessor;

    @Inject
    public PaypalPaymentServlet(PaypalPaymentProcessor PaypalPaymentProcessor) {
        this.PaypalPaymentProcessor = PaypalPaymentProcessor;
    }

//    @POST
//    @Path(PAYPAL_URI_ENDPOINT)
//    @Consumes(APPLICATION_JSON)
//    @Produces(APPLICATION_JSON)
//    public Response authorisePayment(@Context HttpServletRequest httpServletRequest, @Context HttpServletResponse httpServletResponse) {
//
//        // fixme get rid of this servlet, we won't be triggering payments from client
//        try {
//            StyleRequestPayment payment = PaypalPaymentProcessor
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
//            StyleRequestPayment payment = PaypalPaymentProcessor.(httpServletRequest);
//            httpServletRequest.getRequestDispatcher("response.jsp").forward(httpServletRequest, httpServletResponse);
//            return Response.ok(payment).build();
//        } catch (ServletException e) {
//            return Response.status(Response.Status.BAD_REQUEST).entity(generateErrorResponse(e)).build();
//        } catch (IOException e) {
//            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(generateErrorResponse(e)).build();
//        }
//
//    }
}
