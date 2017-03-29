package com.hair.business.services.com.hair.business.client.retry;

import java.io.IOException;

/**
 * Exponential backoff functional interface
 *
 * Created by olukoredeaguda on 19/03/2017.
 */
@FunctionalInterface
public interface ExponentialBackOffFunction<T> {
    T execute() throws IOException;
}