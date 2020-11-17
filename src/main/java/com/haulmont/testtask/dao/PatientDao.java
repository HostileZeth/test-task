package com.haulmont.testtask.dao;

import java.sql.SQLException;
import java.util.List;

import com.haulmont.testtask.entity.Patient;

public interface PatientDao {

	List<Patient> getPatientList() throws SQLException;

	Patient getPatient(long id) throws SQLException;
	
	boolean deletePatient(long id);
	
	long savePatient(Patient patient) throws SQLException;

	boolean updatePatient(Patient patient) throws SQLException;

}