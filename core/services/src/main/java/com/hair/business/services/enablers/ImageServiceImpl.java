package com.hair.business.services.enablers;

import com.hair.business.cache.repository.WriteBehind;
import com.hair.business.dao.entity.Customer;
import com.hair.business.dao.entity.Image;
import com.hair.business.dao.es.repository.ImageRepository;
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
public class ImageServiceImpl extends AbstractPersistenceService<ImageRepository, Image, String> implements ImagesService {

    static final Logger logger = LoggerFactory.getLogger(ImageServiceImpl.class);

    private final ImageRepository cRepository;
    private final WriteBehind asyncPersistor;

    @Inject
    public ImageServiceImpl(ImageRepository cRepository, WriteBehind asyncPersistor) {
        super(cRepository);

        this.cRepository = cRepository;
        this.asyncPersistor = asyncPersistor;
    }


    public void delete(Customer bean) {

    }

    @Cacheable(cacheNames = {"imageCache"})
    public Image find(String s) {
        return null;
    }

    @Cacheable(cacheNames = {"imageCache"})
    public Iterable<Image> find() {
        return null;
    }

    public Image findMerchant(String id) {
        return null;
    }

    public void delete(Image bean) {

    }

    public Image findImage(String id) {
        return null;
    }
}
