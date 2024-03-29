package com.hair.business.rest.resources;

import static java.util.logging.Logger.getLogger;

import java.util.logging.Logger;

/**
 * Created by olukoredeaguda on 24/05/2016.
 *
 *
 */
public abstract class AbstractRequestServlet {

    private static final Logger logger = getLogger(AbstractRequestServlet.class.getName());

    protected String generateErrorResponse(Exception exception) {

        exception.printStackTrace();
        logger.warning(exception.getMessage());
        return String.format("{\"status\" : \"failed\", \"message\" : \"Oops! We didn't quite process your request correctly. " +
                    "It has been logged for review. Apologies for the inconvenience. Actual error message as follows: %s\"}", exception.getMessage());
    }

    protected String wrapString(String entity) {
        return String.format("{\"key\" : \"%s\"}", entity);
    }

    protected String wrapString(String keyString, String entity) {
        return String.format("{\"%s\" : \"%s\"}", keyString, entity);
    }

}
