package com.hair.business.services.customer;

import com.hair.business.dao.datastore.abstractRepository.Repository;
import com.hair.business.services.StyleRequestService;
import com.hair.business.services.merchant.MerchantService;
import com.hair.business.services.merchant.MerchantServiceImpl;
import com.x.business.scheduler.TaskQueue;

import org.junit.Before;
import org.mockito.Mockito;

/**
 * Created by Olukorede Aguda on 29/08/2016.
 *
 *
 */
public class MerchantServiceTest extends AbstractServicesTestBase {
    ;
    private MerchantService merchantService;
    private Repository repository;
    private StyleRequestService styleRequestService = Mockito.mock(StyleRequestService.class);
    private TaskQueue emailQueue = Mockito.mock(TaskQueue.class);
    private TaskQueue apnsQueue = Mockito.mock(TaskQueue.class);


    @Before
    public void setUp(){
        repository = injector.getInstance(Repository.class);
        merchantService = new MerchantServiceImpl(repository, styleRequestService, emailQueue, apnsQueue);
    }

}