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
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@Theme(ValoTheme.THEME_NAME)
public class MainUI extends UI {
	
	private String WIDTH_PERCENTAGE = "80%";
	private String GRID_HEIGHT_PERCENTAGE = "90%";
	
	//db service
	private TestDbService testDbService = new TestDbService();
	
	//components
	private Label headerCaption;

	//layout
	protected VerticalLayout mainLayout;
	
	//switchable grids tab
	protected TabSheet gridTabSheet;
	
	//grids + layouts
	protected VerticalLayout patientLayout;
	protected Grid<Patient> patientGrid;
	
	protected VerticalLayout prescriptionLayout;
	protected Grid<Prescription> prescriptionGrid;
	protected HorizontalLayout filterLayout;
	protected Label searchCaption;
	protected TextField filterPrescriptionTextField;
	
	protected VerticalLayout doctorLayout;
	protected Grid<Doctor> doctorGrid;
	
	

    @Override
    protected void init(VaadinRequest request) {
    	
    	//switchable tab content
    	mainLayout = new VerticalLayout();
        mainLayout.setSizeFull();
        mainLayout.setMargin(true);
        
        headerCaption = new Label();
        headerCaption.setCaption("NO BUTTON PRESSED");
        mainLayout.addComponent(headerCaption);
        mainLayout.setComponentAlignment(headerCaption, Alignment.TOP_CENTER);
        
        
        gridTabSheet = new TabSheet();
        gridTabSheet.setWidth(WIDTH_PERCENTAGE);
        gridTabSheet.setHeight(GRID_HEIGHT_PERCENTAGE);
        mainLayout.addComponent(gridTabSheet);
        mainLayout.setComponentAlignment(gridTabSheet, Alignment.MIDDLE_CENTER);
        mainLayout.setExpandRatio(gridTabSheet, 1.0f);
        
        createPatientGrid();
        patientLayout = generateTabGridLayout(patientGrid);
        gridTabSheet.addTab(patientLayout, "Пациент").setId("Patient");
        
        createPrescriptionGrid();
        filterLayout = new HorizontalLayout();
        searchCaption = new Label(); searchCaption.setCaption("Поиск по описанию:");
        filterPrescriptionTextField = new TextField();
        filterPrescriptionTextField.setValueChangeMode(ValueChangeMode.EAGER);
        filterPrescriptionTextField.setPlaceholder("Фильтр по описанию...");
        filterPrescriptionTextField.addValueChangeListener(e -> updateFilteredPrescriptionGrid(e.getValue()));
        
        filterLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        filterLayout.addComponent(searchCaption);
        filterLayout.addComponent(filterPrescriptionTextField);        
        
        prescriptionLayout = generateTabGridLayout(prescriptionGrid);
        prescriptionLayout.addComponent(filterLayout);       
        prescriptionLayout.setComponentAlignment(filterLayout, Alignment.MIDDLE_CENTER);
        
        gridTabSheet.addTab(prescriptionLayout, "Рецепт").setId("Prescription");
        
        createDoctorGrid();
        doctorLayout = generateTabGridLayout(doctorGrid);
        gridTabSheet.addTab(doctorLayout, "Врач").setId("Doctor");
        
        createButtonLayout();
        updateGrid(patientGrid, testDbService.getPatientList());

        gridTabSheet.addSelectedTabChangeListener(new TabSheetGridUpdater());
        
        setContent(mainLayout);
    }





	private void createButtonLayout() {
		HorizontalLayout buttonLayout;
		Button createButton;
		Button updateButton;
		Button deleteButton;
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
	}


