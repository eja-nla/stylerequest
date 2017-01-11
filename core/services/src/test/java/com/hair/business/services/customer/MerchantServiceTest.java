package com.hair.business.services.customer;

import com.hair.business.dao.datastore.abstractRepository.Repository;

import org.junit.Before;

/**
 * Created by Olukorede Aguda on 29/08/2016.
 *
 *
 */
public class MerchantServiceTest extends AbstractServicesTestBase {

    private MerchantService merchantService;
    private Repository repository;


    @Before
    public void setUp(){
        repository = injector.getInstance(Repository.class);
        merchantService = new MerchantServiceImpl(repository);
    }

}