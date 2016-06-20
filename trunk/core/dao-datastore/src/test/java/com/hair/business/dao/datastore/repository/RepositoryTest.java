package com.hair.business.dao.datastore.repository;


import static com.hair.business.dao.datastore.testbase.PersistentEntityTestConstants.CUSTOMER;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import com.hair.business.beans.entity.Customer;
import com.hair.business.dao.datastore.abstractRepository.ObjectifyRepository;
import com.hair.business.dao.datastore.testbase.AbstractDatastoreTestBase;

import org.junit.Test;

/**
 * Created by Olukorede Aguda on 08/06/2016.
 */
public class RepositoryTest extends AbstractDatastoreTestBase{

    private final ObjectifyRepository repository = new ObjectifyDatastoreRepositoryImpl();

    @Test
    public void saveCustomerNow() throws Exception {

        Long id = repository.saveCustomerNow(CUSTOMER);

        assertThat(id, not(new Long(0)));


        Customer x = repository.findCustomerNow(id);
        assertThat(x, is(CUSTOMER));
    }

    @Test
    public void syncFind() throws Exception {

    }

    @Test
    public void asyncSave() throws Exception {

    }

    @Test
    public void asyncFind() throws Exception {

    }

}