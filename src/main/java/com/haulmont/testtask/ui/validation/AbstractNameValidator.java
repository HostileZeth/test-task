package com.haulmont.testtask.ui.validation;

import com.vaadin.data.ValidationResult;
import com.vaadin.data.Validator;
/*
 * Checks if value can be treated as capitalized "Name" or "Double-Name"/"Double Name" separated with space or hyphen
 */
public abstract class AbstractNameValidator implements Validator<String> {
	protected static final String NAMES_REGEXP = "[А-ЯЁ][а-яё]+";	
	protected static final int MINIMAL_LENGTH = 2;
	protected static final int MAXIMAL_LENGTH = 15;
	
	protected static ValidationResult checkName(String value) {
    	if (value.length() < 2 || value.length() > 15) return ValidationResult.error("Введите строку (2-15 символов)");   
    	if (value.charAt(0) == ' ' || value.charAt(value.length() - 1) == ' ' ||
    			value.charAt(0) == '-' || value.charAt(value.length() - 1) == '-')
    		return ValidationResult.error("Не допускается ведущих и заверщающих пробелов и чёрточек -");   
    	   
    	String[] names = value.split("[ -]"); //split on space or '-'; should contain strictly 1 or 2 strings in case of correct input
    	if (names.length > 2) return ValidationResult.error("Допускается не более одного разделительного пробела или чёрточки");
    	if (names.length == 0) return ValidationResult.error("Допускаются только кириллические символы, начинающиеся с заглавной буквы");
    	System.out.println(names[0]);
    	   
    	if (names.length == 2)
    		System.out.println(names[1]);
    	   
    	if (!names[0].matches(NAMES_REGEXP))
    		return ValidationResult.error("Допускаются только кириллические символы, начинающиеся с заглавной буквы");
    	   
    	if (names.length == 2)
    		if (!names[1].matches(NAMES_REGEXP))
    			return ValidationResult.error("Допускаются только кириллические символы, начинающиеся с заглавной буквы");
    		   
    	return ValidationResult.ok();
    }    
}
