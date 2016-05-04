package com.hair.business.dao.abstracts;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;

/**
 * Abstract persistence entity
 *
 * Created by Olukorede Aguda on 25/04/2016.
 */
public abstract class AbstractPersistenceEntity extends AbstractBean {
    @Id
    private String id;

    @Version
    private Long version;

    public AbstractPersistenceEntity(){
        this.version = 1L;
    }

    @Override
    public boolean equals(Object obj){
        if(!(obj instanceof AbstractPersistenceEntity)){
            return false;
        }
        if(this == obj){
            return true;
        }

        AbstractPersistenceEntity bean = (AbstractPersistenceEntity) obj;

        return new EqualsBuilder().append(this.id, bean.id).isEquals();
    }

    @Override
    public int hashCode(){
        return new HashCodeBuilder().append(id).toHashCode();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
}