package com.hair.business.dao.entity;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;

/**
 * Created by olukoredeaguda on 25/04/2016.
 */
public abstract class AbstractPersistentEntity extends AbstractBean {
    @Id
    private String id;

    @Version
    private Long version;

    public AbstractPersistentEntity(){
        this.version = 1L;
    }

    @Override
    public boolean equals(Object obj){
        if(!(obj instanceof AbstractPersistentEntity)){
            return false;
        }
        if(this == obj){
            return true;
        }

        AbstractPersistentEntity bean = (AbstractPersistentEntity) obj;

        return new EqualsBuilder().append(this.id, bean.id).isEquals();
    }

    @Override
    public int hashCode(){
        return new HashCodeBuilder().append(id).toHashCode();
    }
}
