package com.hair.business.dao.es.abstractRepository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

/**
 * Abstract Postgres repository wrapper
 *
 * Created by Olukorede Aguda on 25/04/2016.
 */
@Repository
public interface AbstractPostgresRepository<T, PK extends Serializable> extends CrudRepository<T, PK> {
}
