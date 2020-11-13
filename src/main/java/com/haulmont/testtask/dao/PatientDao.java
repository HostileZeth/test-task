package com.haulmont.testtask.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.haulmont.testtask.dao.connection.ConnectionFactory;
import com.haulmont.testtask.entity.Patient;

public class PatientDao {
	
	private PatientDao() {}
	
	private static PatientDao instance;
	
	public static PatientDao getInstance()
	{
		if (instance == null) instance = new PatientDao();
		return instance;		
	}
	
	public List<Patient> getPatientList()
	{
		ArrayList<Patient> resultList = new ArrayList<>();
	   	
    	Connection conn = null;
    	
    	try {
			conn = ConnectionFactory.getInstance().getConnection();
			
			Statement statement = conn.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT id, first_name, last_name, patronymic, phone_number FROM patient");
			
			while (resultSet.next())
			{
				System.out.println("READING " + resultSet.getString("first_name") + resultSet.getString("last_name"));
				resultList.add(createPatient(resultSet));
			}
			
		} catch (SQLException e) {
			System.err.println("Failed to retrieve patients list");
			e.printStackTrace();
		}
    	
    	System.out.println(resultList);
    	
    	return resultList;
	}
	
	public List<Patient> getPatient(int id)
	{
		ArrayList<Patient> resultList = new ArrayList<>();
	   	
    	Connection conn = null;
    	
    	try {
			conn = ConnectionFactory.getInstance().getConnection();
			
			Statement statement = conn.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT id, first_name, last_name, patronymic, phone_number FROM patient WHERE id = " + id);
			
			while (resultSet.next())
			{
				System.out.println("READING " + resultSet.getString("first_name") + resultSet.getString("last_name"));
				resultList.add(createPatient(resultSet));
			}
			
		} catch (SQLException e) {
			System.err.println("Failed to retrieve patients list");
			e.printStackTrace();
		}
    	
    	System.out.println(resultList);
    	
    	return resultList;
	}
	
	private Patient createPatient(ResultSet resultSet) throws SQLException
	{
		return new Patient(resultSet.getLong("id"), resultSet.getString("first_name"), resultSet.getString("last_name"), resultSet.getString("patronymic"),
				resultSet.getString("phone_number"));
	}

}
