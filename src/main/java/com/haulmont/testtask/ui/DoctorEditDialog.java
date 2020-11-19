package com.haulmont.testtask.ui;

import com.haulmont.testtask.entity.Doctor;
import com.haulmont.testtask.ui.validation.NameSurnameValidator;
import com.haulmont.testtask.ui.validation.NonEmptyStringValidator;
import com.haulmont.testtask.ui.validation.PatronymicValidator;
import com.haulmont.testtask.ui.validation.ValidatorAdder;
import com.vaadin.ui.TextField;

public class DoctorEditDialog extends AbstractEditDialog {
	
	private TextField id;
	private TextField firstName;
	private TextField lastName;
	private TextField patronymic;
	private TextField specialization;
	
	private Doctor doctor;
	
	protected DoctorEditDialog() {
		super("Добавить врача");
		showMessage("Введите данные врача и нажмите кнопку ОК");
		init();
	}
	
	protected DoctorEditDialog(Doctor doctor) {
		super("Редактировать врача");
		showMessage("Измените данные врача и нажмите кнопку ОК");
		this.doctor = doctor;
		init();
		setDoctorInfo();
	}
	
	private void init() {
		id = new TextField(); 
		id.setCaption("Id"); 
		id.setReadOnly(true); 
		id.setValue("(пусто)"); 
		id.setEnabled(false);
		inputFieldList.add(id);
		
		lastName = new TextField(); 
		lastName.setCaption("Фамилия");
		lastName.setRequiredIndicatorVisible(true);
		ValidatorAdder.addValidator(lastName, new NameSurnameValidator());
		inputFieldList.add(lastName);
		
		firstName = new TextField(); 
		firstName.setCaption("Имя");		
		firstName.setRequiredIndicatorVisible(true);
		ValidatorAdder.addValidator(firstName, new NameSurnameValidator());
		inputFieldList.add(firstName);
		
		patronymic = new TextField(); 
		patronymic.setCaption("Отчество");
		ValidatorAdder.addValidator(patronymic, new PatronymicValidator());
		inputFieldList.add(patronymic);
		
		specialization = new TextField();
		specialization.setCaption("Специализация");
		specialization.setRequiredIndicatorVisible(true);
		ValidatorAdder.addValidator(specialization, new NonEmptyStringValidator());
		inputFieldList.add(specialization);
		
		addFieldsToLayout();
		
		buttonLayout.addComponent(applyButton);
		buttonLayout.addComponent(cancelButton);
		
		buttonLayout.setWidth(TEXTFIELD_WIDTH_PERCENT);
		
		applyButton.addClickListener(e -> applyButtonClicked());
		
		layout.addComponent(buttonLayout);
	}
	
	@Override
	protected boolean isFieldsCorrect() {
		//if field has ERROR mark - then false
		if (!super.isFieldsCorrect()) return false;
		//if required field is empty - then false
		if (lastName.getValue().equals("") || firstName.getValue().equals("") || specialization.getValue().equals(""))
			return false;
		
		return true; // else true
	}
	
	private void applyButtonClicked() {
		long receivedId = -1;
		boolean isUpdated = false;
		if (!isFieldsCorrect()) {
			showErrorMessage("Корректно заполните поля и повторите попытку");
			return;
		}		
		try {			
			if (doctor == null) {
				doctor = new Doctor(firstName.getValue(), lastName.getValue(), patronymic.getValue(), specialization.getValue());	
				receivedId = testDbService.saveDoctor(doctor);
			} else {
				doctor = new Doctor(doctor.getId(), firstName.getValue(), lastName.getValue(), patronymic.getValue(), specialization.getValue());
				isUpdated = testDbService.updateDoctor(doctor);
			}
		} catch (Exception e) {
			e.printStackTrace();
			showErrorMessage("Не удалось сохранить данные о докторе.");
		}
		if (receivedId!=-1) { 
			doctor.setId(receivedId);
			id.setValue(Long.toString(receivedId));
			blockTextFieldsAndButtons();
			showAcceptedMessage("Доктор успешно сохранён. \r\n Закройте окно.");
		}
		if (isUpdated) {
			blockTextFieldsAndButtons();
			showAcceptedMessage("Доктор успешно обновлён. \r\n Закройте окно.");
		}
	}
	//setting up initial info
	private void setDoctorInfo() {
		id.setValue(Long.toString(doctor.getId()));
		lastName.setValue(doctor.getLastName());
		firstName.setValue(doctor.getFirstName());
		patronymic.setValue(doctor.getPatronymic());
		specialization.setValue(doctor.getSpecialization());
	}
}
