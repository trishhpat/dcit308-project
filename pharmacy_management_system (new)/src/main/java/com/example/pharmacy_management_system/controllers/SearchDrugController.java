package com.example.pharmacy_management_system.controllers;

import com.example.pharmacy_management_system.models.Drug;
import com.example.pharmacy_management_system.utils.DatabaseConnection;
import javafx.application.Platform;
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
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SearchDrugController {
    @FXML
    private TextField searchField;

    @FXML
    private TableView<Drug> searchResultsTable;

    @FXML
    private TableColumn<Drug, String> resultDrugNameColumn;

    @FXML
    private TableColumn<Drug, String> resultDrugDescriptionColumn;

    @FXML
    private TableColumn<Drug, Integer> resultDrugQuantityColumn;

    @FXML
    private TableColumn<Drug, Double> resultDrugPriceColumn;

    @FXML
    private Label loadingLabel;

    @FXML
    private Label pleaseWaitLabel;

    private ObservableList<Drug> searchResults = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Initialize table columns
        resultDrugNameColumn.setCellValueFactory(new PropertyValueFactory<>("drugName"));
        resultDrugDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        resultDrugQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        resultDrugPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        // Hide "Please wait" label initially
        pleaseWaitLabel.setVisible(false);
    }

    @FXML
    private void handleSearch(ActionEvent event) {
        searchResults.clear(); // Clear previous search results

        String searchTerm = searchField.getText().trim();

        if (searchTerm.isEmpty()) {
            // Display message or handle empty search term
            return;
        }

        // Show "Please wait" label
        pleaseWaitLabel.setVisible(true);

        // Perform database search in background task
        Task<Void> searchTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                try (Connection connection = DatabaseConnection.getConnection()) {
                    String query = "SELECT * FROM drugs WHERE LOWER(drug_name) LIKE LOWER(?)";
                    PreparedStatement statement = connection.prepareStatement(query);
                    statement.setString(1, "%" + searchTerm.toLowerCase() + "%");

                    ResultSet resultSet = statement.executeQuery();

                    while (resultSet.next()) {
                        int id = resultSet.getInt("id");
                        String drugName = resultSet.getString("drug_name");
                        String description = resultSet.getString("description");
                        int quantity = resultSet.getInt("quantity");
                        double price = resultSet.getDouble("price");
                        int supplier_id = resultSet.getInt("supplier_id");

                        Drug drug = new Drug(id, drugName, description, quantity, price, supplier_id);
                        searchResults.add(drug);
                    }

                    resultSet.close();
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                    // Handle database query exception
                }
                return null;
            }

            @Override
            protected void succeeded() {
                super.succeeded();

                // Hide "Please wait" label when search is complete
                pleaseWaitLabel.setVisible(false);

                // Update TableView on JavaFX Application Thread
                Platform.runLater(() -> {
                    searchResultsTable.setItems(searchResults);
                });
            }
        };

        new Thread(searchTask).start();
    }

    @FXML
    public void handleBack(ActionEvent event) {
        try {
            Parent homePage = FXMLLoader.load(getClass().getResource("/com/example/pharmacy_management_system/hello-view.fxml"));
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
