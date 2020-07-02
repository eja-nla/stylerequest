package com.hair.business.rest.resources.style;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import com.hair.business.services.StyleService;

import org.junit.Test;

import javax.ws.rs.core.Response;

/**
 * Created by ejanla on 7/1/20.
 */
public class StyleServletTest {

    private final StyleService styleService = mock(StyleService.class);
    private final StyleServlet styleServlet = new StyleServlet(styleService, null);


    @Test
    public void testPublishStyle() throws Exception {
        assertThat(styleServlet.publishStyle(null, null).getStatus(), is(Response.Status.OK.getStatusCode()));
    }

}
