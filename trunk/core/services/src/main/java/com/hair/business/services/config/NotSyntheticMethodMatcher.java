package com.hair.business.services.config;

import com.google.inject.matcher.AbstractMatcher;

import java.lang.reflect.Method;

/**
 * False synthetic method matcher to label our intercepted methods as non-synthetic and
 * in effect prevent Guice from throwing annoying synthetic interceptor warnings
 *
 * Created by olukoredeaguda on 28/03/2017.
 */
public final class NotSyntheticMethodMatcher extends AbstractMatcher<Method> {

    static final NotSyntheticMethodMatcher INSTANCE = new NotSyntheticMethodMatcher();

    private NotSyntheticMethodMatcher() {}

    @Override
    public boolean matches(Method method) {
        return !method.isSynthetic();
    }
}
