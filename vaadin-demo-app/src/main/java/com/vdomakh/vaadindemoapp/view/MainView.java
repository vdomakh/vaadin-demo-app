package com.vdomakh.vaadindemoapp.view;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import com.vdomakh.vaadindemoapp.dao.JsonItemDAO;
import com.vdomakh.vaadindemoapp.domain.Item;

@Route
public class MainView extends VerticalLayout {
    private final JsonItemDAO jsonItemDAO;
    private Grid<Item> grid = new Grid<>(Item.class, false);
    private List<Item> updatedList = new ArrayList<>();
    
    private final TextField filter = new TextField();
    private final Button addNewButton = new Button("Создать", VaadinIcon.PLUS.create());
    private final Button saveButton = new Button("Сохранить", VaadinIcon.DATABASE.create());
    private final HorizontalLayout toolbar = new HorizontalLayout(addNewButton, saveButton);

    @Autowired
    public MainView(JsonItemDAO jsonItemDAO) {
        this.jsonItemDAO = jsonItemDAO;
        
        filter.setPlaceholder("Поиск по строке");
        filter.setValueChangeMode(ValueChangeMode.EAGER);
        filter.addValueChangeListener(field -> showItems(field.getValue()));
        
        grid.addColumn("id");
        grid.addColumn("name");
        grid.addColumn("email");
        grid.addColumn("phone");
        grid.addColumn("description");
        grid.setSelectionMode(SelectionMode.NONE);
        
        grid.addItemDoubleClickListener(event -> showDialog(event.getItem()));
        
        add(toolbar, filter, grid);
        
        showItems("");
        
        addNewButton.addClickListener(e -> showCreationDialog());
        saveButton.addClickListener(e -> saveTableToDB());
        
    }
    
    private void showItems(String filterString) {
    	if (updatedList.isEmpty()) {
		    	if (filterString.isEmpty()) {
		    		grid.setItems(jsonItemDAO.getAllItems());
		    	}
		    	else {
		    		grid.setItems(jsonItemDAO.getItemsByFilter(filterString));
		    	}
    	}
    	else {
    		if (filterString.isEmpty()) {
	    		grid.setItems(updatedList);
	    	}
	    	else {
	        	List<Item> result = new ArrayList<>();
	          	for (Item i : updatedList) {
	        		String nameDescriptionConcat = i.getName() + " " + i.getDescription();
	        		if (nameDescriptionConcat.contains(filterString)) {
	        			result.add(i);
	        		}
	          	} 
	    		grid.setItems(result);
	    	}
    	}
    }
    
    private void showDialog(Item i) {
    	Dialog dialog = new Dialog();
    	dialog.setModal(true);
    	VerticalLayout dialogLayout = new VerticalLayout();
    	Text t = new Text(i.toString());
    	dialogLayout.add(t);
    	dialog.add(dialogLayout);
    	add(dialog);
    	dialog.open();
    	
    }
    
    private void showCreationDialog() {
    	Dialog dialog = new Dialog();
    	dialog.setModal(true);
    	
    	TextField newName = new TextField("Name");
    	TextField newEmail = new TextField("E-mail");
    	TextField newPhone = new TextField("Phone");
    	TextField newDescription = new TextField("Description");
    	
    	FormLayout formLayout = new FormLayout();
    	formLayout.add(
    	        newName, newEmail,
    	        newPhone, newDescription
    	);
    	
        Button saveButton = new Button("Сохранить", VaadinIcon.PLUS.create());
        Button discardButton = new Button("Отменить", VaadinIcon.THUMBS_DOWN.create());
    	
    	dialog.add(formLayout,saveButton,discardButton);
    	add(dialog);
    	dialog.open();
    	
    	long newId = updatedList.isEmpty() ? jsonItemDAO.getAllItems().size()+1 : updatedList.size()+1;
    	saveButton.addClickListener(e -> {
    		propagateTableWithNewItem(new Item(
    				newId,
        			newName.getValue(),
        			newEmail.getValue(),
        			newPhone.getValue(),
        			newDescription.getValue(),
        			true));
    		dialog.close();
    	});
    	discardButton.addClickListener(e -> dialog.close());
    	
    }
    
    private void propagateTableWithNewItem(Item i) {
    	if (!updatedList.isEmpty()) {
    		updatedList.add(i);
    	}
    	else {
    		updatedList = jsonItemDAO.getAllItems();
    		updatedList.add(i);
    	}
    	grid.setItems(updatedList);	
    }
    
    private void saveTableToDB() {
    	if (!updatedList.isEmpty()) {
    	jsonItemDAO.updateItems(updatedList);
    	updatedList.clear();
    	}
    }
    
}
