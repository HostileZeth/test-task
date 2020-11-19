package com.haulmont.testtask.ui;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;

import com.haulmont.testtask.entity.Doctor;
import com.haulmont.testtask.entity.Patient;
import com.haulmont.testtask.entity.Prescription;
import com.haulmont.testtask.entity.enumeration.Priority;
import com.haulmont.testtask.service.DateConverterService;
import com.haulmont.testtask.ui.validation.DatePresentationValidator;
import com.haulmont.testtask.ui.validation.IdValidator;
import com.haulmont.testtask.ui.validation.NonEmptyStringValidator;
import com.haulmont.testtask.ui.validation.ValidatorAdder;
import com.vaadin.server.UserError;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.TextField;

public class PrescriptionEditDialog extends AbstractEditDialog {
	private TextField id;
	private TextField doctorId;
	private TextField patientId;
	private TextField description;
	private TextField date;
	private TextField expirationDate;
	private ComboBox<Priority> priorityComboBox;
	private Prescription prescription;
	
	private static final String RU_DATEFORMAT_PLACEHOLDER = "ДД-ММ-ГГГГ";
	
	protected PrescriptionEditDialog() {
		super("Добавить рецепт");
		showMessage("Введите данные рецепта и нажмите кнопку ОК");
		init();
	}
	
	protected PrescriptionEditDialog(Prescription prescription) {
		super("Редактировать рецепта");
		showMessage("Измените данные рецепта и нажмите кнопку ОК");
		this.prescription = prescription;
		init();
		setPrescriptionInfo();
	}
	
	private void init() {
		id = new TextField(); id.setCaption("Id"); id.setReadOnly(true); id.setValue("(пусто)"); id.setEnabled(false);
		inputFieldList.add(id);	
		doctorId = new TextField(); 
		doctorId.setCaption("Id доктора");
		doctorId.setRequiredIndicatorVisible(true);
		ValidatorAdder.addValidator(doctorId, new IdValidator());
		inputFieldList.add(doctorId);
		
		patientId = new TextField(); 
		patientId.setCaption("Id пациента");
		patientId.setRequiredIndicatorVisible(true);
		ValidatorAdder.addValidator(patientId, new IdValidator());
		inputFieldList.add(patientId);
		
		description = new TextField(); 
		description.setCaption("Описание");
		description.setRequiredIndicatorVisible(true);
		ValidatorAdder.addValidator(description, new NonEmptyStringValidator());
		inputFieldList.add(description);
		
		date = new TextField(); 
		date.setCaption("Дата"); 
		date.setPlaceholder(RU_DATEFORMAT_PLACEHOLDER);
		date.setRequiredIndicatorVisible(true);
		ValidatorAdder.addValidator(date, new DatePresentationValidator());
		inputFieldList.add(date);
		
		expirationDate = new TextField(); 
		expirationDate.setCaption("Срок"); 
		expirationDate.setPlaceholder(RU_DATEFORMAT_PLACEHOLDER);
		expirationDate.setRequiredIndicatorVisible(true);
		ValidatorAdder.addValidator(expirationDate, new DatePresentationValidator());
		inputFieldList.add(expirationDate);
		
		priorityComboBox = new ComboBox<Priority>(); 
		priorityComboBox.setCaption("Приоритет");
		priorityComboBox.setRequiredIndicatorVisible(true);
		priorityComboBox.setItems(Priority.values());
		priorityComboBox.addValueChangeListener(e -> {
			if (e.getValue() == null) priorityComboBox.setComponentError(new UserError("Поле приоритет обязательно для заполнения"));
			else
				priorityComboBox.setComponentError(null);
		});
		
		addFieldsToLayout();
		buttonLayout.addComponent(applyButton);
		buttonLayout.addComponent(cancelButton);
		buttonLayout.setWidth(TEXTFIELD_WIDTH_PERCENT);
		applyButton.addClickListener(e -> applyButtonClicked());
		
		layout.addComponent(buttonLayout);
	}
	
