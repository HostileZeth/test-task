package com.haulmont.testtask;

import java.util.List;

import com.haulmont.testtask.entity.Doctor;
import com.haulmont.testtask.entity.Patient;
import com.haulmont.testtask.entity.Prescription;
import com.haulmont.testtask.service.TestDbService;
import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@Theme(ValoTheme.THEME_NAME)
public class MainUI extends UI {
	
	private String WIDTH_PERCENTAGE = "80%";
	private String GRID_HEIGHT_PERCENTAGE = "90%";
	
	//components
	private Label headerCaption;

	//db service
	private TestDbService testDbService = new TestDbService();

    @Override
    protected void init(VaadinRequest request) {
    	

    	
    	//layout
    	VerticalLayout mainLayout;
    	HorizontalLayout buttonLayout;
    	
    	//CRUD buttons
    	Button createButton;
    	Button updateButton;
    	Button deleteButton;
    	
    	//tabs
    	TabSheet mainTabSheet;
    	
    	//switchable tab content
    	Grid<Patient> patientGrid;
    	
    	Grid<Prescription> prescriptionGrid;
    	Label searchCaption;
    	TextField filterPrescriptionTextField;
    	
    	Grid<Doctor> doctorGrid;
    	
    	mainTabSheet = new TabSheet();
    	        
    	mainLayout = new VerticalLayout();
        mainLayout.setSizeFull();
        mainLayout.setMargin(true);
        
        headerCaption = new Label();
        headerCaption.setCaption("NO BUTTON PRESSED");
        mainLayout.addComponent(headerCaption);
        mainLayout.setComponentAlignment(headerCaption, Alignment.TOP_CENTER);
        
        TabSheet gridTabSheet = new TabSheet();
        gridTabSheet.setWidth(WIDTH_PERCENTAGE);
        gridTabSheet.setHeight(GRID_HEIGHT_PERCENTAGE);
        mainLayout.addComponent(gridTabSheet);
        mainLayout.setComponentAlignment(gridTabSheet, Alignment.MIDDLE_CENTER);
        mainLayout.setExpandRatio(gridTabSheet, 1.0f);
        
        
        patientGrid = new Grid<Patient>(Patient.class);
        patientGrid.setColumnOrder("id", "firstName", "lastName", "patronymic", "phoneNumber");
        VerticalLayout patientLayout = generateTabGridLayout(patientGrid);
        gridTabSheet.addTab(patientLayout, "Пациент");

        prescriptionGrid = new Grid<Prescription>();
        prescriptionGrid.addColumn(thePrescription -> thePrescription.getDoctor().getFirstName() + thePrescription.getDoctor().getLastName());
        prescriptionGrid.addColumn(thePrescription -> thePrescription.getId()).setCaption("Id").setId("prescription_id");
        prescriptionGrid.addColumn(thePrescription -> thePrescription.getDoctor().getId() + "#" + thePrescription.getDoctor().getFirstName() + thePrescription.getDoctor().getLastName() + thePrescription.getDoctor().getPatronymic())
        					.setCaption("Врач").setId("doctor");
        prescriptionGrid.addColumn(thePrescription -> thePrescription.getPatient().getId() + "#" + thePrescription.getPatient().getFirstName() + thePrescription.getPatient().getLastName() + thePrescription.getPatient().getPatronymic())
		.setCaption("Пациент").setId("patient");
        prescriptionGrid.addColumn(thePrescription -> thePrescription.getDate()).setCaption("Дата").setId("date");
        prescriptionGrid.addColumn(thePrescription -> thePrescription.getDuration()).setCaption("Срок").setId("duration");
        prescriptionGrid.addColumn(thePrescription -> thePrescription.getDescription()).setCaption("Описание").setId("description");
        prescriptionGrid.addColumn(thePrescription -> thePrescription.getPriority()).setCaption("Приоритет").setId("priority");
        
        HorizontalLayout filterLayout = new HorizontalLayout();
        searchCaption = new Label(); searchCaption.setCaption("Поиск по описанию:");
        filterPrescriptionTextField = new TextField();
        filterPrescriptionTextField.setValueChangeMode(ValueChangeMode.EAGER);
        filterLayout.addComponent(searchCaption);
        filterLayout.addComponent(filterPrescriptionTextField);        
        
        //prescriptionGrid.setColumnOrder("id", "doctor", "patient", "date", "duration", "description", "priority");
        VerticalLayout prescriptionLayout = generateTabGridLayout(prescriptionGrid);
        prescriptionLayout.addComponent(filterLayout);        
        
        gridTabSheet.addTab(prescriptionLayout, "Рецепт");
        
        doctorGrid = new Grid<Doctor>(Doctor.class);
        doctorGrid.setColumnOrder("id", "firstName", "lastName", "patronymic", "specialization");
        VerticalLayout doctorLayout = generateTabGridLayout(doctorGrid);
        gridTabSheet.addTab(doctorLayout, "Врач");
        
        buttonLayout = new HorizontalLayout();
        buttonLayout.setWidth(WIDTH_PERCENTAGE);
        mainLayout.addComponent(buttonLayout);
        mainLayout.setComponentAlignment(buttonLayout, Alignment.BOTTOM_CENTER);
        
        createButton = new Button("Добавить");
        createButton.addClickListener(new CreateButtonClickListener());
        buttonLayout.addComponent(createButton);
        buttonLayout.setComponentAlignment(createButton, Alignment.TOP_CENTER);
        
        
        updateButton = new Button("Изменить");
        updateButton.addClickListener(new UpdateButtonClickListener());
        buttonLayout.addComponent(updateButton);
        buttonLayout.setComponentAlignment(updateButton, Alignment.TOP_CENTER);

        
        deleteButton = new Button("Удалить");
        deleteButton.addClickListener(new DeleteButtonClickListener());
        buttonLayout.addComponent(deleteButton);
        buttonLayout.setComponentAlignment(deleteButton, Alignment.TOP_CENTER);
        
        updatePatientGrid(patientGrid, testDbService.getPatientList());
        
        //setContent(mainLayout);
        mainTabSheet.addTab(mainLayout, "Patients");
        
        gridTabSheet.setSelectedTab(2); // duct taped
        
        setContent(mainTabSheet);
    }


	private VerticalLayout generateTabGridLayout(Grid<?> someGrid) {
		someGrid.setWidth(WIDTH_PERCENTAGE);
        
        VerticalLayout someLayout = new VerticalLayout();
        someLayout.addComponent(someGrid);
        someLayout.setComponentAlignment(someGrid, Alignment.MIDDLE_CENTER);
        someLayout.setExpandRatio(someGrid, 1.0f);
		return someLayout;
	}
    

    protected void updatePatientGrid(Grid<Patient> grid, List<Patient> list)
    {
    	System.out.println(list);
    	System.out.println(list.size());
    	
    	grid.setItems(list);
    }
    
    private class CreateButtonClickListener implements Button.ClickListener
    {

		@Override
		public void buttonClick(ClickEvent event) {
			//todo listener stub
			headerCaption.setCaption("ITS CREATE, BRO");			
		}
    	
    }
    
    private class UpdateButtonClickListener implements Button.ClickListener
    {

		@Override
		public void buttonClick(ClickEvent event) {
			//todo listener stub
			headerCaption.setCaption("ITS UPDATE, BRO");			
		}
    	
    }
    
    private class DeleteButtonClickListener implements Button.ClickListener
    {

		@Override
		public void buttonClick(ClickEvent event) {
			//todo listener stub
			headerCaption.setCaption("ITS DELETE, BRO");			
		}
    	
    }
    
    

}