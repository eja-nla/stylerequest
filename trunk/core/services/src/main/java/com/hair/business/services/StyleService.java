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

    /**
     * Radius find
     * */
    InputStream geoFind(GeoPointExt point, int radius, int pageSize);

    /**
     * Term based search
     * */
    InputStream search(String term);

    /**
     * Term based radius search
     * */
    InputStream geoSearch(String term, int radius, double lat, double lon);

    /**
     * Publish new style
     * */
    Style publishStyle(Style style, Long publisherId);

    /**
     * Update the images of a style
     * */
    void updateStyleImages(Long styleId, List<Image> styleImages);

    /**
     * update style details
     * */
    void updateStyle(Style style);

    /**
     * find styles using ids
     * */
    List<Style> findStyles(List<Long> ids);

    /**
     * deactivate style
     * */
    void removeStyle(Long styleId);

    /**
     * Fetches additional pages of a scroll request
     * */
    InputStream scroll(String scroll, String scrollId);
}
