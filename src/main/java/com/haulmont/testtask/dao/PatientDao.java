package com.haulmont.testtask.dao;

import java.util.List;

import com.haulmont.testtask.entity.Patient;

public interface PatientDao {

	List<Patient> getPatientList();

	Patient getPatient(long id) throws Exception;

}