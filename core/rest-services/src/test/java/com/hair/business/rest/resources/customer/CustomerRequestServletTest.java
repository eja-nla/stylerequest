package com.hair.business.rest.resources.customer;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import com.hair.business.beans.entity.Customer;
import com.hair.business.rest.testbase.AbstractServletTestBase;

/**
 * Created by Olukorede Aguda on 26/06/2016.
 */
public class CustomerRequestServletTest extends AbstractServletTestBase {

    CustomerRequestServlet svl = injector.getInstance(CustomerRequestServlet.class);

   // @Test
    public void getCustomerInfo() throws Exception {
        //HttpServletRequest req = mock(HttpServletRequest.class);
        Customer x = svl.getCustomerInfo(new Long(1));
        assertThat(x, is(nullValue()));
    }

}