package com.nesstar.common;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public final class ServerHandler {


   private final String dbURI;
   private final String dbUsername;
   private final String dbPassword;
   private Connection connection;

   public ServerHandler(String dbURI, String dbUsername, String dbPassword) {
      this.dbURI = dbURI;
      this.dbUsername = dbUsername;
      this.dbPassword = dbPassword;
   }

   public Connection getConnection() throws IOException, SQLException {
	   connection = DriverManager.getConnection(this.dbURI, this.dbUsername, this.dbPassword);
	   return connection;
   }
}
