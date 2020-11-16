package com.haulmont.testtask.service;

import java.sql.SQLException;
import java.util.List;

import com.haulmont.testtask.dao.DoctorDao;
import com.haulmont.testtask.dao.DoctorDaoJdbcImpl;
import com.haulmont.testtask.dao.PatientDao;
import com.haulmont.testtask.dao.PatientDaoJdbcImpl;
import com.haulmont.testtask.dao.PrescriptionDao;
import com.haulmont.testtask.dao.PrescriptionDaoJdbcImpl;
import com.haulmont.testtask.entity.Doctor;
import com.haulmont.testtask.entity.Patient;
import com.haulmont.testtask.entity.Prescription;



public class TestDbService {
	
	private PatientDao patientDao;
	private DoctorDao doctorDao;
	private PrescriptionDao prescriptionDao;
	
	static TestDbService testDbService;
	
	public static TestDbService instanceOf() {
		if (testDbService == null) testDbService = new TestDbService();
		return testDbService;
	}
	
	private TestDbService() {
		patientDao = new PatientDaoJdbcImpl();
		doctorDao = new DoctorDaoJdbcImpl();
		prescriptionDao = new PrescriptionDaoJdbcImpl(doctorDao, patientDao);
	}	
	
	public List<Patient> getPatientList() throws SQLException {
    	return patientDao.getPatientList();
	}
	
	public Patient getPatient (long id) throws SQLException {
		return patientDao.getPatient(id);
	}
	
	public boolean deletePatient(long id) {
		return patientDao.deletePatient(id);
	}

	public List<Doctor> getDoctorList() throws SQLException {
    	return doctorDao.getDoctorList();
	}
	
	public Doctor getDoctor (long id) throws SQLException 
	{
			return doctorDao.getDoctor(id);
	}

	public boolean deleteDoctor(long id) {
		return doctorDao.deleteDoctor(id);
	}

	public List<Prescription> getPrescriptionList() throws SQLException {
		// TODO Auto-generated method stub
		try {
			return prescriptionDao.getPrescriptionList();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("Db Service: failed to retrieve prescription list; Data integrity violated");
			e.printStackTrace();
		}
		
		return null;
	}
	
	public List<Prescription> getFilteredPrescriptionList(String filter) throws SQLException {
		return prescriptionDao.getFilteredPrescriptionList(filter);
	}

	public Prescription getPrescription(long id) throws SQLException {
		return prescriptionDao.getPrescription(id);
	}
	
	public boolean deletePrescription(long id)
	{
		return prescriptionDao.deletePrescription(id);
	}
		
}
