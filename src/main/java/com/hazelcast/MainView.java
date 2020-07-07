package com.hazelcast;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Route
public class MainView extends VerticalLayout {

    public MainView() {
        add(hostLabel(), tasksGrid());
    }

    private Component tasksGrid() {
        Object object = VaadinSession.getCurrent().getSession().getAttribute("grids");
        TaskGrid grid = object == null ? new TaskGrid() : (TaskGrid) object;
        grid.getElement().removeFromTree();
        return grid;
    }

    private Component hostLabel() {
        Text label = new Text("");
        try {
            String hostName = InetAddress.getLocalHost().getHostName();
            label.setText(hostName);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return label;
    }
}