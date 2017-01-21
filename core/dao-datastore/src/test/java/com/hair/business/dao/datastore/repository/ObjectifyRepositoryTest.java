package com.hair.business.dao.datastore.repository;


import static com.x.y.EntityTestConstants.createAddress;
import static com.x.y.EntityTestConstants.createCustomer;
import static com.x.y.EntityTestConstants.createLocation;
import static com.x.y.EntityTestConstants.createMerchant;
import static com.x.y.EntityTestConstants.createStyle;
import static com.x.y.EntityTestConstants.createStyleRequest;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import com.google.common.collect.Lists;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Result;
import com.hair.business.beans.constants.StyleRequestState;
import com.hair.business.beans.entity.Address;
import com.hair.business.beans.entity.Customer;
import com.hair.business.beans.entity.Location;
import com.hair.business.beans.entity.Merchant;
import com.hair.business.beans.entity.Style;
import com.hair.business.beans.entity.StyleRequest;
import com.hair.business.dao.datastore.abstractRepository.Repository;
import com.hair.business.dao.datastore.testbase.AbstractDatastoreTestBase;

import org.joda.time.DateTime;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by Olukorede Aguda on 08/06/2016.
 */
public class ObjectifyRepositoryTest extends AbstractDatastoreTestBase {

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
    public void testFindBySingleQuery() throws Exception {
        List<StyleRequest> s = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            s.add(createStyleRequest());
        }

        s.get(0).setState(StyleRequestState.IN_PROGRESS);
        repository.saveMany(s);

        // they all got created in state ACCEPTED except one which is IN_PROGRESS. Did we find that loner?
        assertThat(repository.findByQuery(StyleRequest.class, "state", StyleRequestState.IN_PROGRESS).size(), is(1));

        delete(s);
    }

    @Test
    public void testFindByMultiQuery() throws Exception {

        List<StyleRequest> styleRequests = createStyleRequests();
        styleRequests.get(0).setState(StyleRequestState.IN_PROGRESS);
        repository.saveMany(styleRequests);

        List<String> conditions = new ArrayList<>(Arrays.asList("customerPermanentId", "state", "appointmentDateTime >"));
        List<Object> values = new ArrayList<>(Arrays.asList(styleRequests.get(0).getCustomerPermanentId(), StyleRequestState.IN_PROGRESS, new DateTime().minusDays(1)));

        // did we find the styleRequest whose customer has this known ID, is in-progress state, and created within the last 1 day?
        assertThat(repository.findByQuery(StyleRequest.class, conditions, values).size(), is(1));
        delete(styleRequests);
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

    @Test
    public void testSaveFew() throws Exception {
        Merchant mer = createMerchant();
        Address a = createAddress();
        Customer cu = createCustomer();

        repository.saveFew(mer, a, cu);

        Merchant r = repository.findOne(mer.getId(), mer.getClass());
        Address ad = repository.findOne(a.getId(), a.getClass());
        Customer t = repository.findOne(cu.getId(), cu.getClass());

        assertThat(r, is(mer));
        assertThat(ad, is(a));
        assertThat(t, is(cu));

        delete(Lists.newArrayList(mer, ad, cu));
    }

    private List<Customer> createCustomers(){
        List<Customer> cus = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            cus.add(createCustomer());
        }
        return cus;
    }

    private List<StyleRequest> createStyleRequests(){
        List<StyleRequest> styleRequests = new ArrayList<>();
        IntStream.range(0, 5).forEach(itr -> {
            Style st = createStyle();
            Merchant m = createMerchant();
            Customer c = createCustomer();
            Location l = createLocation();
            repository.saveFew(st, m, c, l);
            StyleRequest sr = new StyleRequest(st, m, c, l, StyleRequestState.PENDING, DateTime.now());
            sr.setId(repository.allocateId(StyleRequest.class));
            sr.setPermanentId(sr.getId());
            styleRequests.add(sr);
        });

        return styleRequests;
    }

    private void delete(Collection e){
        repository.delete(e.toArray());
    }
}