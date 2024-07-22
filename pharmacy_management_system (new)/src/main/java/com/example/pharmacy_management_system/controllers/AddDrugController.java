package com.example.pharmacy_management_system.controllers;

import com.example.pharmacy_management_system.controllers.ControllerTraits.Navigator;
import com.example.pharmacy_management_system.controllers.ControllerTraits.Validator;
import com.example.pharmacy_management_system.models.Drug;
import com.example.pharmacy_management_system.models.Supplier;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.util.StringConverter;
import java.sql.*;
import java.util.HashMap;

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
    private Label drugNameErrorFeild;

    @FXML
    private Label descriptionErrorFeild;

    @FXML
    private Label quantityErrorFeild;

    @FXML
    private Label priceErrorFeild;

    @FXML
    private Label supplierErrorFeild;

    @FXML
    private ComboBox<Supplier> supplierComboBox;

    private Supplier supplierModel = new Supplier();

    @FXML
    public void initialize() throws SQLException {
        loadSuppliers();
    }


    private void loadSuppliers() throws SQLException {
        ObservableList<Supplier> suppliersList = supplierModel.listOfSupplier();
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

        System.out.println("Suppliers in ComboBox:");
        for (Supplier supplier : suppliersList) {
            System.out.println("Supplier ID: " + supplier.getId() + ", Name: " + supplier.getName() + ", Contact: " + supplier.getContact() + ", Address: " + supplier.getAddress());
        } 

    }

    @FXML
    public void handleSaveDrug(ActionEvent event) throws SQLException {
        String drugName = drugNameField.getText();
        String description = descriptionField.getText();
        String quantityStr = quantityField.getText();
        String priceStr = priceField.getText();
        Supplier selectedSupplier = supplierComboBox.getSelectionModel().getSelectedItem();

        //make validations of inputs 
        
        Label[] errorLabels = {
            drugNameErrorFeild,
            descriptionErrorFeild,
            quantityErrorFeild,
            priceErrorFeild
        };

        String[] validationInstructions = {
            "[drugName]{0}<"+drugName+">(required)",
            "[description]{1}<"+description+">(required)",
            "[quantity]{2}<"+quantityStr+">(required)(mustBeInteger)",
            "[price]{3}<"+priceStr+">(required)(mustBeFloat)",
        };

        HashMap<String, HashMap<String, Object>>  validate = Validator.validate(validationInstructions, errorLabels);

        if (selectedSupplier == null) {
            supplierErrorFeild.setText("Please select a supplier.");
            return;
        }else{supplierErrorFeild.setText("");}
        
        if(!(Boolean)validate.get("errorState").get("isvalid")){
            return;
        }else{
            HashMap<String,Object> data =  validate.get("validatedData");
            int supplierId = selectedSupplier.getId();

            Drug newDrug = new Drug();
            newDrug.setDrugName(data.get("drugName").toString());
            newDrug.setDescription(data.get("description").toString());
            newDrug.setQuantity(Integer.parseInt(data.get("quantity").toString()));
            newDrug.setPrice(Double.parseDouble(data.get("price").toString()));
            newDrug.setSupplierId(supplierId);

            if(newDrug.isSuccessfullyStoredInDatabase()){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Drug added successfully!");
                alert.showAndWait();

                supplierErrorFeild.setText("");

                Navigator.navigate("/com/example/pharmacy_management_system/view_drugs.fxml", event,loadingLabel);
    
            }else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Error adding drug.");
                alert.showAndWait();

                return;
            }


        }

    }



    public void handleBack(ActionEvent event) {
        Navigator.navigate(Navigator.getPreviousRoute(), event,loadingLabel);
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