package com.haulmont.testtask.ui;

import java.sql.SQLException;
import java.util.List;

import com.haulmont.testtask.entity.Doctor;
import com.haulmont.testtask.entity.DoctorStatEntity;
import com.haulmont.testtask.entity.HasId;
import com.haulmont.testtask.entity.Patient;
import com.haulmont.testtask.entity.Prescription;
import com.haulmont.testtask.entity.enumeration.Priority;
import com.haulmont.testtask.service.DateConverterService;
import com.haulmont.testtask.service.DbService;
import com.haulmont.testtask.ui.validation.IdValidator;
import com.haulmont.testtask.ui.validation.ValidatorAdder;
import com.vaadin.annotations.Theme;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@Theme(ValoTheme.THEME_NAME) 
public class MainUI extends UI {
	private static final String WIDTH_PERCENTAGE = "85%";
	private static final String GRID_HEIGHT_PERCENTAGE = "90%";
	private static final float GRID_HEIGHT_BY_ROWS = 10f;
	private static final float BUTTON_PANEL_EXPAND_RATIO = 0.3f;
	private static final String FILTER_LAYOUT_WIDTH = "60%";
	
	private DbService dbService;

	private Label headerCaption; // info header
	protected VerticalLayout mainLayout; //main ui layout
	protected TabSheet gridTabSheet; //switchable patient - prescription - doctor grids tabs
	
	protected VerticalLayout patientLayout; // patients
	protected Grid<Patient> patientGrid;
	
	protected VerticalLayout prescriptionLayout; // prescriptions
	protected HorizontalLayout filterOptionsLayout;
	protected Grid<Prescription> prescriptionGrid;
	protected TextField filterPrescriptionDescriptionTextField; // and filtering
	protected TextField filterPrescriptionPatientIdTextField;
	protected ComboBox<Priority> filterPrescriptionPriorityComboBox;
	protected Button filterButton;
	
	protected VerticalLayout doctorLayout; // doctors
	protected Grid<Doctor> doctorGrid;
	protected Button showDoctorStatsButton;
	
	public MainUI() {
		dbService = DbService.instanceOf();
	}

    @Override
    protected void init(VaadinRequest request) {
    	mainLayout = new VerticalLayout();
        mainLayout.setSizeFull();
        mainLayout.setMargin(true);
                        
        headerCaption = new Label();
        showGreeting();
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
        
        filterOptionsLayout = new HorizontalLayout();
        filterPrescriptionPatientIdTextField = new TextField();
        filterPrescriptionPatientIdTextField.setPlaceholder("Введите Id");
        filterPrescriptionPatientIdTextField.setCaption("Пациент");
        ValidatorAdder.addValidator(filterPrescriptionPatientIdTextField, new IdValidator());
        
        filterPrescriptionDescriptionTextField = new TextField();
        filterPrescriptionDescriptionTextField.setValueChangeMode(ValueChangeMode.EAGER);
        filterPrescriptionDescriptionTextField.setPlaceholder("Введите текст");
        filterPrescriptionDescriptionTextField.setCaption("Описание");
        
        filterPrescriptionPriorityComboBox = new ComboBox<Priority>();
        filterPrescriptionPriorityComboBox.setCaption("Приоритет");
        filterPrescriptionPriorityComboBox.setItems(Priority.values());
        
        filterButton = new Button();
        filterButton.setCaption("Применить");
        filterButton.addClickListener(e -> {
        	if (filterPrescriptionPatientIdTextField.getComponentError() == null) {
        		updateFilteredPrescriptionGrid(filterPrescriptionPatientIdTextField.getValue(),
        				filterPrescriptionDescriptionTextField.getValue().toString(), filterPrescriptionPriorityComboBox.getValue());}
        });
        
        filterOptionsLayout.addComponent(filterPrescriptionPatientIdTextField);
        filterOptionsLayout.addComponent(filterPrescriptionPriorityComboBox);
        filterOptionsLayout.addComponent(filterPrescriptionDescriptionTextField);
        filterOptionsLayout.addComponent(filterButton);
        filterOptionsLayout.setComponentAlignment(filterButton, Alignment.BOTTOM_CENTER); //nice
        filterOptionsLayout.setWidth(FILTER_LAYOUT_WIDTH);
        
        prescriptionLayout = generateTabGridLayout(prescriptionGrid);
        prescriptionLayout.addComponent(filterOptionsLayout);
        prescriptionLayout.setComponentAlignment(filterOptionsLayout, Alignment.MIDDLE_CENTER);
        
        gridTabSheet.addTab(prescriptionLayout, "Рецепт").setId("Prescription");
        
        createDoctorGrid();
        doctorLayout = generateTabGridLayout(doctorGrid);
        showDoctorStatsButton = new Button("Показать статистику");
        showDoctorStatsButton.addClickListener(e -> showDoctorStats());
        doctorLayout.addComponent(showDoctorStatsButton);
        doctorLayout.setComponentAlignment(showDoctorStatsButton, Alignment.MIDDLE_CENTER);
        gridTabSheet.addTab(doctorLayout, "Врач").setId("Doctor");
        
        createButtonLayout();
        
        try {
        updateGrid(patientGrid, dbService.getPatientList());
        }
        catch (SQLException e) {
        	showErrorMessage("Не удалось прочитать данные из БД.");
        }
        
        gridTabSheet.addSelectedTabChangeListener(e -> refreshCurrentTab());
        setContent(mainLayout);
    }

