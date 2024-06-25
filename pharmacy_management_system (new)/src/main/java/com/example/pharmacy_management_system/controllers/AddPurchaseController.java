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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class AddPurchaseController {

    @FXML
    private TextField drugNameField;

    @FXML
    private TextField buyerField;

    @FXML
    private TextField quantityField;

    @FXML
    private Label calculatedTotalLabel;

    @FXML
    private Label successLabel;

    @FXML
    private Button submitButton;

    @FXML
    private Label loadingLabel;

    @FXML
    private ListView<String> drugNameSuggestionsListView;

    //declare the system Models here  Model here 
    private Drug drugModel = new Drug();
    private PurchaseHistory purchasesModel = new PurchaseHistory();


    // Data structure to store drug names and prices for suggestions and calculations
    private Map<String, Double> drugNamePriceMap = new HashMap<>();

    public void initialize() {
        successLabel.setTranslateY(40);

        // Fetch drug names and prices from the database and populate the data structure
        fetchDrugDataFromDatabaseAndPopulateItInDrugNamePrice();

        // Set listener for drug name field to update suggestions dynamically
        drugNameField.textProperty().addListener((observable, oldValue, newValue) -> {
            updateDrugNameSuggestions(newValue);
        });

        // Handle item selection from drug name suggestions
        drugNameSuggestionsListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                drugNameField.setText(newValue); // Set selected drug name in the text field
            }
        });
    }

    private void fetchDrugDataFromDatabaseAndPopulateItInDrugNamePrice() {
        ObservableList<Drug> drugs = drugModel.getDrugsInDatabase();
        for (Drug drug : drugs) {
            drugNamePriceMap.put(drug.getDrugName(), drug.getPrice());
        }
    }

    private void updateDrugNameSuggestions(String input) {
        ObservableList<String> suggestions = FXCollections.observableArrayList();

        for (String drugName : drugNamePriceMap.keySet()) {
            if (drugName.toLowerCase().startsWith(input.toLowerCase())) {
                suggestions.add(drugName);
            }
        }

        drugNameSuggestionsListView.setItems(suggestions);
    }


    public void handleBack(ActionEvent event) {
        try {
            Parent homePage = FXMLLoader.load(getClass().getResource("/com/example/pharmacy_management_system/view_purchase_history.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.getScene().setRoot(homePage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAdd(ActionEvent event) throws SQLException {
      
            String drugName = drugNameField.getText().trim();
            String buyer = buyerField.getText().trim();
            int quantity = Integer.parseInt(quantityField.getText().trim());
            double totalAmount = calculateTotalAmount(drugName, quantity);
            LocalDateTime currentDate = LocalDateTime.now();
            PurchaseHistory newPurchase = new PurchaseHistory(drugName,currentDate,buyer,quantity,totalAmount);

            if(newPurchase.isStoredInDatabaeSuccessfully()){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Purchase recorded successfully added successfully!");
                alert.showAndWait();
            }else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Error adding Purchase.");
                alert.showAndWait();
            }
        

    }


    private double calculateTotalAmount(String drugName, int quantity) throws SQLException {
        if (drugNamePriceMap.containsKey(drugName)) {
            double price = drugNamePriceMap.get(drugName);
            return quantity * price;
        } else {
            throw new IllegalArgumentException("Drug name not found in database: " + drugName);
        }
    }

    // private void savePurchase(String drugName, String buyer, int quantity, double totalAmount, LocalDate currentDate) {
    //     // Assuming you have a database connection and a table to store purchases
    //     try (Connection connection = DatabaseConnection.getConnection()) {
    //         String query = "INSERT INTO purchase_history (drug_name, buyer, quantity, total_amount, purchase_date) VALUES (?, ?, ?, ?, ?)";
    //         PreparedStatement statement = connection.prepareStatement(query);
    //         statement.setString(1, drugName);
    //         statement.setString(2, buyer);
    //         statement.setInt(3, quantity);
    //         statement.setDouble(4, totalAmount);
    //         statement.setDate(5, java.sql.Date.valueOf(currentDate));

    //         int rowsInserted = statement.executeUpdate();
    //         if (rowsInserted > 0) {
    //             System.out.println("A new purchase was added successfully!");
    //             successLabel.setVisible(true);
    //         }

    //         statement.close();
    //     } catch (SQLException e) {
    //         e.printStackTrace();
    //         // Handle database exception (show error message, log, etc.)
    //     }
    // }

    @FXML
    private void handleCalculate(ActionEvent event) {
        try {
            String drugName = drugNameField.getText().trim();
            int quantity = Integer.parseInt(quantityField.getText().trim());
            double totalAmount = calculateTotalAmount(drugName, quantity);
            calculatedTotalLabel.setText(String.format("%.2f", totalAmount)); // Display total amount with two decimal places
        } catch (NumberFormatException | SQLException e) {
            e.printStackTrace();
            // Handle number format or SQL exceptions (e.g., show error message to user)
        }
    }

    private void clearFields() {
        drugNameField.clear();
        buyerField.clear();
        quantityField.clear();
        calculatedTotalLabel.setText(""); // Clear calculated total amount label
        drugNameSuggestionsListView.getItems().clear(); // Clear suggestions list
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
