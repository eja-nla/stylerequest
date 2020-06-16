package com.hair.business.services;

import com.hair.business.beans.entity.GeoPointExt;
import com.hair.business.beans.entity.Image;
import com.hair.business.beans.entity.Style;

import java.io.InputStream;
import java.util.List;

/**
 * Style Service
 *
 * Created by Olukorede Aguda on 20/09/2016.
 */
public interface StyleService {

    Style findStyle(Long styleId);

    InputStream geoSeachStyles(GeoPointExt point, int radius, int pageSize);

    Style publishStyle(Style style, Long publisherId);

    void updateStyleImages(Long styleId, List<Image> styleImages);

    void updateStyle(Style style);

    List<Style> findStyles(List<Long> ids);

    List<Style> findStylesByDescription(String description);

    void removeStyle(Long styleId);

    /**
     * Fetches additional pages of a scroll request
     * */
    InputStream scroll(String scroll, String scrollId);
}
