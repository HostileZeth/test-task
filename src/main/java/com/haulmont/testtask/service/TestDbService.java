package com.haulmont.testtask.service;

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
	
	private String db = "jdbc:hsqldb:file:local-db/access";
	private String user = "sa";
	private String password = "";
	
	private PatientDao patientDao;
	private DoctorDao doctorDao;
	private PrescriptionDao prescriptionDao;
	
	public TestDbService()
	{
		patientDao = new PatientDaoJdbcImpl();
		doctorDao = new DoctorDaoJdbcImpl();
		prescriptionDao = new PrescriptionDaoJdbcImpl(doctorDao, patientDao);
	}	
	
	public List<Patient> getPatientList()
	{
    	return patientDao.getPatientList();
	}
	
	public Patient getPatient (long id)
	{
		try {
			return patientDao.getPatient(id);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("Db Service: failed to retrieve patient" + id);
			e.printStackTrace();
		}
		
		return null;
	}

	public List<Doctor> getDoctorList() {
    	return doctorDao.getDoctorList();
	}
	
	public Doctor getDoctor (long id)
	{
		try {
			return doctorDao.getDoctor(id);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("Db Service: failed to retrieve doctor " + id);
			e.printStackTrace();
		}
		
		return null;
	}

	public List<Prescription> getPrescriptionList() {
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
	
	public List<Prescription> getFilteredPrescriptionList(String filter) {
		// TODO Auto-generated method stub
		try {
			return prescriptionDao.getFilteredPrescriptionList(filter);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("Db Service: failed to retrieve prescription list; Data integrity violated");
			e.printStackTrace();
		}
		
		return null;
	}

	public Prescription getPrescription(long id) {
		
		try {
			return prescriptionDao.getPrescription(id);
		} catch (Exception e) {
			System.out.println("DbService: Failed to retrieve prescription " + id);
			e.printStackTrace();
		}
		
		return null;
	}
	

	/*public void testDbAccess()
	{
		Connection conn = null;
		//jdbc:hsqldb:D:\EclipseWorkspace\Haulmont test-task\test-task\local-db\access
		//it works
        //String db = "jdbc:hsqldb:file:D:\\EclipseWorkspace\\Haulmont test-task\\test-task\\local-db\\access";
		
                
        try {
            conn = getConnection();
             
            // Create and execute statement
            Statement stmt = conn.createStatement();
            ResultSet rs =  stmt.executeQuery("select FIRSTNAME, LASTNAME from CUSTOMER");
            //ResultSet rs =  stmt.executeQuery("select FIRSTNAME, LASTNAME from PUBLIC.CUSTOMER");
            //ResultSet rs =  stmt.executeQuery("select FIRSTNAME, LASTNAME from PUBLIC.PUBLIC.CUSTOMER");
             
            // Loop through the data and print all artist names
            while(rs.next()) {
                System.out.println("Customer Name: " + rs.getString("FIRSTNAME") + " " + rs.getString("LASTNAME"));
            }
             
            // Clean up
            rs.close();
            stmt.close();
        }
        catch (SQLException e)
        {
        	System.err.println(e.getMessage());
        }
        finally {
            try {
                // Close connection
                if (conn != null) 
                    conn.close();
            }
            catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }
        
	} */
	
}
