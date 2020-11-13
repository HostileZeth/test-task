package com.haulmont.testtask;

import java.util.List;

import com.haulmont.testtask.entity.Patient;
import com.haulmont.testtask.service.TestDbService;
import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@Theme(ValoTheme.THEME_NAME)
public class MainUI extends UI {
	
	private List<Patient> patientList;
	private Grid<Patient> patientGrid;
	
	@SuppressWarnings("unused")
	private TestDbService testDbService = new TestDbService();

    @Override
    protected void init(VaadinRequest request) {
        VerticalLayout layout = new VerticalLayout();
        layout.setSizeFull();
        layout.setMargin(true);

        layout.addComponent(new Label("Main UI"));
        layout.addComponent(new Label("I like adding captions"));
        
        testDbService.testDbAccess();
        
        System.out.println(testDbService.getPatientList());
        
        initPatientGrid();
        updatePatientGrid();
        
        layout.addComponent(patientGrid);

        setContent(layout);
    }
    
    protected void initPatientGrid()
    {
    	patientGrid = new Grid<Patient>(Patient.class);
    	//patientGrid.setColumns("id", "Имя", "Фамилия", "Отчество", "Номер телефона");
    	
    }
    
    protected void updatePatientGrid()
    {
    	patientList = testDbService.getPatientList();
    	System.out.println(patientList);
    	System.out.println(patientList.size());
    	
    	patientGrid.setItems(patientList);
        //setFormVisible(false);
    }
    

}