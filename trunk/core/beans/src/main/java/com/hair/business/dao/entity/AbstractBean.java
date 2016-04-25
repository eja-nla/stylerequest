package com.hair.business.dao.entity;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Abstract bean.
 * Created by olukoredeaguda on 25/04/2016.
 */
public abstract class AbstractBean {

    @Override
    public String toString(){
        return new ReflectionToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).toString();
    }
}
