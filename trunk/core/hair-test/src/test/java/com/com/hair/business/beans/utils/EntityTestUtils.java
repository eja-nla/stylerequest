package com.com.hair.business.beans.utils;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;

import org.apache.commons.collections.CollectionUtils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;

/**
 * Created by Olukorede Aguda on 20/08/2016.
 *
 * Entity test utils.
 *
 * Convenient class for entity/json mapping tests.
 * These tests are intentionally added in the repository module
 * This is because the entities contain objectify annotations -
 * meaning they require datastore initialization/registrations which is better handled here
 */
public class EntityTestUtils {

    private final ObjectMapper objectMapper;

    public static final EntityTestUtils TEST_UTILS  = new EntityTestUtils();

    public EntityTestUtils() {
        objectMapper = new ObjectMapper();
        objectMapper.configure(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS , false);
        objectMapper.registerModule(new JodaModule());

    }



    /**
     * validator to ensure test json files fields directly mirror their corresponding classes
     * */
    public <T> void validateFieldsAreEqual(T obj1, T obj2) throws JsonProcessingException {

        Collection<Field> classFields1 = Arrays.asList(obj1.getClass().getDeclaredFields());
        Collection<Field> classFields2 = Arrays.asList(obj2.getClass().getDeclaredFields());

        for (Field field : classFields1) {
            field.setAccessible(true);
            try {
                if (field.get(obj2) == null) {
                    fail("found null field '" + field.getName() + "'. " +
                            "Please ensure that the fields in Json test file '" + obj2.getClass().getSimpleName().toLowerCase() +
                            ".json' match the fields declared in "+ obj2.getClass() + " JSON: \n" + objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj2));
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace(); // no need for logger
            }
        }
        assertTrue(CollectionUtils.isEqualCollection(classFields2, classFields1));
    }

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }
}
