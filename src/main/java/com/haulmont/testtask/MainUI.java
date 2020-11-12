package com.haulmont.testtask;

import com.haulmont.testtask.service.TestDbService;
import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Runo;

@SuppressWarnings("serial")
@Theme(Runo.THEME_NAME)
public class MainUI extends UI {

    @Override
    protected void init(VaadinRequest request) {
        VerticalLayout layout = new VerticalLayout();
        layout.setSizeFull();
        layout.setMargin(true);

        layout.addComponent(new Label("Main UI"));
        layout.addComponent(new Label("I like adding captions"));
        
        TestDbService ndb = new TestDbService();
        
        
        ndb.testDbAccess();
        
        

        setContent(layout);
    }
}