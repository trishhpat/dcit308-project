package com.example.pharmacy_management_system.controllers;

import com.example.pharmacy_management_system.models.Drug;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class UpdateDrugController {
    @FXML
    private TextField drugNameField;

    @FXML
    private TextField drugPriceField;

    @FXML
    private TextField drugDescriptionField;

    @FXML
    private TextField additionalQuantityField;

    @FXML
    private Label loadingLabel;

    private Drug drugModel = new Drug();

    private Drug drug;

    private Alert alert = new Alert(Alert.AlertType.ERROR);

    public void initData(Drug drug) {
        this.drug = drug;
        drugNameField.setText(drug.getDrugName());
        drugPriceField.setText(String.valueOf(drug.getPrice()));
        drugDescriptionField.setText(drug.getDescription());
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
    public void handleUpdate(ActionEvent event) {
        String newName = drugNameField.getText();
        double newPrice = Double.parseDouble(drugPriceField.getText());
        String newDescription = drugDescriptionField.getText();
        int additionalQuantity = 0;

        if (!additionalQuantityField.getText().isEmpty()) {
            try {
                additionalQuantity = Integer.parseInt(additionalQuantityField.getText());
            } catch (NumberFormatException e) {
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Please enter a valid number for additional quantity.");
                alert.showAndWait();
                return;
            }
        }

        if (drugModel.drugUpdateWithQuantity(drug.getId(), newName, newPrice, newDescription, additionalQuantity)) {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
        } else {
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Error updating drug.");
            alert.showAndWait();
        }
    }

    @FXML
    public void handleDelete(ActionEvent event) {
        // Show confirmation alert
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this drug?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            if (drugModel.drugIsDeletedSuccessfully(drug.getId())) {
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.close();
            } else {
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Error deleting drug.");
                alert.showAndWait();
            }
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
