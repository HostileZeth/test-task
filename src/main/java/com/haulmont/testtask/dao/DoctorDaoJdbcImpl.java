package com.haulmont.testtask.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.haulmont.testtask.connection.ConnectionFactory;
import com.haulmont.testtask.entity.Doctor;
import com.haulmont.testtask.entity.DoctorStatEntity;

public class DoctorDaoJdbcImpl implements DoctorDao {

	@Override
	public List<Doctor> getDoctorList() throws SQLException {
		Connection conn = ConnectionFactory.getInstance().getConnection();
		PreparedStatement preparedStatement = conn.prepareStatement("SELECT id, first_name, last_name, patronymic, specialization FROM doctor");
		List<Doctor> resultList = getResultingDoctorList(preparedStatement);	
		conn.commit();
		return resultList;
	}

	@Override
	public Doctor getDoctor(long id) throws SQLException {
		Connection conn = ConnectionFactory.getInstance().getConnection();
		PreparedStatement preparedStatement = conn.prepareStatement("SELECT id, first_name, last_name, patronymic, specialization FROM doctor WHERE id = ?");
		preparedStatement.setLong(1, id);
		List<Doctor> resultList = getResultingDoctorList(preparedStatement);
		conn.commit();
    	if (resultList.size() == 1) return resultList.get(0);
    		else throw new SQLException("Result for getDoctor(long id) is not single for id = " + id);
	}
	
	public boolean deleteDoctor(long id) {		
		int rowsAffected = 0;
		try (Connection conn = ConnectionFactory.getInstance().getConnection())  
    	{    		
    		PreparedStatement statement = conn.prepareStatement("DELETE FROM doctor WHERE id = ?");
    		statement.setLong(1, id);
    		rowsAffected = statement.executeUpdate();
		} catch (SQLException e) {
			System.err.println("Failed to delete doctor with id = " + id);
			return false;
		}
		if (rowsAffected == 1) return true;
		else return false;
	}
	
	@Override
	public long saveDoctor(Doctor doctor) throws SQLException {
		Connection conn = ConnectionFactory.getInstance().getConnection();
		PreparedStatement statement = conn.prepareStatement("INSERT INTO doctor (first_name, last_name, patronymic, specialization) "
				+ "VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
		statement.setString(1, doctor.getFirstName());
		statement.setString(2, doctor.getLastName());
		statement.setString(3, doctor.getPatronymic());
		statement.setString(4, doctor.getSpecialization());
		
		int affectedRows = statement.executeUpdate();
		if (affectedRows!=1) {
			throw new SQLException("Saving doctor failed - Affected more than 1 row.");
		}
		
		ResultSet generatedKeys = statement.getGeneratedKeys();
		conn.commit();
		
		if (generatedKeys.next()) return generatedKeys.getLong(1);
			else {
                throw new SQLException("Saving Doctor failed, no ID obtained.");
            }
	}
	
	@Override
	public boolean updateDoctor(Doctor doctor) throws SQLException {
		Connection conn = ConnectionFactory.getInstance().getConnection();
		PreparedStatement statement = conn.prepareStatement("UPDATE doctor SET first_name = ?, last_name = ?, patronymic = ?, specialization = ? WHERE id = ?");
		statement.setString(1, doctor.getFirstName());
		statement.setString(2, doctor.getLastName());
		statement.setString(3, doctor.getPatronymic());
		statement.setString(4, doctor.getSpecialization());
		statement.setLong(5, doctor.getId());
		
		int affectedRows = statement.executeUpdate();
		conn.commit();
		if (affectedRows!=1) {
			throw new SQLException("Updating doctor failed - Affected more than 1 row.");
		}
		return true;
	}
	
	public List<DoctorStatEntity> getDoctorStats() throws SQLException {
		ArrayList<DoctorStatEntity> result = new ArrayList<DoctorStatEntity>();
		Connection conn = ConnectionFactory.getInstance().getConnection();
		
		PreparedStatement statement = conn.prepareStatement("SELECT id, CONCAT(last_name, ' ', first_name, ' ', patronymic) as doctor, COUNT(id) as recipes " + 
				"from doctor JOIN prescription ON doctor.id = prescription.doctor_id " + 
				"GROUP BY id ORDER BY recipes DESC;");
		ResultSet resultSet = statement.executeQuery();
		
		while (resultSet.next()) {
			System.out.println("READING " + resultSet.getString("doctor") + resultSet.getString("recipes"));
			try {
				result.add(new DoctorStatEntity(resultSet.getLong("id"), resultSet.getString("doctor"), resultSet.getLong("recipes")));
			}
			catch (SQLException e) {
				System.out.println("Failed to fetch data for doctor stats.");
				throw new SQLException("Failed to fetch data for doctor stats.");
			}
		}
		conn.commit();
		return result;
	}
	
	private List<Doctor> getResultingDoctorList(PreparedStatement preparedStatement) throws SQLException {
		ArrayList<Doctor> resultList = new ArrayList<>();
		try {
			ResultSet resultSet = preparedStatement.executeQuery();
			
			while (resultSet.next()) {
				System.out.println("READING " + resultSet.getString("first_name") + resultSet.getString("last_name"));
				try {
					resultList.add(createDoctorEntity(resultSet));
				}
				catch (SQLException e) {
					System.out.println("Failed to fetch data for Doctor.");
					throw new SQLException("Failed to fetch data for Doctor.");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultList;
	}

	private Doctor createDoctorEntity(ResultSet resultSet) throws SQLException {
		return new Doctor(resultSet.getLong("id"), resultSet.getString("first_name"), resultSet.getString("last_name"), resultSet.getString("patronymic"),
				resultSet.getString("specialization"));
	}
}