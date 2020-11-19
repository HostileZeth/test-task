package com.haulmont.testtask.ui.validation;

import com.vaadin.data.ValidationResult;
import com.vaadin.data.ValueContext;

public class PatronymicValidator extends AbstractNameValidator {
	@Override
	public ValidationResult apply(String value, ValueContext context) {
		if (value.length() == 0) 
			return ValidationResult.ok();
        return checkName(value);
	}
}
