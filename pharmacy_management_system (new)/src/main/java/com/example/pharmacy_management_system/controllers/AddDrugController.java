package com.example.pharmacy_management_system.controllers;

import com.example.pharmacy_management_system.models.Supplier;
import com.example.pharmacy_management_system.utils.DatabaseConnection;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.util.StringConverter;

import java.io.IOException;
import java.sql.*;

public class AddDrugController {
    @FXML
    private Button addButton;

    @FXML
    private TextField drugNameField;

    @FXML
    private TextField descriptionField;

    @FXML
    private TextField quantityField;

    @FXML
    private TextField priceField;

    @FXML
    private Label successLabel;

    @FXML
    private Label errorLabel;

    @FXML
    private Label pleaseWaitLabel;

    @FXML
    private Label loadingLabel;

    @FXML
    private TextField typeField;

    @FXML
    private TextField categoryField;

    @FXML
    private ComboBox<Supplier> supplierComboBox;

    private ObservableList<Supplier> suppliersList = FXCollections.observableArrayList();

    @FXML
    public void initialize() throws SQLException {
        loadSuppliers();
    }

    private void loadSuppliers() throws SQLException {
        Connection connection = DatabaseConnection.getConnection();
        String query = "SELECT * FROM suppliers";
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String contact = resultSet.getString("contact");
                String address = resultSet.getString("address");

                // Create Supplier object and add to the suppliersList
                Supplier supplier = new Supplier(id, name, contact, address);
                suppliersList.add(supplier);
            }

            // Set items and converter for ComboBox
            supplierComboBox.setItems(suppliersList);
            supplierComboBox.setConverter(new StringConverter<Supplier>() {
                @Override
                public String toString(Supplier supplier) {
                    return supplier == null ? null : supplier.getName();
                }

                @Override
                public Supplier fromString(String string) {
                    return suppliersList.stream().filter(supplier ->
                            supplier.getName().equals(string)).findFirst().orElse(null);
                }
            });

            // Print all suppliers after setting items to the ComboBox
            System.out.println("Suppliers in ComboBox:");
            for (Supplier supplier : suppliersList) {
                System.out.println("Supplier ID: " + supplier.getId() + ", Name: " + supplier.getName() + ", Contact: " + supplier.getContact() + ", Address: " + supplier.getAddress());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }






    private static final String URL = "jdbc:postgresql://dpg-cpnubig8fa8c73b7h500-a.oregon-postgres.render.com:5432/pms_database_v9a2";
    private static final String USER = "pms_database_v9a2_user";
    private static final String PASSWORD = "r3UcCBXBQ9umX0L2c96E2BoYRHsJzR6a";


    @FXML
    public void handleSaveDrug() throws SQLException {
        String drugName = drugNameField.getText();
        String description = descriptionField.getText();
        String quantityStr = quantityField.getText();
        String priceStr = priceField.getText();
        Supplier selectedSupplier = supplierComboBox.getSelectionModel().getSelectedItem();

        if (selectedSupplier == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please select a supplier.");
            alert.showAndWait();
            return;
        }
        int supplierId = selectedSupplier.getId();


        if (drugName.isEmpty() || description.isEmpty() || quantityStr.isEmpty() || priceStr.isEmpty() || selectedSupplier == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all fields and select a supplier.");
            alert.showAndWait();
            return;
        }

        int quantity;
        double price;
        try {
            quantity = Integer.parseInt(quantityStr);
            price = Double.parseDouble(priceStr);
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please enter valid numbers for quantity and price.");
            alert.showAndWait();
            return;
        }

        // Save drug details to the database
        Connection connection = DatabaseConnection.getConnection();
        String insertQuery = "INSERT INTO drugs (drug_name, description, quantity, price, supplier_id) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(insertQuery)) {
            pstmt.setString(1, drugName);
            pstmt.setString(2, description);
            pstmt.setInt(3, quantity);
            pstmt.setDouble(4, price);
            pstmt.setInt(5, selectedSupplier.getId());

            pstmt.executeUpdate();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Drug added successfully!");
            alert.showAndWait();

            drugNameField.clear();
            descriptionField.clear();
            quantityField.clear();
            priceField.clear();
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Error adding drug.");
            alert.showAndWait();
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

    public void handleBack(ActionEvent event) {
        try {
            Parent homePage = FXMLLoader.load(getClass().getResource("/com/example/pharmacy_management_system/hello-view.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.getScene().setRoot(homePage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAdd() {
        String drugName = drugNameField.getText();
        String description = descriptionField.getText();
        String quantityText = quantityField.getText();
        String priceText = priceField.getText();

        if (drugName.isEmpty() || description.isEmpty() || quantityText.isEmpty() || priceText.isEmpty()) {
            errorLabel.setText("All fields must be filled.");
            errorLabel.setVisible(true);
            return;
        }

        pleaseWaitLabel.setText("Please wait...");
        pleaseWaitLabel.setVisible(true);
        errorLabel.setVisible(false);
        successLabel.setVisible(false);

        new Thread(() -> {
            try {
                int quantity = Integer.parseInt(quantityText);
                double price = Double.parseDouble(priceText);

                String sql = "INSERT INTO drugs (drug_name, description, quantity, price) VALUES (?, ?, ?, ?)";

                try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                     PreparedStatement pstmt = conn.prepareStatement(sql)) {

                    pstmt.setString(1, drugName);
                    pstmt.setString(2, description);
                    pstmt.setInt(3, quantity);
                    pstmt.setDouble(4, price);
                    pstmt.executeUpdate();

                    Platform.runLater(() -> {
                        pleaseWaitLabel.setVisible(false);
                        successLabel.setVisible(true);
                        errorLabel.setVisible(false);

                        drugNameField.clear();
                        descriptionField.clear();
                        quantityField.clear();
                        priceField.clear();
                    });
                } catch (SQLException e) {
                    e.printStackTrace();
                    Platform.runLater(() -> {
                        pleaseWaitLabel.setVisible(false);
                        errorLabel.setText("Error adding drug to the database.");
                        errorLabel.setVisible(true);
                        successLabel.setVisible(false);
                    });
                }
            } catch (NumberFormatException e) {
                Platform.runLater(() -> {
                    pleaseWaitLabel.setVisible(false);
                    errorLabel.setText("Quantity must be an integer and price must be a double.");
                    errorLabel.setVisible(true);
                    successLabel.setVisible(false);
                });
            }
        }).start();
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
