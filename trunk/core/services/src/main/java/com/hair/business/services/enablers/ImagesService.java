package com.hair.business.services.enablers;


import com.hair.business.dao.entity.Image;
import com.hair.business.services.stereotype.PersistenceService;

import org.springframework.stereotype.Service;

/**
 * Consumer Service.
 *
 * Created by Olukorede Aguda on 25/04/2016.
 */
@Service
public interface ImagesService extends PersistenceService<Image, String> {

    Image findImage(String id);

}
