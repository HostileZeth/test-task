package com.haulmont.testtask.entity;

import java.util.Date;

import com.haulmont.testtask.entity.enumeration.Priority;

public class Prescription {
	
	private long id;

	private Doctor doctor;
	private Patient patient;
	
	private String description;
	private Date date;
	private int duration;
	
	private Priority priority;
	
}
