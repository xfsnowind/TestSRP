package com.nesstar;

import io.dropwizard.Configuration;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.bazaarvoice.dropwizard.assets.AssetsBundleConfiguration;
import com.bazaarvoice.dropwizard.assets.AssetsConfiguration;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TestSRPConfiguration extends Configuration implements AssetsBundleConfiguration {
	@Valid
    @NotNull
    @JsonProperty
    private final AssetsConfiguration assets = new AssetsConfiguration();
	
	private String username;
	private String password;
    private String databaseURI;
    
    public String getUsername() {
		return username;
	}
	public String getPassword() {
		return password;
	}
	public String getDatabaseURI() {
		return databaseURI;
	}
	
	@Override
	public AssetsConfiguration getAssetsConfiguration() {
		return assets;
	}
}