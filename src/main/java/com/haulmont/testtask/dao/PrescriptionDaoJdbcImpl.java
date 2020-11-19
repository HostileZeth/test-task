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
import com.haulmont.testtask.entity.Patient;
import com.haulmont.testtask.entity.Prescription;
import com.haulmont.testtask.entity.enumeration.Priority;
import com.haulmont.testtask.service.DateConverterService;

public class PrescriptionDaoJdbcImpl implements PrescriptionDao {

	private DoctorDao doctorDao;
	private PatientDao patientDao;
		
	public PrescriptionDaoJdbcImpl(DoctorDao doctorDao, PatientDao patientDao) {
		this.doctorDao = doctorDao;
		this.patientDao = patientDao;
	}

	@Override
	public List<Prescription> getPrescriptionList() throws SQLException {	
		Connection conn = ConnectionFactory.getInstance().getConnection();
		PreparedStatement preparedStatement = conn.prepareStatement("SELECT id, doctor_id, patient_id, description, date, expiration_date, priority FROM prescription");
		List<Prescription> resultList = getResultingPrescriptionList(preparedStatement);
		conn.commit();
    	return resultList;
	}

	@Override
	public Prescription getPrescription(long id) throws SQLException {
		Connection conn = ConnectionFactory.getInstance().getConnection();
		PreparedStatement preparedStatement = conn.prepareStatement("SELECT id, doctor_id, patient_id, description, date, expiration_date, priority FROM prescription WHERE id = ?");
    	preparedStatement.setLong(1, id);
    	List<Prescription> resultList = getResultingPrescriptionList(preparedStatement);
    	conn.commit();
    	
    	if (resultList.size() == 1) 
    		return resultList.get(0);
		else 
			throw new SQLException("Result for getPrescription(long id) is not single for id = " + id);
	}

	@Override
	public List<Prescription> getFilteredPrescriptionList(String patient_id, String filter, Priority priority) throws SQLException {
		Connection conn = ConnectionFactory.getInstance().getConnection();
		
		boolean notFirst = false;
		String statement = "SELECT id, doctor_id, patient_id, description, date, expiration_date, priority FROM prescription WHERE";
		
		if (!patient_id.contentEquals("")) {
			statement += " patient_id = ?"; 
			notFirst = true;
		}
		if (!filter.contentEquals("")) {
			if (notFirst) statement += " AND ";
			statement += " UPPER(description) LIKE UPPER(?) ";
			notFirst = true;
		}
		if (priority != null) {
			if (notFirst) statement += " AND ";
			statement += " priority = ?";
		}
		
		int propNumber = 1;
		PreparedStatement preparedStatement = conn.prepareStatement(statement);
		if (!patient_id.contentEquals("")) {
			preparedStatement.setString(propNumber, patient_id);
			propNumber++;
		}
		if (!filter.contentEquals("")) {
			preparedStatement.setString(propNumber, "%"+filter+"%");
			propNumber++;
		}
		if (priority != null)
			preparedStatement.setString(propNumber, priority.toString());
		
		List<Prescription>resultList = getResultingPrescriptionList(preparedStatement);
		conn.commit();
		return resultList;
	}
	
	@Override
	public boolean deletePrescription(long id) {
		int rowsAffected = 0;
		try (Connection conn = ConnectionFactory.getInstance().getConnection())  
    	{    		
    		PreparedStatement statement = conn.prepareStatement("DELETE FROM prescription WHERE id = ?");
    		statement.setLong(1, id);
    		rowsAffected = statement.executeUpdate();
		} catch (SQLException e) {
			System.err.println("Failed to delete prescription with id = " + id);
			return false;
		}
		
		if (rowsAffected == 1) 
			return true;
		else 
			return false;
	}

	@Override
	public boolean updatePrescription(Prescription prescription) throws SQLException {
		Connection conn = ConnectionFactory.getInstance().getConnection();
		DateConverterService converterInstance = DateConverterService.instanceOf();
		PreparedStatement statement = conn.prepareStatement("UPDATE prescription SET doctor_id = ?, patient_id = ?, description = ?,"
				+ " date = ?, expiration_date = ?, priority = ? WHERE id = ?");
		
		String dateString = converterInstance.dateToDbString(prescription.getDate());
		String expirationDateString = converterInstance.dateToDbString(prescription.getExpirationDate());
		
		statement.setLong(1, prescription.getDoctor().getId());
		statement.setLong(2, prescription.getPatient().getId());
		statement.setString(3, prescription.getDescription());
		statement.setString(4, dateString);
		statement.setString(5, expirationDateString);
		statement.setString(6, prescription.getPriority().toString());
		statement.setLong(7, prescription.getId());
		
		int affectedRows = statement.executeUpdate();
		conn.commit();
		
		if (affectedRows!=1) {
			throw new SQLException("Updating patient failed - Affected more than 1 row.");
		}
		
		return true;
	}

	@Override
	public long savePrescription(Prescription prescription) throws SQLException {
		Connection conn = ConnectionFactory.getInstance().getConnection();
		DateConverterService converterInstance = DateConverterService.instanceOf();
		
		PreparedStatement statement = conn.prepareStatement("INSERT INTO prescription (doctor_id, patient_id, description, date, expiration_date, priority)"
				+ "VALUES (?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
		
		String dateString = converterInstance.dateToDbString(prescription.getDate());
		String expirationDateString = converterInstance.dateToDbString(prescription.getExpirationDate());
		statement.setLong(1, prescription.getDoctor().getId());
		statement.setLong(2, prescription.getPatient().getId());
		statement.setString(3, prescription.getDescription());
		statement.setString(4, dateString);
		statement.setString(5, expirationDateString);
		statement.setString(6, prescription.getPriority().toString());
		
		int affectedRows = statement.executeUpdate();
		conn.commit();
		
		if (affectedRows!=1) {
			throw new SQLException("Saving prescription failed - Affected more than 1 row.");
		}
		
		ResultSet generatedKeys = statement.getGeneratedKeys();
		
		if (generatedKeys.next()) 
			return generatedKeys.getLong(1);
		else {
            throw new SQLException("Saving prescription failed, no ID obtained.");
        }
	}
	
	private List<Prescription> getResultingPrescriptionList(PreparedStatement preparedStatement) throws SQLException {
		ArrayList<Prescription> resultList = new ArrayList<>();
		try {
			ResultSet resultSet = preparedStatement.executeQuery();
			
			while (resultSet.next()) {
				System.out.println("READING " + resultSet.getLong("doctor_id") + resultSet.getLong("patient_id"));
				try {
					resultList.add(createPrescription(resultSet));
				}
				catch (SQLException e) {
					System.out.println("Failed to fetch data for Prescription; Data integrity failed");
					throw new SQLException("Failed to fetch data for Prescription; Data integrity failed");
				}
			}
			
		} catch (SQLException e) {
			throw e;
		}
		
		return resultList;
	}
	
	private Prescription createPrescription(ResultSet resultSet) throws SQLException {
		Doctor thisDoctor = doctorDao.getDoctor(resultSet.getLong("doctor_id"));
		Patient thisPatient = patientDao.getPatient(resultSet.getLong("patient_id"));
		Prescription prescription = new Prescription(resultSet.getLong("id"), thisDoctor, thisPatient, 
				resultSet.getString("description"), resultSet.getDate("date"), resultSet.getDate("expiration_date"), 
				Priority.valueOf(resultSet.getString("priority")));
		
		return prescription;
	}
}
