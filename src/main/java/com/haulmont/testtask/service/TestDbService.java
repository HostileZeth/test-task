package com.haulmont.testtask.service;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
import com.haulmont.testtask.entity.enumeration.Priority;



public class TestDbService {
	
	private PatientDao patientDao;
	private DoctorDao doctorDao;
	private PrescriptionDao prescriptionDao;
	
	private DateFormat dbDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	private DateFormat presentationDateFormat = new SimpleDateFormat("dd-MM-yyyy");
	
	static TestDbService testDbService;
	
	public static TestDbService instanceOf() {
		if (testDbService == null) testDbService = new TestDbService();
		return testDbService;
	}
	
	public DateFormat getDbDateFormat() {
		return dbDateFormat;
	}
	
	public DateFormat getPresentationDateFormat() {
		return presentationDateFormat;
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

	public long savePatient(Patient patient) throws SQLException {
		return patientDao.savePatient(patient);
	}
	
	public boolean updatePatient(Patient patient) throws SQLException {
		return patientDao.updatePatient(patient);
	}
		
	public List<Doctor> getDoctorList() throws SQLException {
    	return doctorDao.getDoctorList();
	}
	
	public Doctor getDoctor (long id) throws SQLException {
		return doctorDao.getDoctor(id);
	}

	public boolean deleteDoctor(long id) {
		return doctorDao.deleteDoctor(id);
	}

	public boolean updateDoctor(Doctor doctor) throws SQLException {
		return doctorDao.updateDoctor(doctor);
	}

	public long saveDoctor(Doctor doctor) throws SQLException {
		return doctorDao.saveDoctor(doctor);
	}

	public List<Prescription> getPrescriptionList() throws SQLException {
		// TODO Auto-generated method stub
		try {
			return prescriptionDao.getPrescriptionList();
		} catch (Exception e) {
			
			System.out.println("Db Service: failed to retrieve prescription list; Data integrity violated");
			e.printStackTrace();
			throw new SQLException("Db Service: failed to retrieve prescription list; Data integrity violated");
		}
	}
	
	public List<Prescription> getFilteredPrescriptionList(String id, String filter, Priority priority) throws SQLException {
		return prescriptionDao.getFilteredPrescriptionList(id, filter, priority);
	}

	public Prescription getPrescription(long id) throws SQLException {
		return prescriptionDao.getPrescription(id);
	}
	
	public boolean deletePrescription(long id) {
		return prescriptionDao.deletePrescription(id);
	}

	public boolean updatePrescription(Prescription prescription) throws SQLException {
		return prescriptionDao.updatePrescription(prescription);
	}
	
	public long savePrescription(Prescription prescription) throws SQLException {
		return prescriptionDao.savePrescription(prescription);
	}


		
}
