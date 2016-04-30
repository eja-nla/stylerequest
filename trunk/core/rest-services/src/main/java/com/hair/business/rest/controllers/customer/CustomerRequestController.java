package com.hair.business.rest.controllers.customer;

import static com.hair.business.rest.MvcConstants.*;


import com.hair.business.dao.entity.Customer;
import com.hair.business.services.actors.CustomerService;
import com.hair.business.services.actors.MerchantService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Customer request controller.
 *
 * Created by Olukorede Aguda on 30/04/2016.
 */
@RestController
@Path(CUSTOMER_URI)
public class CustomerRequestController {

    final Logger logger = LoggerFactory.getLogger(getClass());

    private final CustomerService customerService;
    private final MerchantService merchantService;

    @Inject
    public CustomerRequestController(CustomerService customerService, MerchantService merchantService) {
        this.customerService = customerService;
        this.merchantService = merchantService;
    }

    @GET
    @Path(INFO)
    @Produces(MediaType.APPLICATION_JSON)
    public Customer getCustomerInfo(@PathParam(ID) String customerId) {
        return customerService.find(customerId);
    }
}
