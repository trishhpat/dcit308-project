package com.example.pharmacy_management_system.controllers;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddSupplierController {
    @FXML
    private TextField nameField;

    @FXML
    private TextField contactField;

    @FXML
    private TextField addressField;

    @FXML
    private Button addButton;

    @FXML
    private Label messageLabel;

    @FXML
    private Label loadingLabel;

    private static final String URL = "jdbc:postgresql://dpg-cpnubig8fa8c73b7h500-a.oregon-postgres.render.com:5432/pms_database_v9a2";
    private static final String USER = "pms_database_v9a2_user";
    private static final String PASSWORD = "r3UcCBXBQ9umX0L2c96E2BoYRHsJzR6a";

    @FXML
    public void handleBack(ActionEvent event) {
        try {
            Parent homePage = FXMLLoader.load(getClass().getResource("/com/example/pharmacy_management_system/view_suppliers.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.getScene().setRoot(homePage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAdd() {
        String name = nameField.getText();
        String contact = contactField.getText();
        String address = addressField.getText();

        if (name.isEmpty() || contact.isEmpty() || address.isEmpty()) {
            messageLabel.setText("Please fill all fields.");
            messageLabel.setVisible(true);
            return;
        }

        String sql = "INSERT INTO suppliers (name, contact, address) VALUES (?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, name);
            pstmt.setString(2, contact);
            pstmt.setString(3, address);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                messageLabel.setText("Supplier added successfully!");
                messageLabel.setVisible(true);
                clearFields();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            messageLabel.setText("Error adding supplier.");
            messageLabel.setVisible(true);
        }
    }

    private void clearFields() {
        nameField.clear();
        contactField.clear();
        addressField.clear();
    }

    public void navigate(String fxml, ActionEvent event) {
        // Show the loading label
        loadingLabel.setVisible(true);

        // Create a task to load the new scene
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
                Parent root = loader.load();
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                // Update the UI on the JavaFX Application Thread
                javafx.application.Platform.runLater(() -> stage.getScene().setRoot(root));
                return null;
            }
        };

        // Set the task to hide the loading label after loading is done
        task.setOnSucceeded(e -> loadingLabel.setVisible(false));

        // Run the task in a background thread
        new Thread(task).start();
    }

    public void handleAddDrugs(ActionEvent event) {
        navigate("/com/example/pharmacy_management_system/add_drug.fxml", event);
    }

    public void handleSearchDrugs(ActionEvent event) {
        navigate("/com/example/pharmacy_management_system/search_drug.fxml", event);
    }

    public void handleViewSuppliers(ActionEvent event) {
        navigate("/com/example/pharmacy_management_system/view_suppliers.fxml", event);
    }

    public void handleViewDrugs(ActionEvent event) {
        navigate("/com/example/pharmacy_management_system/view_drugs.fxml", event);
    }

    public void handleViewPurchaseHistory(ActionEvent event) {
        navigate("/com/example/pharmacy_management_system/view_purchase_history.fxml", event);
    }

    public void handleStatisticsAndReports(ActionEvent event) {
        navigate("/com/example/pharmacy_management_system/statistics.fxml", event);
    }
}
