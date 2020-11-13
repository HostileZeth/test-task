package com.haulmont.testtask.dao.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

	private String db = "jdbc:hsqldb:file:local-db/access";
	private String user = "sa";
	private String password = "";
	
	private static ConnectionFactory instance;
	
	private ConnectionFactory() {}
	
	public static ConnectionFactory getInstance()
	{
		if (instance == null) instance = new ConnectionFactory();
		return instance;
	}
	
	public Connection getConnection() throws SQLException
	{
		return DriverManager.getConnection(db, user, password);
	}
	
}
