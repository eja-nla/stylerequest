package com.hair.business.services.customer;

import com.hair.business.dao.datastore.abstractRepository.Repository;

import org.junit.Before;

/**
 * Created by Olukorede Aguda on 29/08/2016.
 *
 *
 */
public class MerchantServiceTest extends AbstractServicesTestBase {

    MerchantService merchantService;
    Repository repository;


    @Before
    public void setUp(){
        repository = injector.getInstance(Repository.class);
        merchantService = new MerchantServiceImpl(repository);
    }


}