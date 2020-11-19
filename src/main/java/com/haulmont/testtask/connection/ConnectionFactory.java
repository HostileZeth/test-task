package com.haulmont.testtask.connection;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionFactory {
	
	private String db;
	private String user;
	private String password;
	
	private static ConnectionFactory instance;
	
	private ConnectionFactory() {
		Properties properties = new Properties();
		InputStream inputStream = getClass().getClassLoader().getResourceAsStream("connection.properties");
		try {
			properties.load(inputStream);
		} catch (IOException e) {
			System.out.println("Cannot read properties from connection.properties file. ConnectionFactory not initialized.");
			e.printStackTrace();
		}
		
		db = properties.getProperty("hqsqldb.database");
		user = properties.getProperty("hqsqldb.username");
		password = properties.getProperty("hqsqldb.password");
	}
	
	public static ConnectionFactory getInstance() {
		if (instance == null) instance = new ConnectionFactory();
		return instance;
	}
	
	public Connection getConnection() {
		Connection conn = null;
		try	{
			conn = DriverManager.getConnection(db, user, password);
		}
		catch (SQLException e) {
			System.out.println("Database connection failed. Press any key to retry or close the app.");
			try {
				System.in.read();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			return getConnection();
		}
		return conn;
	}
}
