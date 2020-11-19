package com.haulmont.testtask.ui.validation;

import com.vaadin.data.ValidationResult;
import com.vaadin.data.Validator;
import com.vaadin.data.ValueContext;

public class NonEmptyStringValidator implements Validator<String> {
	@Override
    public ValidationResult apply(String value, ValueContext context) {
    	if (value.length() == 0) 
    		return ValidationResult.error("Поле не может быть пустым"); 
        return ValidationResult.ok();
	}
}
