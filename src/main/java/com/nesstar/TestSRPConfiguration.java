package com.nesstar;

import io.dropwizard.Configuration;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

public class TestSRPConfiguration extends Configuration {
	@NotEmpty
    @JsonProperty
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
}