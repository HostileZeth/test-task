package com.haulmont.testtask.ui;

import java.util.ArrayList;
import java.util.List;

import com.haulmont.testtask.service.DbService;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/*
 * Contains generalized edit dialog window preferences and methods for TextField manipulation and validation. 
 */

public abstract class AbstractEditDialog extends Window {
	protected static final String WINDOW_WIDTH = "30%";
	protected static final String WINDOW_HEIGHT = "75%";
	protected static final String TEXTFIELD_WIDTH_PERCENT = "80%";
	protected DbService testDbService;
	
	protected Label info;
	protected VerticalLayout layout;
	protected HorizontalLayout buttonLayout;
	protected List<TextField> inputFieldList; //container for text fields to check them and disable them at once
	
	protected Button applyButton;
	protected Button cancelButton;
	
	protected AbstractEditDialog(String title) {
		super(title);
		testDbService = DbService.instanceOf();
		inputFieldList = new ArrayList<>();
		setHeight(WINDOW_HEIGHT);
		setWidth(WINDOW_WIDTH);
		setPositionX(200);
		setPositionY(200);
		setResizable(false);
		
		info = new Label();
		layout = new VerticalLayout();
		layout.setDefaultComponentAlignment(Alignment.TOP_CENTER);
		layout.addComponent(info);
		
		buttonLayout = new HorizontalLayout();
		buttonLayout.setDefaultComponentAlignment(Alignment.TOP_CENTER);
		
		applyButton = new Button("OK");
		cancelButton = new Button("Отмена");
		cancelButton.addClickListener(e -> close());
		setContent(layout);
	}
	//dialog fields displaying
	protected void addFieldsToLayout() {
		for (TextField tf : inputFieldList) {
			tf.setWidth(TEXTFIELD_WIDTH_PERCENT);
			layout.addComponent(tf);
		}
	}
	//blocks text fields in case of successful input
	protected void blockTextFieldsAndButtons() {
		for (TextField tf : inputFieldList)
			tf.setEnabled(false);
		applyButton.setEnabled(false);
		cancelButton.setEnabled(false);
	}
	//checks if there errors on textfields
	protected boolean isFieldsCorrect() {
		for (TextField tf : inputFieldList)
			if (tf.getComponentError() != null)
				return false;
		return true;
	}
	
	protected void showMessage(String message) {
    	info.setCaption(message);
    }
	
    protected void showAcceptedMessage(String message) {
    	info.setIcon(VaadinIcons.CHECK);
    	info.setCaption(message);
    }
    
    protected void showErrorMessage(String message) {
    	info.setIcon(VaadinIcons.BAN);
    	info.setCaption(message);
    }
    
    
}
