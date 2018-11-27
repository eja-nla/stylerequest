package com.hair.business.services;

import com.hair.business.beans.entity.Image;
import com.hair.business.beans.entity.Style;

import java.util.List;

/**
 * Style Service
 *
 * Created by Olukorede Aguda on 20/09/2016.
 */
public interface StyleService {

    Style findStyle(Long styleId);

    Style publishStyle(Style style, Long publisherId);

    void updateStyleImages(Long styleId, List<Image> styleImages);

    void updateStyle(Style style);

    List<Style> findStyles(List<Long> ids);

    List<Style> findStylesByDescription(String description);

    void removeStyle(Long styleId);
}
