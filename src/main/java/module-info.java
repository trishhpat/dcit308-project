module com.example.pharmacy_management_system {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires java.sql;
    requires com.opencsv;
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;
    requires org.postgresql.jdbc;

    opens com.example.pharmacy_management_system to javafx.fxml;
    opens com.example.pharmacy_management_system.controllers to javafx.fxml;

    exports com.example.pharmacy_management_system;
    exports com.example.pharmacy_management_system.controllers;

    opens com.example.pharmacy_management_system.models to javafx.base;

}