package com.example.pharmacy_management_system.controllers;

import com.example.pharmacy_management_system.models.Drug;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ViewDrugsController {
    @FXML
    private TableView<Drug> drugsTable;

    @FXML
    private TableColumn<Drug, String> drugNameColumn;

    @FXML
    private TableColumn<Drug, Double> drugPriceColumn;

    @FXML
    private TableColumn<Drug, String> drugDescriptionColumn;

    @FXML
    private Label loadingLabel;

    private static final String URL = "jdbc:postgresql://dpg-cpnubig8fa8c73b7h500-a.oregon-postgres.render.com:5432/pms_database_v9a2";
    private static final String USER = "pms_database_v9a2_user";
    private static final String PASSWORD = "r3UcCBXBQ9umX0L2c96E2BoYRHsJzR6a";

    private ObservableList<Drug> drugData;

    @FXML
    public void initialize() {
        drugData = FXCollections.observableArrayList();
        drugNameColumn.setCellValueFactory(new PropertyValueFactory<>("drugName"));
        drugPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        drugDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        loadDrugsFromDatabase();

        // Add double-click event handler
        drugsTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Drug selectedDrug = drugsTable.getSelectionModel().getSelectedItem();
                if (selectedDrug != null) {
                    openUpdateWindow(selectedDrug);
                }
            }
        });
    }

    private void openUpdateWindow(Drug drug) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/pharmacy_management_system/update_drug.fxml"));
            Parent updateDrugPage = loader.load();

            UpdateDrugController controller = loader.getController();
            controller.initData(drug);

            Stage stage = new Stage();
            stage.setScene(new Scene(updateDrugPage));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            // Refresh the table after the update window is closed
            loadDrugsFromDatabase();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadDrugsFromDatabase() {
        drugData.clear();
        String sql = "SELECT * FROM drugs";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String drugName = rs.getString("drug_name");
                String description = rs.getString("description");
                int quantity = rs.getInt("quantity");
                double price = rs.getDouble("price");
                int supplier_id = rs.getInt("supplier_id");


                Drug drug = new Drug(id, drugName, description, quantity, price, supplier_id);
                drugData.add(drug);
            }

            drugsTable.setItems(drugData);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void handleBack(ActionEvent event) {
        try {
            Parent homePage = FXMLLoader.load(getClass().getResource("/com/example/pharmacy_management_system/hello-view.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.getScene().setRoot(homePage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleAdd(ActionEvent event) {
        try {
            Parent homePage = FXMLLoader.load(getClass().getResource("/com/example/pharmacy_management_system/add_drug.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.getScene().setRoot(homePage);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
