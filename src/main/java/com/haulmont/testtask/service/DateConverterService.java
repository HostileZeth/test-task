package com.haulmont.testtask.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateConverterService {
	private static DateConverterService dateConverterService;
	
	private DateFormat dbDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	private DateFormat presentationDateFormat = new SimpleDateFormat("dd-MM-yyyy");
	
	private DateConverterService() {}
	
	public static DateConverterService instanceOf() {
		if (dateConverterService == null) dateConverterService = new DateConverterService();
		return dateConverterService;
	}
	
	public DateFormat getDbDateFormat() {
		return dbDateFormat;
	}
	
	public DateFormat getPresentationDateFormat() {
		return presentationDateFormat;
	}
	
	public String dateToPresentationString(Date date) {
		return presentationDateFormat.format(date);
	}
	
	public String dateToDbString(Date date) {
		return dbDateFormat.format(date);
	}
	
	public Date presentationStringToDate(String date) throws ParseException {
		return presentationDateFormat.parse(date);
	}
}
