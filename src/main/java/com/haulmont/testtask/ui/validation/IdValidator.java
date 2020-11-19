package com.haulmont.testtask.ui.validation;

import com.vaadin.data.ValidationResult;
import com.vaadin.data.Validator;
import com.vaadin.data.ValueContext;

public class IdValidator implements Validator<String> {
	protected static final String PHONE_REGEXP = "[0-9]+";
	protected static final boolean IS_EMPTY_ALLOWED = true;
	@Override
    public ValidationResult apply(String value, ValueContext context) {
        if (value.length() == 0)
        	if (IS_EMPTY_ALLOWED) 
        		return ValidationResult.ok();
        	else return ValidationResult.error("Значение не должно быть пустым");
        if (value.matches(PHONE_REGEXP)) 
        	return ValidationResult.ok();
        else
        	return ValidationResult.error("Id может содержать только цифры");
    }
}
