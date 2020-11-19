package com.haulmont.testtask.ui.validation;

import com.vaadin.data.ValidationResult;
import com.vaadin.data.Validator;
import com.vaadin.data.ValueContext;
import com.vaadin.server.UserError;
import com.vaadin.ui.AbstractField;

/*
 * https://vaadin.com/forum/thread/15426235/vaadin8-field-validation-without-binders
 * Utility class for adding validators to textfields
 * @author: Alejandro Duarte
 */

public class ValidatorAdder {
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void addValidator(AbstractField field, Validator validator) {
        field.addValueChangeListener(event -> {
            ValidationResult result = validator.apply(event.getValue(), new ValueContext(field));
            if (result.isError()) {
                UserError error = new UserError(result.getErrorMessage());
                field.setComponentError(error);
            } else {
                field.setComponentError(null);
            }
        });
    }
}
