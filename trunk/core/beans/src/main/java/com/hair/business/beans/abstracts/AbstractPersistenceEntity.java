package com.hair.business.beans.abstracts;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.joda.time.DateTime;

/**
 * Abstract persistence entity
 *
 * Created by Olukorede Aguda on 25/04/2016.
 */
public abstract class AbstractPersistenceEntity extends AbstractBean {

    private Long permanentId; // An idempotent field that stores the very first id every assigned to this entity. If we ever move/reindex/reimport the entity, this ensures a unique identifier that never changes

    private Long version;

    private DateTime createdDate;

    AbstractPersistenceEntity(){
        this.version = 1L;
        this.createdDate = DateTime.now();
    }

    @Override
    public boolean equals(Object obj){
        if(!(obj instanceof AbstractPersistenceEntity)){
            return false;
        }

        if (!this.getClass().equals(obj.getClass())) {
            return false;
        }

        if(this == obj){
            return true;
        }

        AbstractPersistenceEntity bean = (AbstractPersistenceEntity) obj;

        return new EqualsBuilder().append(this.permanentId, bean.permanentId).isEquals();
    }

    @Override
    public int hashCode(){
        return new HashCodeBuilder().append(permanentId).toHashCode();
    }

    public Long getPermanentId() {
        return permanentId;
    }

    /**
     * Enforce idempotency on permanentId
     * */
    public void setPermanentId(Long permanentId) {
        if (this.permanentId == null) {
            this.permanentId = permanentId;
        }

    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public DateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(DateTime createdDate) {
        this.createdDate = createdDate;
    }
}