	private void createPrescriptionGrid() {
		prescriptionGrid = new Grid<Prescription>();
        prescriptionGrid.addColumn(Prescription::getId).setCaption("Id").setId("prescription_id");
        prescriptionGrid.addColumn(thePrescription -> thePrescription.getDoctor().getId() + "#" + thePrescription.getDoctor().getLastName() + thePrescription.getDoctor().getFirstName() + thePrescription.getDoctor().getPatronymic())
        					.setCaption("Врач").setId("doctor");
        prescriptionGrid.addColumn(thePrescription -> thePrescription.getPatient().getId() + "#" + thePrescription.getPatient().getLastName() + thePrescription.getPatient().getFirstName() + thePrescription.getPatient().getPatronymic())
		.setCaption("Пациент").setId("patient");
        prescriptionGrid.addColumn(Prescription::getDate).setCaption("Дата").setId("date");
        prescriptionGrid.addColumn(Prescription::getExpirationDate).setCaption("Срок").setId("duration");
        prescriptionGrid.addColumn(Prescription::getDescription).setCaption("Описание").setId("description");
        prescriptionGrid.addColumn(Prescription::getPriority).setCaption("Приоритет").setId("priority");
	}


	private void createPatientGrid() {
		patientGrid = new Grid<Patient>();
        patientGrid.addColumn(Patient::getId).setCaption("Id").setId("id");
        patientGrid.addColumn(Patient::getLastName).setCaption("Фамилия").setId("lastName");
        patientGrid.addColumn(Patient::getFirstName).setCaption("Имя").setId("firstName");
        patientGrid.addColumn(Patient::getPatronymic).setCaption("Отчество").setId("patronymic");
        patientGrid.addColumn(Patient::getPhoneNumber).setCaption("Номер телефона").setId("phoneNumber");
	}
	
	private void createDoctorGrid() {
		doctorGrid = new Grid<Doctor>();
        doctorGrid.addColumn(Doctor::getId).setCaption("Id").setId("id");
        doctorGrid.addColumn(Doctor::getLastName).setCaption("Фамилия").setId("lastName");
        doctorGrid.addColumn(Doctor::getFirstName).setCaption("Имя").setId("firstName");
        doctorGrid.addColumn(Doctor::getPatronymic).setCaption("Отчество").setId("patronymic");
        doctorGrid.addColumn(Doctor::getSpecialization).setCaption("Специализация").setId("specialization");
	}

	private VerticalLayout generateTabGridLayout(Grid<?> someGrid) {
		
		someGrid.setWidth(WIDTH_PERCENTAGE);
        
        VerticalLayout someLayout = new VerticalLayout();
        someLayout.addComponent(someGrid);
        someLayout.setComponentAlignment(someGrid, Alignment.MIDDLE_CENTER);
        someLayout.setExpandRatio(someGrid, 1.0f);
		return someLayout;
	}
    

    protected <T> void updateGrid(Grid<T> grid, List<T> list)
    {
    	System.out.println(list);
    	if (list.size() == 0) System.out.println("! > > > Zero items for updating, its weird");
    	grid.setItems(list);
    }
    
    protected void updateFilteredPrescriptionGrid(String filter) {
    	prescriptionGrid.setItems(testDbService.getFilteredPrescriptionList(filter));
        //grid.setItems(service.findAll(filterText.getValue()));
    }
    
    protected void refreshCurrentTab() {

		String tabId = gridTabSheet.getTab(gridTabSheet.getSelectedTab()).getId();
    	
		switch (tabId) {
		
		case "Patient":
			//doctorGrid = null;
			//doctorLayout = null;
			/*createPatientGrid();
	        patientLayout = generateTabGridLayout(patientGrid);*/
			List<Patient> patientList = testDbService.getPatientList();
			updateGrid(patientGrid, patientList);
			break;
		case "Prescription":
			//patientLayout = null;
			//patientGrid = null;
			//doctorLayout = null;
			//doctorGrid = null;
			List<Prescription> prescriptionList = testDbService.getPrescriptionList();
			updateGrid(prescriptionGrid, prescriptionList);
			break;
		case "Doctor":
			/*patientLayout = null;
			patientGrid = null;
			createDoctorGrid();
	        doctorLayout = generateTabGridLayout(doctorGrid);*/
			List<Doctor> doctorList = testDbService.getDoctorList();
			updateGrid(doctorGrid, doctorList);
			break;
			
		default:
			//do nothing
		
		}
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
       
    private class TabSheetGridUpdater implements TabSheet.SelectedTabChangeListener
    {
		@Override
		public void selectedTabChange(SelectedTabChangeEvent event) {			
			refreshCurrentTab();
		}
    	
    }

}