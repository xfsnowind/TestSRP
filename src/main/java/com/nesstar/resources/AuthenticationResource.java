package com.nesstar.resources;

import java.math.BigInteger;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.LinkedHashMap;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.codahale.metrics.annotation.Timed;
import com.nesstar.reprensentation.Authentication;
import com.nimbusds.srp6.Hex;
import com.nimbusds.srp6.SRP6ClientCredentials;
import com.nimbusds.srp6.SRP6ClientSession;
import com.nimbusds.srp6.SRP6CryptoParams;
import com.nimbusds.srp6.SRP6Exception;
import com.nimbusds.srp6.SRP6ServerSession;

@Path("auth")
@Produces(MediaType.APPLICATION_JSON)
public class AuthenticationResource {
	private Authentication authentication = null;
	private SRP6CryptoParams config = null;
	private SRP6ServerSession server = null;
	
	public AuthenticationResource () {
		this.authentication = new Authentication();
		this.config = SRP6CryptoParams.getInstance();
		this.server = new SRP6ServerSession(config);
	}
	
	
	@Path("step1")
	@GET
    @Timed
    public Object step1(@QueryParam("username") String username, @QueryParam("A") String A) {
		HashMap<String, Object> result = new LinkedHashMap<String, Object>();
		String dbUrl = "jdbc:mysql://localhost/SRP";
        String dbClass = "com.mysql.jdbc.Driver";
        String query = "select salt, convert(verifier using utf8) as verifier from SRP.User where username='" + username + "';";
        String databaseUsername = "root";
        String databasePassword = "root";
        String salt = null;
        String verifier = null;
        try {

            Class.forName(dbClass);
            Connection connection = DriverManager.getConnection(dbUrl, databaseUsername, databasePassword);
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            
            // iterate through the java resultset
            while (rs.next())
            {
              salt = rs.getString("salt");
              verifier = rs.getString("verifier");
            }
            connection.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
		// Retrieve user verifier 'v' + salt 's' from database
		BigInteger v = Hex.decodeToBigInteger(verifier);
		BigInteger s = Hex.decodeToBigInteger(salt);
		this.authentication.setSalt(s);
		this.authentication.setA(Hex.decodeToBigInteger(A));

		// Compute the public server value 'B'
		BigInteger B = server.step1(username, s, v);
		System.out.println("b: " + Hex.encode(B));;
		this.authentication.setB(B);
		
		
		result.put("B", Hex.encode(B));
		result.put("salt", salt);

		return result;
	}
	
	@Path("step2")
	@GET
	@Timed
	public Object step2(@QueryParam("S") String Sclient) {
		SRP6ClientSession client = new SRP6ClientSession();
		SRP6ClientCredentials cred = null;
		BigInteger Sserver = null;
		HashMap<String, Object> result = new LinkedHashMap<String, Object>();

		try {
	        cred = client.step2(this.config, this.authentication.getSalt(), this.authentication.getB());
	        Sserver = this.server.step2(this.authentication.getA(), cred.M1);
		} catch (SRP6Exception e) {
		        // Invalid server 'B'
		}
		
		if (Sserver.toString().equals(Sclient)) {
			result.put("auth", true);
		} else {
			result.put("auth", false);
		}
		return result;
	}
}
