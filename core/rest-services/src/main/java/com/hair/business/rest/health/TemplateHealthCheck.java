package com.hair.business.rest.health;

import com.codahale.metrics.health.HealthCheck;
import com.hair.business.rest.DropwizardConfiguration;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by Olukorede Aguda on 14/05/2016.
 */
@Named
public class TemplateHealthCheck extends HealthCheck {

    private DropwizardConfiguration configuration;

    @Inject
    public TemplateHealthCheck(DropwizardConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    protected Result check() throws Exception {
        final String saying = String.format(configuration.getTemplate(), "TEST");
        if (!saying.contains("TEST")) {
            return Result.unhealthy("template doesn't include a name");
        }
        return Result.healthy();
    }
}
