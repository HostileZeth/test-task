package com.haulmont.testtask.ui.validation;

import java.text.ParseException;
import java.util.Date;

import com.haulmont.testtask.service.DateConverterService;
import com.vaadin.data.ValidationResult;
import com.vaadin.data.Validator;
import com.vaadin.data.ValueContext;

public class DatePresentationValidator implements Validator<String> {
	DateConverterService dateConverterService = DateConverterService.instanceOf();
	//DD-MM-YYYY date format validator
	@Override
    public ValidationResult apply(String value, ValueContext context) {
    	if (value.length() == 0) 
    		return ValidationResult.error("Поле не может быть пустым");
    	if (value.length() != 10) 
    		return ValidationResult.error("Введите дату в формате ДД-ММ-ГГГГ"); //length should be equal 10 because of DD-MM-YYYY
    	try {
			Date date = dateConverterService.presentationStringToDate(value);
			String value2 = dateConverterService.dateToPresentationString(date);
			if (value.contentEquals(value2)) 
				return ValidationResult.ok();
			else 
				return ValidationResult.error("Введите дату в формате ДД-ММ-ГГГГ");
		} catch (ParseException e) {
			e.printStackTrace();
			return ValidationResult.error("Введите дату в формате ДД-ММ-ГГГГ");
		}
    }

}
