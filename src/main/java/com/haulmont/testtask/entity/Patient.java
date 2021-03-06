package com.haulmont.testtask.entity;

public class Patient implements HasId {
	
	private long id;
	
	private String firstName;
	private String lastName;
	private String patronymic;
	
	private String phoneNumber;
	
	public Patient(long id, String firstName, String lastName, String patronymic, String phoneNumber) {
		this(firstName, lastName, patronymic, phoneNumber);
		this.id = id;
	}
	
	public Patient(String firstName, String lastName, String patronymic, String phoneNumber) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.patronymic = patronymic;
		this.phoneNumber = phoneNumber;
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
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getPatronymic() {
		return patronymic;
	}
	public void setPatronymic(String patronymic) {
		this.patronymic = patronymic;
	}
	@Override
	public String toString() {
		return "Patient [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", phoneNumber="
				+ phoneNumber + "]";
	}

}
