package com.nesstar;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jetty.servlets.CrossOriginFilter;

import com.bazaarvoice.dropwizard.assets.ConfiguredAssetsBundle;
import com.nesstar.common.ServerHandler;
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
    	bootstrap.addBundle(new ConfiguredAssetsBundle("/assets/", "/test"));
    }

    @Override
    public void run(TestSRPConfiguration configuration, Environment environment) {
    	Map<String, String> filters = new HashMap<String, String>();
    	String dbURI = configuration.getDatabaseURI();
        String dbUsername = configuration.getUsername();
        String dbPassword = configuration.getPassword();

        ServerHandler serverHandler = new ServerHandler(dbURI, dbUsername, dbPassword);
    	filters.put("allowedHeaders", "X-Requested-With,Content-Type,Accept,Origin");
    	filters.put("allowedOrigins", "*");
    	
    	environment.jersey().register(new RegisterResource(serverHandler));
    	environment.jersey().register(new AuthenticationResource(serverHandler));
    	
    	environment.servlets().addFilter("*", new CrossOriginFilter()).setInitParameters(filters);
    }
}