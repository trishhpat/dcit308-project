package com.example.pharmacy_management_system.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;

import java.io.IOException;
import java.util.HashMap;

import com.example.pharmacy_management_system.controllers.ControllerTraits.Navigator;
import com.example.pharmacy_management_system.controllers.ControllerTraits.Validator;
import com.example.pharmacy_management_system.models.Supplier;

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

    @FXML
    private Label nameErrorFeild;

    @FXML
    private Label contactErrorFeild;

    @FXML
    private Label addressErrorFeild;

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
    private void handleAdd(ActionEvent event) {
        String name = nameField.getText();
        String contact = contactField.getText();
        String address = addressField.getText();

        Label[] errorLabels = {
            nameErrorFeild,
            contactErrorFeild,
            addressErrorFeild   
        };

        String[] validationInstructions = {
            "[drugName]{0}<"+name+">(required)",
            "[buyer]{1}<"+contact+">(required)",
            "[quantity]{2}<"+ address+">(required)"
        };

        HashMap<String, HashMap<String, Object>>  validate = Validator.validate(validationInstructions, errorLabels);

        if(!(Boolean)validate.get("errorState").get("isvalid")){
            System.out.println("there was an invalid input");
            return;
        }else{
        Supplier newSupplier = new Supplier(name,contact,address);
        if(newSupplier.isSuccessfullyStoredInDatabase()){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Success");
                    alert.setHeaderText(null);
                    alert.setContentText("Supplier added successfully added successfully!");
                    alert.showAndWait();
                    clearFields();
                    Navigator.navigate("/com/example/pharmacy_management_system/view_suppliers.fxml", event,loadingLabel);
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Error adding Supplier.");
            alert.showAndWait();
        }
        }
    }

    private void clearFields() {
        nameField.clear();
        contactField.clear();
        addressField.clear();
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