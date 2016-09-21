package com.hair.business.services.customer;

import com.hair.business.beans.entity.Image;
import com.hair.business.beans.entity.Style;

import java.util.Collection;

/**
 * Style Service
 *
 * Created by Olukorede Aguda on 20/09/2016.
 */
public interface StyleService {

    Style createStyle(String styleName, Long publisherId, Collection<Image> styleImages);

    void updateStyle(Long styleId, Collection<Image> styleImages);
}
