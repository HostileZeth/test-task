package com.haulmont.testtask.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.haulmont.testtask.entity.Patient;



public class TestDbService {
	
	private String db = "jdbc:hsqldb:file:local-db/access";
	private String user = "sa";
	private String password = "";
	
	private String[] patientProps = {"id", "first_name", "last_name", "patronymic", "phone_number"};
	
	private Connection getConnection() throws SQLException
	{
		return DriverManager.getConnection(db, user, password);
	}
	
	public List<Patient> getPatientList()
	{
    	ArrayList<Patient> resultList = new ArrayList<>();
    	   	
    	Connection conn = null;
    	
    	try {
			conn = getConnection();
			
			Statement statement = conn.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT id, first_name, last_name, patronymic, phone_number FROM patient");
			
			while (resultSet.next())
			{
				System.out.println("READING " + resultSet.getString("first_name") + resultSet.getString("last_name"));
				
				resultList.add(
						new Patient(resultSet.getLong("id"),
							resultSet.getString("first_name"),
							resultSet.getString("last_name"),
							resultSet.getString("patronymic"),
							resultSet.getString("phone_number")));
			}
			
		} catch (SQLException e) {
			System.err.println("Failed to retrieve patients list");
			e.printStackTrace();
		}
    	
    	System.out.println(resultList);
    	
    	return resultList;
	}

	public void testDbAccess()
	{
		Connection conn = null;
		//jdbc:hsqldb:D:\EclipseWorkspace\Haulmont test-task\test-task\local-db\access
		//it works
        //String db = "jdbc:hsqldb:file:D:\\EclipseWorkspace\\Haulmont test-task\\test-task\\local-db\\access";
		
                
        try {
            conn = getConnection();
             
            // Create and execute statement
            Statement stmt = conn.createStatement();
            ResultSet rs =  stmt.executeQuery("select FIRSTNAME, LASTNAME from CUSTOMER");
            //ResultSet rs =  stmt.executeQuery("select FIRSTNAME, LASTNAME from PUBLIC.CUSTOMER");
            //ResultSet rs =  stmt.executeQuery("select FIRSTNAME, LASTNAME from PUBLIC.PUBLIC.CUSTOMER");
             
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
