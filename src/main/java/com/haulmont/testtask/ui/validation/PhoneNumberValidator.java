package com.haulmont.testtask.ui.validation;

import com.vaadin.data.ValidationResult;
import com.vaadin.data.Validator;
import com.vaadin.data.ValueContext;

public class PhoneNumberValidator implements Validator<String> {
	protected static final String PHONE_REGEXP = "\\+?[0-9]+";
	protected static final int MINIMAL_LENGTH = 4;
	protected static final int MAXIMAL_LENGTH = 16;
	@Override
    public ValidationResult apply(String value, ValueContext context) {
        if (value.length() < MINIMAL_LENGTH) 
        	return ValidationResult.error("Введите номер телефона");
        if (value.length() > MAXIMAL_LENGTH) 
        	return ValidationResult.error("Введите не более 16 символов включая +");
        if (value.matches(PHONE_REGEXP)) 
        	return ValidationResult.ok();
        else 
        	return ValidationResult.error("Допускается ведущий + и подряд идущие цифры номера телефона");
    }
}
