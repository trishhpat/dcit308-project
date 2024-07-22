package com.example.pharmacy_management_system.controllers;

import com.example.pharmacy_management_system.controllers.ControllerTraits.Navigator;
import com.example.pharmacy_management_system.controllers.ControllerTraits.Validator;
import com.example.pharmacy_management_system.models.Drug;
import com.example.pharmacy_management_system.models.PurchaseHistory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import java.sql.SQLException;
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
    private Label drugErrorFeild;

    @FXML
    private Label buyerErrorFeild;

    @FXML
    private Label quantityErrorFeild;


    @FXML
    private ListView<String> drugNameSuggestionsListView;

    //declare the system Models here  Model here 
    private Drug drugModel = new Drug();


    // Data structure to store drug names and prices for suggestions and calculations
    private Map<String, Drug> drugNamePriceMap = new HashMap<>();

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
            drugNamePriceMap.put(drug.getDrugName(), drug);
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
            Parent homePage = FXMLLoader.load(getClass().getResource(Navigator.getPreviousRoute()));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.getScene().setRoot(homePage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAdd(ActionEvent event) throws SQLException {
      

            Label[] errorLabels = {
                    drugErrorFeild,
                    buyerErrorFeild,
                    quantityErrorFeild   
            };

            String[] validationInstructions = {
                "[drugName]{0}<"+drugNameField.getText()+">(required)",
                "[buyer]{1}<"+buyerField.getText()+">(required)",
                "[quantity]{2}<"+ quantityField.getText() +">(required)(mustBeInteger)"
            };

            HashMap<String, HashMap<String, Object>>  validate = Validator.validate(validationInstructions, errorLabels);

            if(!(Boolean)validate.get("errorState").get("isvalid")){
                System.out.println("there was an invalid input");
                return;
            }else{

                String drugName = drugNameField.getText().trim();
                String buyer = buyerField.getText().trim();
                int quantity = Integer.parseInt(quantityField.getText().trim());
                double totalAmount = calculateTotalAmount(drugName, quantity);
                LocalDateTime currentDate = LocalDateTime.now();

                PurchaseHistory newPurchase = new PurchaseHistory(drugName,currentDate,buyer,quantity,totalAmount);
                if(newPurchase.isStoredInDatabaseSuccessfully() && drugModel.drugUpdateWasSuccessfulOnPurchaseWithQuantity(drugNamePriceMap.get(drugName).getId(), drugNamePriceMap.get(drugName).getQuantity(), quantity)){
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Success");
                    alert.setHeaderText(null);
                    alert.setContentText("Purchase recorded successfully added successfully!");
                    alert.showAndWait();
                    clearFields();
                    Navigator.navigate("/com/example/pharmacy_management_system/view_purchase_history.fxml", event,loadingLabel);                   
                }else{
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Error adding Purchase.");
                    alert.showAndWait();
                }

            }
    
            
        

    }


    private double calculateTotalAmount(String drugName, int quantity) throws SQLException {
        if (drugNamePriceMap.containsKey(drugName) && quantity <= drugNamePriceMap.get(drugName).getQuantity()) {
            double price = drugNamePriceMap.get(drugName).getPrice();
            return quantity * price;
        }else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Quantity is more than available you only have " + drugNamePriceMap.get(drugName).getQuantity() + " Available " );
            alert.showAndWait();
            throw new IllegalArgumentException("Drug name not found in database: " + drugName);
        }
    }


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

    public void handleAddDrugs(ActionEvent event) {
        Navigator.navigate("/com/example/pharmacy_management_system/add_drug.fxml", event,loadingLabel);
    }

    public void handleSearchDrugs(ActionEvent event) {
        Navigator.navigate("/com/example/pharmacy_management_system/search_drug.fxml", event,loadingLabel);
    }

    public void handleViewSuppliers(ActionEvent event) {
        Navigator.navigate("/com/example/pharmacy_management_system/view_suppliers.fxml", event,loadingLabel);
    }

    public void handleViewDrugs(ActionEvent event) {
        Navigator.navigate("/com/example/pharmacy_management_system/view_drugs.fxml", event,loadingLabel);
    }

    public void handleViewPurchaseHistory(ActionEvent event) {
        Navigator.navigate("/com/example/pharmacy_management_system/view_purchase_history.fxml", event,loadingLabel);
    }

    public void handleStatisticsAndReports(ActionEvent event) {
        Navigator.navigate("/com/example/pharmacy_management_system/statistics.fxml", event,loadingLabel);
    }
}