	private void showGreeting() {
		headerCaption.setIcon(null);
		headerCaption.setCaption("Начните работу с системой. Выберите вкладку, затем используйте одну из трёх функций Создать / Удалить / Редактировать");
	}
    
	private void createButtonLayout() {
		HorizontalLayout buttonLayout;
		Button createButton;
		Button updateButton;
		Button deleteButton;
		buttonLayout = new HorizontalLayout();
        buttonLayout.setWidth(FILTER_LAYOUT_WIDTH);

        mainLayout.addComponents(buttonLayout);
        mainLayout.setExpandRatio(buttonLayout, BUTTON_PANEL_EXPAND_RATIO);
        mainLayout.setComponentAlignment(buttonLayout, Alignment.TOP_CENTER); 
        
        createButton = new Button("Добавить");
        createButton.addClickListener(new CreateButtonClickListener());
        buttonLayout.addComponent(createButton);
        buttonLayout.setComponentAlignment(createButton, Alignment.TOP_LEFT);
        
        updateButton = new Button("Изменить");
        updateButton.addClickListener(new UpdateButtonClickListener());
        buttonLayout.addComponent(updateButton);
        buttonLayout.setComponentAlignment(updateButton, Alignment.TOP_CENTER);
        
        deleteButton = new Button("Удалить");
        deleteButton.addClickListener(new DeleteButtonClickListener());
        buttonLayout.addComponent(deleteButton);
        buttonLayout.setComponentAlignment(deleteButton, Alignment.TOP_RIGHT);
	}

	private void createPrescriptionGrid() {
		DateConverterService converterInstance = DateConverterService.instanceOf();
		prescriptionGrid = new Grid<Prescription>();
        prescriptionGrid.addColumn(Prescription::getId).setCaption("Id").setId("prescription_id").setResizable(false);
        prescriptionGrid.addColumn(thePrescription -> thePrescription.getDoctor().getId() + "#" +
        		thePrescription.getDoctor().getLastName() + " " + thePrescription.getDoctor().getFirstName() + " "+ thePrescription.getDoctor().getPatronymic())
        					.setCaption("Врач").setId("doctor").setResizable(false);
        prescriptionGrid.addColumn(thePrescription -> thePrescription.getPatient().getId() + "#" +
        		thePrescription.getPatient().getLastName() + " " + thePrescription.getPatient().getFirstName() + " " + thePrescription.getPatient().getPatronymic())
		.setCaption("Пациент").setId("patient").setResizable(false);
        prescriptionGrid.addColumn(thePrescription -> converterInstance.dateToPresentationString(thePrescription.getDate())).setCaption("Дата").setId("date").setResizable(false);
        prescriptionGrid.addColumn(thePrescription 
        		-> converterInstance.dateToPresentationString(thePrescription.getExpirationDate())).setCaption("Срок").setId("duration").setResizable(false);
        prescriptionGrid.addColumn(Prescription::getDescription).setCaption("Описание").setId("description").setResizable(false);
        prescriptionGrid.addColumn(Prescription::getPriority).setCaption("Приоритет").setId("priority").setResizable(false);
        prescriptionGrid.setHeightByRows(GRID_HEIGHT_BY_ROWS);
        prescriptionGrid.setSelectionMode(SelectionMode.SINGLE);
        prescriptionGrid.setStyleName(ValoTheme.TABLE_NO_STRIPES); // css dont works
	}

