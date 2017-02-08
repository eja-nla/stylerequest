package com.hair.business.services;

import com.hair.business.beans.entity.Image;
import com.hair.business.beans.entity.Merchant;
import com.hair.business.beans.entity.Style;

import java.util.List;

/**
 * Style Service
 *
 * Created by Olukorede Aguda on 20/09/2016.
 */
public interface StyleService {

    Style findStyle(Long styleId);

    Style publishStyle(String styleName, int duration, Long publisherId, List<Image> styleImages);

    void updateStyleImages(Long styleId, List<Image> styleImages);

    void updateStyle(Style style);

    List<Style> findStyles(List<Long> ids);

    List<Style> findStylesByDescription(String description);

    /**
     * Publish a new style that can be requested
     */
    void publishStyle(Style style, Merchant merchant);
}
