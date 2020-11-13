package com.haulmont.testtask;

import java.util.List;

import com.haulmont.testtask.entity.Doctor;
import com.haulmont.testtask.entity.Patient;
import com.haulmont.testtask.entity.Prescription;
import com.haulmont.testtask.service.TestDbService;
import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@Theme(ValoTheme.THEME_NAME)
public class MainUI extends UI {
	
	private String WIDTH_PERCENTAGE = "80%";
	private String GRID_HEIGHT_PERCENTAGE = "90%";
	
	//data
	private List<Patient> patientList;
	
	//layout
	private VerticalLayout mainLayout;
	private HorizontalLayout buttonLayout;
	
	//components
	private Label caption;
	
	private Grid<Patient> patientGrid;
	private Grid<Prescription> prescriptionGrid;
	private Grid<Doctor> doctorGrid;

	private Button createButton;
	private Button updateButton;
	private Button deleteButton;
	
	//db service
	private TestDbService testDbService = new TestDbService();

    @Override
    protected void init(VaadinRequest request) {
    	
    	TabSheet mainTabSheet = new TabSheet();
    	        
    	mainLayout = new VerticalLayout();
        mainLayout.setSizeFull();
        mainLayout.setMargin(true);
        
        caption = new Label();
        caption.setCaption("NO BUTTON PRESSED");
        mainLayout.addComponent(caption);
        mainLayout.setComponentAlignment(caption, Alignment.TOP_CENTER);
        
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
        
        //prescriptionGrid = new Grid<Prescription>(Prescription.class);
        prescriptionGrid = new Grid<Prescription>();
        prescriptionGrid.addColumn(thePrescription -> thePrescription.getDoctor().getFirstName() + thePrescription.getDoctor().getLastName());
        prescriptionGrid.addColumn(thePrescription -> thePrescription.getId()).setCaption("Id").setId("prescription_id");
        prescriptionGrid.addColumn(thePrescription -> thePrescription.getDoctor().getFirstName() + thePrescription.getDoctor().getLastName() + thePrescription.getDoctor().getPatronymic())
        					.setCaption("Doctor").setId("doctor");
        prescriptionGrid.addColumn(thePrescription -> thePrescription.getPatient().getFirstName() + thePrescription.getPatient().getLastName() + thePrescription.getPatient().getPatronymic())
		.setCaption("Patient").setId("patient");
        prescriptionGrid.addColumn(thePrescription -> thePrescription.getDate()).setCaption("Date").setId("date");
        prescriptionGrid.addColumn(thePrescription -> thePrescription.getDuration()).setCaption("Duration").setId("duration");
        prescriptionGrid.addColumn(thePrescription -> thePrescription.getDescription()).setCaption("Description").setId("description");
        prescriptionGrid.addColumn(thePrescription -> thePrescription.getPriority()).setCaption("Priority").setId("priority");

        
        
        //prescriptionGrid.setColumnOrder("id", "doctor", "patient", "date", "duration", "description", "priority");
        VerticalLayout prescriptionLayout = generateTabGridLayout(prescriptionGrid);
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
        
        updatePatientGrid();
        
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
    

    protected void updatePatientGrid()
    {
    	patientList = testDbService.getPatientList();
    	System.out.println(patientList);
    	System.out.println(patientList.size());
    	
    	patientGrid.setItems(patientList);
    }
    
    private class CreateButtonClickListener implements Button.ClickListener
    {

		@Override
		public void buttonClick(ClickEvent event) {
			//todo listener stub
			caption.setCaption("ITS CREATE, BRO");			
		}
    	
    }
    
    private class UpdateButtonClickListener implements Button.ClickListener
    {

		@Override
		public void buttonClick(ClickEvent event) {
			//todo listener stub
			caption.setCaption("ITS UPDATE, BRO");			
		}
    	
    }
    
    private class DeleteButtonClickListener implements Button.ClickListener
    {

		@Override
		public void buttonClick(ClickEvent event) {
			//todo listener stub
			caption.setCaption("ITS DELETE, BRO");			
		}
    	
    }
    
    

}