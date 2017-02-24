package com.hair.business.rest.resources.merchant;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.identitytoolkit.GitkitUser;

import com.hair.business.beans.entity.Merchant;
import com.hair.business.services.StyleRequestService;
import com.hair.business.services.StyleService;
import com.hair.business.services.merchant.MerchantService;

import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;

/**
 *
 *
 * Created by Olukorede Aguda on 20/02/2017.
 */
public class MerchantRequestServletTest {

    private final MerchantService merchantService = mock(MerchantService.class);
    private final StyleService styleService = mock(StyleService.class);
    private final StyleRequestService styleRequestService = mock(StyleRequestService.class);

    private final MerchantRequestServlet merchantRequestServlet = new MerchantRequestServlet(merchantService, styleService, styleRequestService);

    @Test
    public void testGetMerchantInfo() throws Exception {
        assertThat(merchantRequestServlet.getMerchantInfo(1L).getStatus(), is(Response.Status.OK.getStatusCode()));

        assertThat(merchantRequestServlet.getMerchantInfo(0L).getStatus(), is(Response.Status.BAD_REQUEST.getStatusCode()));
    }

    @Test
    public void testCreateMerchant() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getAttribute("user")).thenReturn(new GitkitUser());
        assertThat(merchantRequestServlet.createMerchant(request, new Merchant()).getStatus(), is(Response.Status.OK.getStatusCode()));
    }

    @Test
    public void testPublishStyle() throws Exception {
        assertThat(merchantRequestServlet.publishStyle(null, null, 1, 2L).getStatus(), is(Response.Status.OK.getStatusCode()));
    }

    @Test
    public void testAcceptRequest() throws Exception {
        assertThat(merchantRequestServlet.acceptRequest(1L, null).getStatus(), is(Response.Status.OK.getStatusCode()));
    }

    @Test
    public void testCancelRequest() throws Exception {
        assertThat(merchantRequestServlet.cancelRequest(1L, null).getStatus(), is(Response.Status.OK.getStatusCode()));
    }

    @Test
    public void testCompleteRequest() throws Exception {
        assertThat(merchantRequestServlet.completeRequest(1L, null).getStatus(), is(Response.Status.OK.getStatusCode()));
    }
}