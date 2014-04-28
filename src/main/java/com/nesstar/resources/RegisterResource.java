package com.nesstar.resources;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import com.codahale.metrics.annotation.Timed;
import com.nesstar.common.ServerHandler;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path("/register")
@Produces(MediaType.APPLICATION_JSON)
public class RegisterResource {
	private final static String dbClass = "com.mysql.jdbc.Driver";
	private String verifier;
    private String username;
    private String salt;
    private ServerHandler serverHanlder;
	
	public RegisterResource(ServerHandler serverHandler) {
		this.serverHanlder = serverHandler;
	}
	
    

    @GET
    @Timed
    public void register(@QueryParam("username") String username,
    					 @QueryParam("salt") String salt,
    					 @QueryParam("verifier") String verifier) {
        this.verifier = verifier;
        this.salt = salt;
        this.username = username;
        
        Connection connection;
        String query = "insert into SRP.User(username, salt, verifier) values('" + this.username + "','" + this.salt + "','" + this.verifier + "');";
        
        try {
            Class.forName(dbClass);
			connection = this.serverHanlder.getConnection();
            Statement statement = connection.createStatement();
            statement.executeUpdate(query);
            connection.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
			e.printStackTrace();
		}
    }
}