	private void createPatientGrid() {
		patientGrid = new Grid<Patient>();
        patientGrid.addColumn(Patient::getId).setCaption("Id").setId("id").setResizable(false); //.setWidth(0.1f);
        patientGrid.addColumn(Patient::getLastName).setCaption("Фамилия").setId("lastName").setResizable(false);
        patientGrid.addColumn(Patient::getFirstName).setCaption("Имя").setId("firstName").setResizable(false);
        patientGrid.addColumn(Patient::getPatronymic).setCaption("Отчество").setId("patronymic").setResizable(false);
        patientGrid.addColumn(Patient::getPhoneNumber).setCaption("Номер телефона").setId("phoneNumber").setResizable(false);
        patientGrid.setHeightByRows(GRID_HEIGHT_BY_ROWS);
        patientGrid.setSelectionMode(SelectionMode.SINGLE);
	}
	
	private void createDoctorGrid() {
		doctorGrid = new Grid<Doctor>();
        doctorGrid.addColumn(Doctor::getId).setCaption("Id").setId("id").setResizable(false); //.setWidth(0.1f);
        doctorGrid.addColumn(Doctor::getLastName).setCaption("Фамилия").setId("lastName").setResizable(false);
        doctorGrid.addColumn(Doctor::getFirstName).setCaption("Имя").setId("firstName").setResizable(false);
        doctorGrid.addColumn(Doctor::getPatronymic).setCaption("Отчество").setId("patronymic").setResizable(false);
        doctorGrid.addColumn(Doctor::getSpecialization).setCaption("Специализация").setId("specialization").setResizable(false);
        doctorGrid.setHeightByRows(GRID_HEIGHT_BY_ROWS);
        doctorGrid.setSelectionMode(SelectionMode.SINGLE);
	}

	private VerticalLayout generateTabGridLayout(Grid<?> someGrid) {
		VerticalLayout someLayout = new VerticalLayout();
		someGrid.setWidth(WIDTH_PERCENTAGE);
        someLayout.addComponent(someGrid);
        someLayout.setComponentAlignment(someGrid, Alignment.MIDDLE_CENTER);
		return someLayout;
	}

    protected <T> void updateGrid(Grid<T> grid, List<T> list) {
    	System.out.println(list);
    	if (list.size() == 0) System.out.println("! > > > Zero items for updating, its weird");
    	grid.setItems(list);
    }
    
    protected void cleanFilters() {
        filterPrescriptionPatientIdTextField.setValue("");
        filterPrescriptionDescriptionTextField.setValue("");
        filterPrescriptionPriorityComboBox.setValue(null);
    }
    
    protected void showDoctorStats() {
    	List<DoctorStatEntity> doctorStats = null;
    	DoctorStatsDisplayWindow statsWindow = null;
    	try {
			doctorStats = dbService.getDoctorStats();
		} catch (SQLException e) {
			showErrorMessage("Не удалось получить статистику по врачам из БД.");
			e.printStackTrace();
			return;
		}
    	
    	statsWindow = new DoctorStatsDisplayWindow(doctorStats);
    	statsWindow.setModal(true);
    	statsWindow.addCloseListener(e -> refreshCurrentTab());
		addWindow(statsWindow);
    }
    
    protected void updateFilteredPrescriptionGrid(String patient_id, String filter, Priority priority) {
    	try {
    		if (!patient_id.contentEquals("") || !filter.contentEquals("") || priority != null)
    			prescriptionGrid.setItems(dbService.getFilteredPrescriptionList(patient_id, filter, priority));
    		else prescriptionGrid.setItems(dbService.getPrescriptionList());
    	} catch (Exception e) {
    		showErrorMessage("Не удалось прочитать данные из БД.");
    		e.printStackTrace();
    	}
    }
    
    protected String getSelectedTabId() {
    	return gridTabSheet.getTab(gridTabSheet.getSelectedTab()).getId();
    }
    
    protected <T extends HasId> long getSelectedItemId(Grid<T> grid) {
    	long selectedRowId = -1;
    	HasId selectedObject = getSelectedItem(grid);
    	if (selectedObject != null) selectedRowId = selectedObject.getId();
    	return selectedRowId;
    }
    
    protected <T extends HasId> T getSelectedItem(Grid<T> grid) {
    	return grid.asSingleSelect().getValue();
    }
    