	@Override //additionally adding combobox field
	protected void addFieldsToLayout() {
		super.addFieldsToLayout();
		layout.addComponent(priorityComboBox);
	}
	
	protected void blockTextFieldsAndButtons() {
		super.blockTextFieldsAndButtons();
		priorityComboBox.setEnabled(false);
	}
	
	private void applyButtonClicked() {
		DateConverterService converterInstance = DateConverterService.instanceOf();
		long receivedId = -1;
		boolean isUpdated = false;
		
		if (priorityComboBox.getValue() == null) {
			showErrorMessage("Выберите приоритет рецепта!");
			return;
		}
		try {
			long doctorIdValue;
			long patientIdValue;
			Doctor requiredDoctor = null;
			Patient requiredPatient = null;
			Date prescriptionDateValue = null;
			Date expirationDateValue = null;
			try {
				doctorIdValue = Long.parseLong(doctorId.getValue()); 
				patientIdValue = Long.parseLong(patientId.getValue());
			} catch (NumberFormatException e) {
				showErrorMessage("Введите корректные Id врача и пациента");
				return;
			}
			try {
				requiredDoctor = testDbService.getDoctor(doctorIdValue);
			} catch (SQLException e) {
				showErrorMessage("Введите корректное Id врача.");
				return;
			}
			try {
				requiredPatient = testDbService.getPatient(patientIdValue);
			} catch (SQLException e) {
				showErrorMessage("Введите корректное Id пациента");
				return;
			}
			try {
				prescriptionDateValue = converterInstance.presentationStringToDate(date.getValue());
			} catch (ParseException e) {
				showErrorMessage("Введите дату рецепта в формате ДД-ММ-ГГГГ");
				return;
			}
			try {
				expirationDateValue = converterInstance.presentationStringToDate(expirationDate.getValue());
			} catch (ParseException e) {
				showErrorMessage("Введите срок рецепта в формате ДД-ММ-ГГГГ");
				return;
			}
			
			if (prescriptionDateValue.compareTo(expirationDateValue) > 0) {
				showErrorMessage("Срок действия рецепта не может быть раньше, чем дата");
				return;
			}
			
			if (prescription == null) {
				prescription = new Prescription(requiredDoctor, requiredPatient, description.getValue(), prescriptionDateValue, expirationDateValue, priorityComboBox.getValue());
				receivedId = testDbService.savePrescription(prescription);
			} else {
				prescription = new Prescription(prescription.getId(), requiredDoctor, requiredPatient, description.getValue(),
						prescriptionDateValue, expirationDateValue, priorityComboBox.getValue());
				isUpdated = testDbService.updatePrescription(prescription);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			showErrorMessage("Не удалось сохранить данные о рецепте.");
		}
		if (receivedId!=-1) { 
			prescription.setId(receivedId);
			id.setValue(Long.toString(receivedId));
			blockTextFieldsAndButtons();
			showAcceptedMessage("Рецепт успешно сохранён. Закройте окно.");
		}
		if (isUpdated) {
			blockTextFieldsAndButtons();
			showAcceptedMessage("Рецепт успешно обновлён. Закройте окно.");
		}
	}
	
	//setting up initial info
	private void setPrescriptionInfo() {
		DateConverterService converterInstance = DateConverterService.instanceOf();
		id.setValue(Long.toString(prescription.getId()));
		doctorId.setValue(Long.toString(prescription.getDoctor().getId()));
		patientId.setValue(Long.toString(prescription.getPatient().getId()));
		description.setValue(prescription.getDescription());
		date.setValue(converterInstance.dateToPresentationString(prescription.getDate()));
		expirationDate.setValue(converterInstance.dateToPresentationString(prescription.getExpirationDate()));
		priorityComboBox.setValue(prescription.getPriority());
	}
}
