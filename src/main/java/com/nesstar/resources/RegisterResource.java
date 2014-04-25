package com.nesstar.resources;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.codahale.metrics.annotation.Timed;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path("/register")
@Produces(MediaType.APPLICATION_JSON)
public class RegisterResource {
	
    private String verifier;
    private String username;
    private String salt;

    @GET
    @Timed
    public void register(@QueryParam("username") String username,
    					 @QueryParam("salt") String salt,
    					 @QueryParam("verifier") String verifier) {
        this.verifier = verifier;
        this.salt = salt;
        this.username = username;
        System.out.println(this.verifier + ", " + this.salt + ", " + this.username); 
        
        
        String dbUrl = "jdbc:mysql://localhost/SRP";
        String dbClass = "com.mysql.jdbc.Driver";
        String query = "insert into SRP.User(username, salt, verifier) values('" + this.username + "','" + this.salt + "','" + this.verifier + "');";
        String databaseUsername = "root";
        String databasePassword = "root";
        try {

            Class.forName(dbClass);
            Connection connection = DriverManager.getConnection(dbUrl, databaseUsername, databasePassword);
            Statement statement = connection.createStatement();
            statement.executeUpdate(query);
            connection.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
