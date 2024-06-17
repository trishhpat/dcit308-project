module com.example.pharmacy_management_system {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;

    opens com.example.pharmacy_management_system to javafx.fxml;
    exports com.example.pharmacy_management_system;
}