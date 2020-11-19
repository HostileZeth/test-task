package com.haulmont.testtask.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.haulmont.testtask.connection.ConnectionFactory;
import com.haulmont.testtask.entity.Patient;

public class PatientDaoJdbcImpl implements PatientDao {
	public PatientDaoJdbcImpl() {}
	
	@Override
	public List<Patient> getPatientList() throws SQLException {		
		Connection conn = ConnectionFactory.getInstance().getConnection();
		PreparedStatement statement = conn.prepareStatement("SELECT id, first_name, last_name, patronymic, phone_number FROM patient");
		List<Patient> resultList = getResultingPatientList(statement);
		conn.commit();
		return resultList;
	}
	
	@Override
	public Patient getPatient(long id) throws SQLException {
		Connection conn = ConnectionFactory.getInstance().getConnection();
		PreparedStatement statement = conn.prepareStatement("SELECT id, first_name, last_name, patronymic, phone_number FROM patient WHERE id = ?");
		statement.setLong(1, id);
		List<Patient> resultList = getResultingPatientList(statement);	
		conn.commit();
    	if (resultList.size() == 1)
    		return resultList.get(0);
    	else 
    		throw new SQLException("Result for getPatient(long id) is not single for id = " + id);
	}
	
	public boolean deletePatient(long id) {		
		int rowsAffected = 0;
		try (Connection conn = ConnectionFactory.getInstance().getConnection())	{    		
    		PreparedStatement statement = conn.prepareStatement("DELETE FROM PATIENT WHERE id = ?");
    		statement.setLong(1, id);
    		rowsAffected = statement.executeUpdate();
		} catch (SQLException e) {
			System.err.println("Failed to delete patient with id = " + id);
			return false;
		}
		if (rowsAffected == 1) 
			return true;
		else 
			return false;
	}

	@Override
	public long savePatient(Patient patient) throws SQLException {
		Connection conn = ConnectionFactory.getInstance().getConnection();
		PreparedStatement statement = conn.prepareStatement("INSERT INTO patient (first_name, last_name, patronymic, phone_number) "
				+ "VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
		statement.setString(1, patient.getFirstName());
		statement.setString(2, patient.getLastName());
		statement.setString(3, patient.getPatronymic());
		statement.setString(4, patient.getPhoneNumber());
		
		int affectedRows = statement.executeUpdate();
		if (affectedRows!=1)
			throw new SQLException("Saving patient failed - Affected more than 1 row.");
		
		ResultSet generatedKeys = statement.getGeneratedKeys();
		conn.commit();
		
		if (generatedKeys.next()) 
			return generatedKeys.getLong(1);
		else
            throw new SQLException("Saving patient failed, no ID obtained.");
	}

	@Override
	public boolean updatePatient(Patient patient) throws SQLException {
		Connection conn = ConnectionFactory.getInstance().getConnection();
		PreparedStatement statement = conn.prepareStatement("UPDATE patient SET first_name = ?, last_name = ?, patronymic = ?, phone_number = ? WHERE id = ?");
		statement.setString(1, patient.getFirstName());
		statement.setString(2, patient.getLastName());
		statement.setString(3, patient.getPatronymic());
		statement.setString(4, patient.getPhoneNumber());
		statement.setLong(5, patient.getId());
		int affectedRows = statement.executeUpdate();
		
		if (affectedRows!=1)
			throw new SQLException("Updating patient failed - Affected more than 1 row.");
		
		conn.commit();
		return true;
	}
	
	private List<Patient> getResultingPatientList(PreparedStatement preparedStatement) throws SQLException {
		ArrayList<Patient> resultList = new ArrayList<>();
		try {
			ResultSet resultSet = preparedStatement.executeQuery();
			
			while (resultSet.next()) {
				System.out.println("READING " + resultSet.getString("first_name") + resultSet.getString("last_name"));
				try {
					resultList.add(createPatient(resultSet));
				}
				catch (SQLException e) {
					System.out.println("Failed to fetch data for Patient.");
					throw new SQLException("Failed to fetch data for Patient.");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultList;
	}
	
	private Patient createPatient(ResultSet resultSet) throws SQLException {
		return new Patient(resultSet.getLong("id"), resultSet.getString("first_name"), resultSet.getString("last_name"), resultSet.getString("patronymic"),
				resultSet.getString("phone_number"));
	}
}
