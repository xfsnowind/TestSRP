package com.nesstar.resources;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.sql.Connection;
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
import com.nesstar.common.ServerHandler;
import com.nesstar.reprensentation.Authentication;
import com.nimbusds.srp6.Hex;
import com.nimbusds.srp6.SRP6CryptoParams;
import com.nimbusds.srp6.SRP6Routines;

@Path("auth")
@Produces(MediaType.APPLICATION_JSON)
public class AuthenticationResource {
	private Authentication authentication = null;
	private SRP6CryptoParams config = null;
	private MessageDigest digest = null;
	private ServerHandler serverHanlder;
	
	public AuthenticationResource(ServerHandler serverHandler) {
		this.serverHanlder = serverHandler;
		this.authentication = new Authentication();
		this.config = SRP6CryptoParams.getInstance(1024, "SHA-1");
		this.digest = this.config.getMessageDigestInstance();
	}
	
	@Path("step1")
	@GET
    @Timed
    public Object step1(@QueryParam("username") String username, @QueryParam("A") String A) {
		HashMap<String, Object> result = new LinkedHashMap<String, Object>();
        String dbClass = "com.mysql.jdbc.Driver";
        String query = "select salt, convert(verifier using utf8) as verifier from SRP.User where username='" + username + "';";
        String salt = null;
        String verifier = null;
        Connection connection;
        try {

            Class.forName(dbClass);
            connection = this.serverHanlder.getConnection();
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
        } catch (IOException e) {
			e.printStackTrace();
		}
        
		// Retrieve user verifier 'v' from database
		BigInteger v = Hex.decodeToBigInteger(verifier);
		BigInteger bigIntegerA = Hex.decodeToBigInteger(A);
		

		// Compute the public server value 'B'
		BigInteger k = SRP6Routines.computeK(this.digest, this.config.N, config.g);
		this.digest.reset();
		
		SecureRandom random = new SecureRandom();
		BigInteger b = SRP6Routines.generatePrivateValue(this.digest, this.config.N, random);
		this.digest.reset();
		
		BigInteger B = SRP6Routines.computePublicServerValue(this.config.N, this.config.g, k, v, b);
		
		BigInteger u = SRP6Routines.computeU(this.digest, this.config.N, bigIntegerA, B);
		this.authentication.setU(u);
		this.authentication.setB(b);
		this.authentication.setV(v);
		this.authentication.setA(bigIntegerA);
		
		result.put("B", Hex.encode(B));
		result.put("salt", salt);

		return result;
	}
	
	@Path("step2")
	@GET
	@Timed
	public Object step2(@QueryParam("S") String Sclient) {
		BigInteger Sserver = null;
		HashMap<String, Object> result = new LinkedHashMap<String, Object>();

		Sserver = SRP6Routines.computeSessionKey(this.config.N, this.authentication.getV(), this.authentication.getU(), this.authentication.getA(), this.authentication.getB());
		
		if (Sserver.toString(16).equals(Sclient)) {
			result.put("auth", true);
		} else {
			result.put("auth", false);
		}
		return result;
	}
}
