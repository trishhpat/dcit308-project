package com.example.pharmacy_management_system.controllers;

import com.example.pharmacy_management_system.models.PurchaseHistory;
import com.example.pharmacy_management_system.utils.DatabaseConnection;

import com.example.pharmacy_management_system.models.Drug;

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
import java.sql.Statement;
import java.time.LocalDateTime;

public class ViewPurchaseHistoryController {

    @FXML
    private Button addButton;

    @FXML
    private TableView<PurchaseHistory> purchaseHistoryTable;

    @FXML
    private TableColumn<PurchaseHistory, LocalDateTime> purchaseDateColumn;

    @FXML
    private TableColumn<PurchaseHistory, String> drugNameColumn;

    @FXML
    private TableColumn<PurchaseHistory, String> buyerColumn;

    @FXML
    private TableColumn<PurchaseHistory, Integer> quantityColumn;

    @FXML
    private TableColumn<PurchaseHistory, Double> totalAmountColumn;

    @FXML
    private Label loadingLabel;

    @FXML
    public void initialize() {
        purchaseDateColumn.setCellValueFactory(new PropertyValueFactory<>("purchaseDate"));
        drugNameColumn.setCellValueFactory(new PropertyValueFactory<>("drugName"));
        buyerColumn.setCellValueFactory(new PropertyValueFactory<>("buyer"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        totalAmountColumn.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));

        loadPurchaseHistory();
    }

    private void loadPurchaseHistory() {
        ObservableList<PurchaseHistory> purchaseHistories = FXCollections.observableArrayList();

        String sql = "SELECT ph.*, d.drug_name " +
                "FROM purchase_history ph " +
                "JOIN drugs d ON ph.drug_name = d.drug_name";

        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String drugName = rs.getString("drug_name");
                LocalDateTime purchaseDate = rs.getTimestamp("purchase_date").toLocalDateTime();
                String buyer = rs.getString("buyer");
                int quantity = rs.getInt("quantity");
                double totalAmount = rs.getDouble("total_amount");

                PurchaseHistory purchaseHistory = new PurchaseHistory(drugName, purchaseDate, buyer, quantity, totalAmount);
                purchaseHistory.setId(id); // Set the ID retrieved from the database
                purchaseHistories.add(purchaseHistory);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        purchaseHistoryTable.setItems(purchaseHistories);
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
            Parent homePage = FXMLLoader.load(getClass().getResource("/com/example/pharmacy_management_system/add_purchase.fxml"));
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