    protected void refreshCurrentTab() {
		String tabId = getSelectedTabId();
		try {
			switch (tabId) {
			case "Patient":
				List<Patient> patientList = dbService.getPatientList();
				updateGrid(patientGrid, patientList);
				break;
			case "Prescription":
				List<Prescription> prescriptionList = dbService.getPrescriptionList();
				updateGrid(prescriptionGrid, prescriptionList);
				cleanFilters();
				break;
			case "Doctor":
				List<Doctor> doctorList = dbService.getDoctorList();
				updateGrid(doctorGrid, doctorList);
				break;
			default:	// there should be no default branch
			}
		} catch (SQLException e) {
			showErrorMessage("Не удалось прочитать данные из БД.");
		}
    }
    
    protected void showInfoMessage(String message) {
    	headerCaption.setIcon(VaadinIcons.CHECK);
    	headerCaption.setCaption(message);
    }
    
    protected void showErrorMessage(String message) {
    	headerCaption.setIcon(VaadinIcons.BAN);
    	headerCaption.setCaption(message);
    }
        
    private class CreateButtonClickListener implements Button.ClickListener {
		@Override
		public void buttonClick(ClickEvent event) {
			String selectedTabId = getSelectedTabId();
			AbstractEditDialog editWindow = null;
			switch (selectedTabId) {
			case "Patient":
				editWindow = new PatientEditDialog();
				break;
			case "Doctor":
				editWindow = new DoctorEditDialog();
				break;
			case "Prescription":
				editWindow = new PrescriptionEditDialog();
				break;
			default: //it should never happen
			}
			editWindow.setModal(true);
			editWindow.addCloseListener(e -> refreshCurrentTab());
			addWindow(editWindow);
			showGreeting();
		}	
    }
    
    private class UpdateButtonClickListener implements Button.ClickListener {
		@Override
		public void buttonClick(ClickEvent event) {
			String selectedTabId = getSelectedTabId();
			AbstractEditDialog editWindow = null;
			switch (selectedTabId) {
			case "Patient":
				Patient selectedPatient = getSelectedItem(patientGrid);
				if (selectedPatient == null) {
					showErrorMessage("Для изменения данных о пациенте выберите строчку в таблице"); 
					return;
				}
				editWindow = new PatientEditDialog(selectedPatient);
				break;
			case "Doctor":
				Doctor selectedDoctor = getSelectedItem(doctorGrid);
				if (selectedDoctor == null) {
					showErrorMessage("Для изменения данных о докторе выберите строчку в таблице"); 
					return;
				}
				editWindow = new DoctorEditDialog(selectedDoctor);
				break;
			case "Prescription":
				Prescription selectedPrescription = getSelectedItem(prescriptionGrid);
				if (selectedPrescription == null) {
					showErrorMessage("Для изменения данных о рецепте выберите строчку в таблице"); 
					return;
				}
				editWindow = new PrescriptionEditDialog(selectedPrescription);
				break;
			default: //it should never happen
			}
			editWindow.setModal(true);
			editWindow.addCloseListener(e -> refreshCurrentTab());
			addWindow(editWindow);
			showGreeting();
		}
    }
    
    private class DeleteButtonClickListener implements Button.ClickListener {	
		@Override
		public void buttonClick(ClickEvent event) {		
			String selectedTabId = getSelectedTabId();
			long selectedId = -1;
			try {			
				switch (selectedTabId) {
				case "Patient":
					selectedId = getSelectedItemId(patientGrid);
					if (dbService.deletePatient(selectedId)) {
						updateGrid(patientGrid, dbService.getPatientList()); showInfoMessage("Успешно удалён пациент " + selectedId);
					} else
						showErrorMessage("Невозможно удалить пациента " + selectedId +". Возможно есть связанные рецепты.");
					break;
				case "Doctor":
					selectedId = getSelectedItemId(doctorGrid);
					if (dbService.deleteDoctor(selectedId)) {
						updateGrid(doctorGrid, dbService.getDoctorList());
						showInfoMessage("Успешно удалён врач " + selectedId);
					}
					else showErrorMessage("Невозможно удалить врача " + selectedId +". Возможно есть связанные рецепты.");
					break;
				case "Prescription":
					selectedId = getSelectedItemId(prescriptionGrid);
					dbService.deletePrescription(selectedId);
					updateGrid(prescriptionGrid, dbService.getPrescriptionList());
					break;
				}
			}
			catch (SQLException e) {
				showErrorMessage("Не удалось прочитать данные из БД.");
			}
		}
    }
}