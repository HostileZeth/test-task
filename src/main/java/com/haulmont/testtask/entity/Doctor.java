package com.haulmont.testtask.entity;

public class Doctor implements HasId {
	
	private long id;
	
	private String firstName;
	private String lastName;
	private String patronymic;
	
	private String specialization;
	
	public Doctor(long id, String firstName, String lastName, String patronymic, String specialization) {
		this(firstName, lastName, patronymic, specialization);
		this.id = id;
	}
	
	public Doctor(String firstName, String lastName, String patronymic, String specialization) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.patronymic = patronymic;
		this.specialization = specialization;
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getSpecialization() {
		return specialization;
	}
	public void setSpecialization(String specialization) {
		this.specialization = specialization;
	}
	public String getPatronymic() {
		return patronymic;
	}
	public void setPatronymic(String patronymic) {
		this.patronymic = patronymic;
	}
	
	@Override
	public String toString() {
		return "Patient [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", specialization="
				+ specialization + "]";
	}
}
