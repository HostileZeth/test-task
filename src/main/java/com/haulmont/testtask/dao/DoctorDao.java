package com.haulmont.testtask.dao;

import java.sql.SQLException;
import java.util.List;

import com.haulmont.testtask.entity.Doctor;
import com.haulmont.testtask.entity.DoctorStatEntity;

public interface DoctorDao {

	List<Doctor> getDoctorList() throws SQLException;

	Doctor getDoctor(long id) throws SQLException;

	boolean deleteDoctor(long id);

	long saveDoctor(Doctor doctor) throws SQLException;

	boolean updateDoctor(Doctor doctor) throws SQLException;

	List<DoctorStatEntity> getDoctorStats() throws SQLException;

}