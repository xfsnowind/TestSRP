package com.nesstar;

import org.eclipse.jetty.servlets.CrossOriginFilter;

import com.nesstar.resources.AuthenticationResource;
import com.nesstar.resources.RegisterResource;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class TestSRPApplication extends Application<TestSRPConfiguration> {
    public static void main(String[] args) throws Exception {
        new TestSRPApplication().run(args);
    }

    @Override
    public void initialize(Bootstrap<TestSRPConfiguration> bootstrap) {
        // nothing to do yet
    }

    @Override
    public void run(TestSRPConfiguration configuration, Environment environment) {
    	environment.jersey().register(new RegisterResource());
    	environment.jersey().register(new AuthenticationResource());
    	
    	environment.servlets().addFilter("/*", new CrossOriginFilter())
    	.setInitParameter(CrossOriginFilter.ALLOWED_HEADERS_PARAM, "X-Requested-With,Content-Type,Accept,Origin");
    }
}