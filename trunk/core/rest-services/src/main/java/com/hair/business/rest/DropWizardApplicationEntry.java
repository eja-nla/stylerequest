package com.hair.business.rest;

import com.codahale.metrics.health.HealthCheck;
import com.hair.business.rest.context.SpringContextLoaderListener;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.Path;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

/**
 * Dropwizard entry point
 *
 * Created by Olukorede Aguda on 14/05/2016.
 *
 */
@Named
public class DropWizardApplicationEntry extends Application<DropwizardConfiguration> {

    private AnnotationConfigWebApplicationContext rootContext;
    private final String appName;

    @Inject
    public DropWizardApplicationEntry(AnnotationConfigWebApplicationContext parent, @Value("${config.file.yaml}") String yamlConfig, @Value("${app.rootName}") String appName) throws Exception {
        this.rootContext = parent;
        this.appName = appName;

        run("server", yamlConfig);
    }

	@Override
	public String getName() {
		return appName;
	}

	@Override
	public void initialize(Bootstrap<DropwizardConfiguration> bootstrap) {
		// nothing to do yet
	}

	@Override
	public void run(DropwizardConfiguration configuration, Environment environment) {

        rootContext.getBeanFactory().registerSingleton("configuration", configuration);

        //health checks
        Map<String, HealthCheck> healthChecks = rootContext.getBeansOfType(HealthCheck.class);
        for(Map.Entry<String,HealthCheck> entry : healthChecks.entrySet()) {
            environment.healthChecks().register("template", entry.getValue());
        }

        //resources
        Map<String, Object> resources = rootContext.getBeansWithAnnotation(Path.class);
        for(Map.Entry<String,Object> entry : resources.entrySet()) {
            environment.jersey().register(entry.getValue());
        }

        environment.servlets().addServletListeners(new SpringContextLoaderListener(rootContext));
	}
}
