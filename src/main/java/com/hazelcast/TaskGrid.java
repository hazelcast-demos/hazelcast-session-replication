package com.hazelcast;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.server.VaadinSession;

import java.util.ArrayList;
import java.util.List;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME;

public class TaskGrid extends Grid<Task> {

    private final List<Task> tasks = new ArrayList<>();

    public TaskGrid() {
        super(Task.class, false);
        configureDataProvider();
        configureCellRenderers();
        configureHeaders();
    }

    private void configureDataProvider() {
        ListDataProvider<Task> provider = new ListDataProvider<>(tasks);
        setDataProvider(provider);
    }

    private void configureCellRenderers() {
        addComponentColumn(item -> new Label(item.getId().toString()));
        addComponentColumn(item -> new Label(item.getLabel()));
        addComponentColumn(item -> {
            Checkbox cb = new Checkbox(item.isDone());
            cb.addClickListener(event -> item.setDone(cb.getValue()));
            saveState();
            return cb;
        });
        addComponentColumn(item -> new Label(ISO_LOCAL_DATE_TIME.format(item.getCreated())));
    }

    private void configureHeaders() {
        HeaderRow addTaskRow = appendHeaderRow();
        TextField labelField = new TextField();
        addTaskRow.getCell(labelColumn()).setComponent(labelField);
        Button addButton = new Button("Add");
        addTaskRow.getCell(doneColumn()).setComponent(addButton);
        addButton.addClickListener(addTask(labelField));
        HeaderRow titleRow = appendHeaderRow();
        titleRow.getCell(idColumn()).setText("ID");
        titleRow.getCell(labelColumn()).setText("Label");
        titleRow.getCell(doneColumn()).setText("Done?");
        titleRow.getCell(createdColumn()).setText("Created");
    }

    private ComponentEventListener<ClickEvent<Button>> addTask(TextField field) {
        return event -> {
            Task task = new Task(field.getValue());
            field.clear();
            tasks.add(task);
            getDataProvider().refreshAll();
            saveState();
        };
    }

    private Column<Task> idColumn() {
        return getColumns().get(0);
    }

    private Column<Task> labelColumn() {
        return getColumns().get(1);
    }

    private Column<Task> doneColumn() {
        return getColumns().get(2);
    }

    private Column<Task> createdColumn() {
        return getColumns().get(3);
    }

    private void saveState() {
        VaadinSession.getCurrent().getSession().setAttribute("grids", this);
    }
}