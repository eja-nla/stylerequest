package com.hair.business.dao.abstracts;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

/**
 * Abstract bean.
 *
 * Created by Olukorede Aguda on 25/04/2016.
 */
public abstract class AbstractBean implements Serializable{

    @Override
    public String toString(){
        return new ReflectionToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).toString();
    }
}
