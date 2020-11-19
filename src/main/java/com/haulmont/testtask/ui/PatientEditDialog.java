package com.haulmont.testtask.ui;

import com.haulmont.testtask.entity.Patient;
import com.haulmont.testtask.ui.validation.NameSurnameValidator;
import com.haulmont.testtask.ui.validation.PatronymicValidator;
import com.haulmont.testtask.ui.validation.PhoneNumberValidator;
import com.haulmont.testtask.ui.validation.ValidatorAdder;
import com.vaadin.ui.TextField;


public class PatientEditDialog extends AbstractEditDialog {
	private TextField id;
	private TextField firstName;
	private TextField lastName;
	private TextField patronymic;
	private TextField phoneNumber;
	
	private Patient patient;
	
	protected PatientEditDialog() {
		super("Добавить пациента");
		showMessage("Введите данные пациента и нажмите кнопку ОК");
		init();
	}
	
	protected PatientEditDialog(Patient patient) {
		super("Редактировать пациента");
		showMessage("Измените данные пациента и нажмите кнопку ОК");
		this.patient = patient;
		init();
		setPatientInfo();
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
		
		phoneNumber = new TextField(); 
		phoneNumber.setCaption("Телефонный номер");
		phoneNumber.setPlaceholder("+7XXXXXXXXXX");
		phoneNumber.setRequiredIndicatorVisible(true);
		ValidatorAdder.addValidator(phoneNumber, new PhoneNumberValidator());
		inputFieldList.add(phoneNumber);
		
		addFieldsToLayout();
		
		buttonLayout.addComponent(applyButton);
		buttonLayout.addComponent(cancelButton);
		buttonLayout.setWidth(TEXTFIELD_WIDTH_PERCENT);
		
		applyButton.addClickListener(e -> applyButtonClicked());
		
		//applyButton.addClickListener()
		layout.addComponent(buttonLayout);
	}
	
	@Override
	protected boolean isFieldsCorrect() {
		//if field has ERROR mark - then false
		if (!super.isFieldsCorrect()) return false;
		//if required field is empty - then false
		if (lastName.getValue().equals("") || firstName.getValue().equals("") || phoneNumber.getValue().equals(""))
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
			if (patient == null) {
				patient = new Patient(firstName.getValue(), lastName.getValue(), patronymic.getValue(), phoneNumber.getValue());	
				receivedId = testDbService.savePatient(patient);
			} else {
				patient = new Patient(patient.getId(), firstName.getValue(), lastName.getValue(), patronymic.getValue(), phoneNumber.getValue());
				isUpdated = testDbService.updatePatient(patient);
			}
		} catch (Exception e) {
			e.printStackTrace();
			showErrorMessage("Не удалось сохранить данные о пациенте.");
		}
		
		if (receivedId!=-1) { 
			patient.setId(receivedId);
			id.setValue(Long.toString(receivedId));
			blockTextFieldsAndButtons();
			showAcceptedMessage("Пациент успешно сохранён. Закройте окно.");
		}
		if (isUpdated) {
			blockTextFieldsAndButtons();
			showAcceptedMessage("Пациент успешно обновлён. Закройте окно.");
		}
	}
	//setting up initial info
	private void setPatientInfo() {
		id.setValue(Long.toString(patient.getId()));
		lastName.setValue(patient.getLastName());
		firstName.setValue(patient.getFirstName());
		patronymic.setValue(patient.getPatronymic());
		phoneNumber.setValue(patient.getPhoneNumber());
	}
}
