package com.hair.business.services.actors;

import com.hair.business.cache.repository.WriteBehind;
import com.hair.business.dao.entity.Customer;
import com.hair.business.dao.entity.Merchant;
import com.hair.business.dao.es.repository.MerchantRepository;
import com.hair.business.services.AbstractPersistenceService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Customer Service Impl.
 *
 * Created by Olukorede Aguda on 27/04/2016.
 */
@Named
public class MerchantServiceImpl extends AbstractPersistenceService<MerchantRepository, Merchant, String> implements MerchantService {

    static final Logger logger = LoggerFactory.getLogger(MerchantServiceImpl.class);

    private final MerchantRepository cRepository;
    private final WriteBehind asyncPersistor;

    @Inject
    public MerchantServiceImpl(MerchantRepository cRepository, WriteBehind asyncPersistor) {
        super(cRepository);

        this.cRepository = cRepository;
        this.asyncPersistor = asyncPersistor;
    }


    public void delete(Customer bean) {

    }

    @Cacheable(cacheNames = {"merchantCache"})
    public Merchant find(String s) {
        return null;
    }

    @Cacheable(cacheNames = {"merchantCache"})
    public Iterable<Merchant> find() {
        return null;
    }

    public Merchant findMerchant(String id) {
        return null;
    }

    public void delete(Merchant bean) {

    }
}
