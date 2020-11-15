package com.haulmont.testtask.entity;

import java.util.Date;

import com.haulmont.testtask.entity.enumeration.Priority;

public class Prescription {
	
	private long id;

	private Doctor doctor;
	private Patient patient;
	
	private String description;
	private Date date;
	private Date expirationDate;
	
	
	
	public Prescription(long id, Doctor doctor, Patient patient, String description, Date date, Date expirationDate,
			Priority priority) {
		this.id = id;
		this.doctor = doctor;
		this.patient = patient;
		this.description = description;
		this.date = date;
		this.expirationDate = expirationDate;
		this.priority = priority;
	}

	//@Enumerated(EnumType.STRING)
	private Priority priority; 

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Doctor getDoctor() {
		return doctor;
	}

	public void setDoctor(Doctor doctor) {
		this.doctor = doctor;
	}

	public Patient getPatient() {
		return patient;
	}

	public void setPatient(Patient patient) {
		this.patient = patient;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Date getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	public Priority getPriority() {
		return priority;
	}

	public void setPriority(Priority priority) {
		this.priority = priority;
	}

	@Override
	public String toString() {
		return "Prescription [id=" + id + ", doctor=" + doctor + ", patient=" + patient + ", description=" + description
				+ ", date=" + date + ", expirationDate=" + expirationDate + ", priority=" + priority + "]";
	}
	
	
}
