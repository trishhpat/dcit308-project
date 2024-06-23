package com.example.pharmacy_management_system;

import com.example.pharmacy_management_system.utils.DatabaseConnection;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class HelloController {

    @FXML
    private Label loadingLabel;

    @FXML
    private Label totalDrugsLabel;

    @FXML
    private Label totalSuppliersLabel;

    @FXML
    private Label totalPurchasesLabel;

    private IntegerProperty totalDrugs = new SimpleIntegerProperty();
    private IntegerProperty totalSuppliers = new SimpleIntegerProperty();
    private IntegerProperty totalPurchases = new SimpleIntegerProperty();

    @FXML
    public void initialize() {
        // Fetch data when the controller is initialized
        fetchDataFromDatabase();
    }

    private void fetchDataFromDatabase() {
        // Show the loading label
        loadingLabel.setVisible(true);

        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws SQLException {
                try (Connection connection = DatabaseConnection.getConnection();
                     Statement statement = connection.createStatement()) {

                    ResultSet resultSet;

                    // Fetch total drugs
                    resultSet = statement.executeQuery("SELECT COUNT(*) AS total FROM drugs");
                    if (resultSet.next()) {
                        totalDrugs.set(resultSet.getInt("total"));
                    }

                    // Fetch total suppliers
                    resultSet = statement.executeQuery("SELECT COUNT(*) AS total FROM suppliers");
                    if (resultSet.next()) {
                        totalSuppliers.set(resultSet.getInt("total"));
                    }

                    // Fetch total purchases
                    resultSet = statement.executeQuery("SELECT COUNT(*) AS total FROM purchase_history");
                    if (resultSet.next()) {
                        totalPurchases.set(resultSet.getInt("total"));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };

        task.setOnSucceeded(e -> {
            // Hide the loading label
            loadingLabel.setVisible(false);

            // Update the UI on the JavaFX Application Thread
            Platform.runLater(() -> {
                totalDrugsLabel.setText(String.valueOf(totalDrugs.get()));
                totalSuppliersLabel.setText(String.valueOf(totalSuppliers.get()));
                totalPurchasesLabel.setText(String.valueOf(totalPurchases.get()));
            });
        });

        new Thread(task).start();
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
                Platform.runLater(() -> stage.getScene().setRoot(root));
                return null;
            }
        };

        // Set the task to hide the loading label after loading is done
        task.setOnSucceeded(e -> loadingLabel.setVisible(false));

        // Run the task in a background thread
        new Thread(task).start();
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