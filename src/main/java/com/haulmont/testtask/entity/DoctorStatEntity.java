package com.haulmont.testtask.entity;

public class DoctorStatEntity implements HasId {

	private long id;
	private String doctorName;
	private long recipesCount;
	
	public DoctorStatEntity(long id, String doctorName, long recipesCount) {
		this.id = id;
		this.doctorName = doctorName;
		this.recipesCount = recipesCount;
	}
	
	@Override
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public String getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}

	public long getRecipesCount() {
		return recipesCount;
	}

	public void getRecipesCount(long count) {
		this.recipesCount = count;
	}
	
	

}
