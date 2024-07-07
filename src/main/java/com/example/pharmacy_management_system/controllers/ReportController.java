package com.example.pharmacy_management_system.controllers;

import com.example.pharmacy_management_system.models.Drug;
import com.example.pharmacy_management_system.models.PurchaseHistory;
import com.example.pharmacy_management_system.utils.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class ReportController {
    @FXML
    private Button addButton;
    @FXML
    private Label loadingLabel;
    @FXML
    private TableView<Drug> drugTable;
    @FXML
    private TableColumn<Drug, String> drugNameColumn;
    @FXML
    private TableColumn<Drug, Integer> initialQuantityColumn;
    @FXML
    private TableColumn<Drug, Integer> remainingQuantityColumn;

    @FXML
    public void initialize() {
        drugNameColumn.setCellValueFactory(new PropertyValueFactory<>("drugName"));
        initialQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        remainingQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("remainingQuantity"));

        loadDrugData();
    }

    private void loadDrugData() {
        Task<ObservableList<Drug>> task = new Task<>() {
            @Override
            protected ObservableList<Drug> call() {
                return getDrugReport();
            }
        };

        task.setOnSucceeded(e -> {
            drugTable.setItems(task.getValue());
            loadingLabel.setVisible(false);
        });

        loadingLabel.setVisible(true);
        new Thread(task).start();
    }

    private ObservableList<Drug> getDrugReport() {
        ObservableList<Drug> drugList = FXCollections.observableArrayList();
        Map<String, Integer> drugPurchaseMap = PurchaseHistory.getTotalQuantitiesPurchased();

        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT id, drug_name, quantity FROM drugs";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String drugName = resultSet.getString("drug_name");
                int initialQuantity = resultSet.getInt("quantity");
                int purchasedQuantity = drugPurchaseMap.getOrDefault(drugName, 0);
                int remainingQuantity = initialQuantity - purchasedQuantity;

                Drug drug = new Drug(id, drugName, "", initialQuantity, 0, 0);
                drug.setRemainingQuantity(remainingQuantity);

                drugList.add(drug);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return drugList;
    }


    // Existing methods...

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

    public void handleBack(ActionEvent event) {
        try {
            Parent homePage = FXMLLoader.load(getClass().getResource("/com/example/pharmacy_management_system/hello-view.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.getScene().setRoot(homePage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
