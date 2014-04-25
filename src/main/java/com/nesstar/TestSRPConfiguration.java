package com.nesstar;

import io.dropwizard.Configuration;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

public class TestSRPConfiguration extends Configuration {
	private String username;
	private String password;
    private String databaseURI;
    
    public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getDatabaseURI() {
		return databaseURI;
	}
	public void setDatabaseURI(String databaseURI) {
		this.databaseURI = databaseURI;
	}
    
}