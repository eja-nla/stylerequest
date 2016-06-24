package com.hair.business.dao.datastore.repository;


import static com.hair.business.dao.datastore.ofy.OfyService.ofy;
import static com.x.y.EntityTestConstants.CUSTOMER;
import static com.x.y.EntityTestConstants.createCustomer;
import static com.x.y.EntityTestConstants.createStyle;
import static com.x.y.EntityTestConstants.createStyleRequest;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import com.hair.business.beans.constants.StyleRequestState;
import com.hair.business.beans.entity.Customer;
import com.hair.business.beans.entity.Style;
import com.hair.business.beans.entity.StyleRequest;
import com.hair.business.dao.datastore.abstractRepository.ObjectifyRepository;
import com.hair.business.dao.datastore.testbase.AbstractDatastoreTestBase;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Olukorede Aguda on 08/06/2016.
 */
public class ObjectifyRepositoryTest extends AbstractDatastoreTestBase{

    private final ObjectifyRepository repository = new ObjectifyDatastoreRepositoryImpl();

    @Test
    public void saveCustomerNow() throws Exception {

        Long id = repository.saveCustomerNow(CUSTOMER);
        assertThat(id, not(new Long(0)));

        Customer x = repository.findCustomerNow(id);
        assertThat(x, is(CUSTOMER));
        delete(Collections.singletonList(CUSTOMER));
    }


    @Test
    public void saveCustomers() throws Exception {
        List<Customer> c = createCustomers();
        repository.saveCustomers(c);

        List<Long> x = c.stream().map(Customer::getId).collect(Collectors.toList());
        assertThat(repository.findCustomers(x).size(), is(5));
        delete(c);
    }

    @Test
    public void saveStyleRequest() throws Exception {
        StyleRequest ls = createStyleRequest();
        repository.saveStyleRequest(ls);

        StyleRequest x = repository.findStyleRequest(ls.getId());
        assertThat(x, is(ls));
        delete(Collections.singletonList(x));
    }

    @Test
    public void saveStyleRequests() throws Exception {
        List<StyleRequest> s = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            s.add(createStyleRequest());
        }
        repository.saveStyleRequests(s);

        List<Long> lIds = s.stream().map(StyleRequest::getId).collect(Collectors.toList());
        assertThat(repository.findStyleRequests(lIds, StyleRequestState.ACCEPTED).size(), is(5));
        delete(s);
    }

    @Test
    public void saveStyle() throws Exception {
        Style s = createStyle();
        repository.saveStyle(s);

        Style x = repository.findStyle(s.getId());
        assertThat(x, is(s));
        delete(Collections.singletonList(s));
    }

    private List<Customer> createCustomers(){
        List<Customer> cus = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            cus.add(createCustomer());
        }
        return cus;
    }

    private void delete(List e){
        ofy().delete().entities(e).now();
    }
}