package com.hair.business.dao.datastore.repository;


import static com.hair.business.dao.datastore.ofy.OfyService.ofy;
import static com.x.y.EntityTestConstants.createCustomer;
import static com.x.y.EntityTestConstants.createMerchant;
import static com.x.y.EntityTestConstants.createStyleRequest;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Result;
import com.hair.business.beans.constants.StyleRequestState;
import com.hair.business.beans.entity.Customer;
import com.hair.business.beans.entity.Merchant;
import com.hair.business.beans.entity.StyleRequest;
import com.hair.business.dao.datastore.abstractRepository.Repository;
import com.hair.business.dao.datastore.testbase.AbstractDatastoreTestBase;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Olukorede Aguda on 08/06/2016.
 */
public class ObjectifyRepositoryTest extends AbstractDatastoreTestBase{

    private final Repository repository = new ObjectifyDatastoreRepositoryImpl();

    @Test
    public void testFindOne() throws Exception {
        Merchant m1 = createMerchant();
        repository.saveOne(m1);
        Merchant r = repository.findOne(m1.getId(), m1.getClass());
        assertThat(r, is(m1));
        delete(Collections.singletonList(m1));
    }

    @Test
    public void testFindMany() throws Exception {
        Collection<Customer> c = createCustomers();
        repository.saveMany(c).now();

        List<Long> x = c.stream().map(Customer::getId).collect(Collectors.toList());
        assertThat(repository.findMany(x, Customer.class).size(), is(5));
        delete(c);
    }

    @Test
    public void testFindByQuery() throws Exception {
        List<StyleRequest> s = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            s.add(createStyleRequest());
        }
        repository.saveMany(s);

        List<Long> lIds = s.stream().map(StyleRequest::getId).collect(Collectors.toList());
        assertThat(repository.findByQuery(lIds, StyleRequest.class, "state ==", StyleRequestState.ACCEPTED).size(), is(5));
        delete(s);
    }


    @Test
    public void testSaveOne() throws Exception {
        Merchant m = createMerchant();

        assertThat(repository.saveOne(m).getId(), is(m.getId()));
        delete(Collections.singletonList(m));
    }

    @Test
    public void testSaveMany() throws Exception {
        Collection<Customer> c = createCustomers();
        Result<Map<Key<Customer>, Customer>> x = repository.saveMany(c);

        assertThat(x.now().entrySet().size(), is(5));
        delete(c);
    }

    private List<Customer> createCustomers(){
        List<Customer> cus = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            cus.add(createCustomer());
        }
        return cus;
    }

    private void delete(Collection e){
        ofy().delete().entities(e).now();
    }
}