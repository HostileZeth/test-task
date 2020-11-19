package com.haulmont.testtask.dao;

import java.sql.SQLException;
import java.util.List;

import com.haulmont.testtask.entity.Prescription;
import com.haulmont.testtask.entity.enumeration.Priority;

public interface PrescriptionDao {
	
	List<Prescription> getPrescriptionList() throws SQLException;
	
	List<Prescription> getFilteredPrescriptionList(String patient_id, String filter, Priority priority) throws SQLException;

	Prescription getPrescription(long id) throws SQLException;

	boolean deletePrescription(long id);

	boolean updatePrescription(Prescription prescription) throws SQLException;

	long savePrescription(Prescription prescription) throws SQLException;


}