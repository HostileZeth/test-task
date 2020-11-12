package com.haulmont.testtask.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class TestDbService {

	public void testDbAccess()
	{
		Connection conn = null;
		//jdbc:hsqldb:D:\EclipseWorkspace\Haulmont test-task\test-task\local-db\access
		//it works
        //String db = "jdbc:hsqldb:file:D:\\EclipseWorkspace\\Haulmont test-task\\test-task\\local-db\\access";
		String db = "jdbc:hsqldb:file:local-db\\access";
        String user = "sa";
        String password = "";
        
        try {
            Class.forName("org.hsqldb.jdbc.JDBCDriver" );
        } catch (Exception e) {
            System.err.println("ERROR: failed to load HSQLDB JDBC driver.");
            e.printStackTrace();
            return;
        }
                
        System.out.println("BEFORE CONNECTION");
        
        try {
            conn = DriverManager.getConnection(db, user, password);
             
            // Create and execute statement
            Statement stmt = conn.createStatement();
//            ResultSet rs =  stmt.executeQuery("select FIRSTNAME, LASTNAME from PUBLIC.CUSTOMER");
            ResultSet rs =  stmt.executeQuery("select FIRSTNAME, LASTNAME from PUBLIC.PUBLIC.CUSTOMER");
             
            // Loop through the data and print all artist names
            while(rs.next()) {
                System.out.println("Customer Name: " + rs.getString("FIRSTNAME") + " " + rs.getString("LASTNAME"));
            }
             
            // Clean up
            rs.close();
            stmt.close();
        }
        catch (SQLException e)
        {
        	System.err.println(e.getMessage());
        }
        finally {
            try {
                // Close connection
                if (conn != null) 
                    conn.close();
            }
            catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }
        
	}
	
}
