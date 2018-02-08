package com.hair.business.rest.resources.stylerequest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.hair.business.services.StyleRequestService;

import org.joda.time.DateTime;
import org.junit.Test;
import org.mockito.Mockito;

import javax.ws.rs.core.Response;

/**
 * Created by olukoredeaguda on 12/02/2017.
 *
 * Style request servlet test
 */
public class StyleRequestServletTest {

    private final StyleRequestService styleRequestService = Mockito.mock(StyleRequestService.class);
    private final StyleRequestServlet styleRequestServlet = new StyleRequestServlet(styleRequestService);

    @Test
    public void findMerchantAcceptedAppointments() throws Exception {
        Response result_bad_request = styleRequestServlet.findMerchantAcceptedAppointments(1L, "bad date1", "bad date2");
        verify(styleRequestService, never()).findMerchantAcceptedAppointments(anyLong(), any(DateTime.class), any(DateTime.class));
        assertThat(result_bad_request.getStatus(), is(Response.Status.BAD_REQUEST.getStatusCode()));

        Response result_ok = styleRequestServlet.findMerchantAcceptedAppointments(1L, DateTime.now().toString(), DateTime.now().toString());
        verify(styleRequestService, times(1)).findMerchantAcceptedAppointments(anyLong(), any(DateTime.class), any(DateTime.class));
        assertThat(result_ok.getStatus(), is(Response.Status.OK.getStatusCode()));
    }

    @Test
    public void findMerchantCancelledAppointments() throws Exception {
        Response result_bad_request = styleRequestServlet.findMerchantCancelledAppointments(1L, "bad date1", "bad date2");
        verify(styleRequestService, never()).findMerchantCancelledAppointments(anyLong(), any(DateTime.class), any(DateTime.class));
        assertThat(result_bad_request.getStatus(), is(Response.Status.BAD_REQUEST.getStatusCode()));

        Response result_ok = styleRequestServlet.findMerchantCancelledAppointments(1L, DateTime.now().toString(), DateTime.now().toString());
        verify(styleRequestService, times(1)).findMerchantCancelledAppointments(anyLong(), any(DateTime.class), any(DateTime.class));
        assertThat(result_ok.getStatus(), is(Response.Status.OK.getStatusCode()));
    }

//    @Test
//    public void findMerchantPendingAppointments() throws Exception {
//        Response result_bad_request = styleRequestServlet.findMerchantPendingAppointments(1L, "bad date1", "bad date2");
//        verify(styleRequestService, never()).findMerchantPendingAppointments(anyLong(), any(DateTime.class), any(DateTime.class));
//        assertThat(result_bad_request.getStatus(), is(Response.Status.BAD_REQUEST.getStatusCode()));
//
//        Response result_ok = styleRequestServlet.findMerchantPendingAppointments(1L, DateTime.now().toString(), DateTime.now().toString());
//        verify(styleRequestService, times(1)).findMerchantPendingAppointments(anyLong(), any(DateTime.class), any(DateTime.class));
//        assertThat(result_ok.getStatus(), is(Response.Status.OK.getStatusCode()));
//    }

    @Test
    public void findMerchantCompletedAppointments() throws Exception {
        Response result_bad_request = styleRequestServlet.findMerchantCompletedAppointments(1L, "bad date1", "bad date2");
        verify(styleRequestService, never()).findMerchantCompletedAppointments(anyLong(), any(DateTime.class), any(DateTime.class));
        assertThat(result_bad_request.getStatus(), is(Response.Status.BAD_REQUEST.getStatusCode()));

        Response result_ok = styleRequestServlet.findMerchantCompletedAppointments(1L, DateTime.now().toString(), DateTime.now().toString());
        verify(styleRequestService, times(1)).findMerchantCompletedAppointments(anyLong(), any(DateTime.class), any(DateTime.class));
        assertThat(result_ok.getStatus(), is(Response.Status.OK.getStatusCode()));
    }

//    @Test
//    public void findCustomerAcceptedAppointments() throws Exception {
//        Response result_bad_request = styleRequestServlet.findCustomerAcceptedAppointments(1L, "bad date1", "bad date2");
//        verify(styleRequestService, never()).findCustomerAcceptedAppointments(anyLong(), any(DateTime.class), any(DateTime.class));
//        assertThat(result_bad_request.getStatus(), is(Response.Status.BAD_REQUEST.getStatusCode()));
//
//        Response result_ok = styleRequestServlet.findCustomerAcceptedAppointments(1L, DateTime.now().toString(), DateTime.now().toString());
//        verify(styleRequestService, times(1)).findCustomerAcceptedAppointments(anyLong(), any(DateTime.class), any(DateTime.class));
//        assertThat(result_ok.getStatus(), is(Response.Status.OK.getStatusCode()));
//    }

    @Test
    public void findCustomerCancelledAppointments() throws Exception {
        Response result_bad_request = styleRequestServlet.findCustomerCancelledAppointments(1L, "bad date1", "bad date2");
        verify(styleRequestService, never()).findCustomerCancelledAppointments(anyLong(), any(DateTime.class), any(DateTime.class));
        assertThat(result_bad_request.getStatus(), is(Response.Status.BAD_REQUEST.getStatusCode()));

        Response result_ok = styleRequestServlet.findCustomerCancelledAppointments(1L, DateTime.now().toString(), DateTime.now().toString());
        verify(styleRequestService, times(1)).findCustomerCancelledAppointments(anyLong(), any(DateTime.class), any(DateTime.class));
        assertThat(result_ok.getStatus(), is(Response.Status.OK.getStatusCode()));
    }

    @Test
    public void findCustomerPendingAppointments() throws Exception {
        Response result_bad_request = styleRequestServlet.findCustomerPendingAppointments(1L, "bad date1", "bad date2");
        verify(styleRequestService, never()).findCustomerPendingAppointments(anyLong(), any(DateTime.class), any(DateTime.class));
        assertThat(result_bad_request.getStatus(), is(Response.Status.BAD_REQUEST.getStatusCode()));

        Response result_ok = styleRequestServlet.findCustomerPendingAppointments(1L, DateTime.now().toString(), DateTime.now().toString());
        verify(styleRequestService, times(1)).findCustomerPendingAppointments(anyLong(), any(DateTime.class), any(DateTime.class));
        assertThat(result_ok.getStatus(), is(Response.Status.OK.getStatusCode()));
    }

//    @Test
//    public void findCustomerCompletedAppointments() throws Exception {
//        Response result_bad_request = styleRequestServlet.findCustomerCompletedAppointments(1L, "bad date1", "bad date2");
//        verify(styleRequestService, never()).findCustomerCompletedAppointments(anyLong(), any(DateTime.class), any(DateTime.class));
//        assertThat(result_bad_request.getStatus(), is(Response.Status.BAD_REQUEST.getStatusCode()));
//
//        Response result_ok = styleRequestServlet.findCustomerCompletedAppointments(1L, DateTime.now().toString(), DateTime.now().toString());
//        verify(styleRequestService, times(1)).findCustomerCompletedAppointments(anyLong(), any(DateTime.class), any(DateTime.class));
//        assertThat(result_ok.getStatus(), is(Response.Status.OK.getStatusCode()));
//    }

}