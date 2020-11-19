package com.haulmont.testtask.ui;

import java.util.List;

import com.haulmont.testtask.entity.DoctorStatEntity;
import com.vaadin.ui.Grid;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class DoctorStatsDisplayWindow extends Window {	
	private Grid<DoctorStatEntity> dataGrid;
	private VerticalLayout layout;
	
	public DoctorStatsDisplayWindow(List<DoctorStatEntity> statsList) {
		super("Статистика врачей");
		layout = new VerticalLayout();
		dataGrid = new Grid<DoctorStatEntity>();
		dataGrid.addColumn(DoctorStatEntity::getId).setCaption("Id").setId("id").setResizable(false);
		dataGrid.addColumn(DoctorStatEntity::getDoctorName).setCaption("Врач").setId("doctor").setResizable(false);
		dataGrid.addColumn(DoctorStatEntity::getRecipesCount).setCaption("Кол-во рецептов").setId("recipesCount").setResizable(false);		
		setPositionX(200);
		setPositionY(200);
		dataGrid.setItems(statsList);
		setResizable(false);
		layout.addComponent(dataGrid);
		layout.setExpandRatio(dataGrid, 0.9f);
		setContent(layout);
	}

}
