package com.hair.business.rest.controllers.image;

import static com.hair.business.rest.MvcConstants.ID;
import static com.hair.business.rest.MvcConstants.IMAGE_URI;
import static com.hair.business.rest.MvcConstants.INFO;

import com.hair.business.dao.entity.Image;
import com.hair.business.services.enablers.ImagesService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Customer request controller.
 *
 * Created by Olukorede Aguda on 30/04/2016.
 */
@RestController
@Path(IMAGE_URI)
public class ImageRequestController {

    final Logger logger = LoggerFactory.getLogger(getClass());

    private final ImagesService imagesService;

    @Inject
    public ImageRequestController(ImagesService imagesService) {
        this.imagesService = imagesService;
    }

    @GET
    @Path(INFO)
    @Produces(MediaType.APPLICATION_JSON)
    public Image getCustomerInfo(@PathParam(ID) String imageId) {
        return imagesService.find(imageId);
    }
}
