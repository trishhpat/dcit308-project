package com.example.pharmacy_management_system.controllers;

import com.example.pharmacy_management_system.models.Supplier;
import com.example.pharmacy_management_system.utils.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;


public class ViewSuppliersController {
    @FXML
    private Button addButton;

    @FXML
    private TableView<Supplier> suppliersTable;

    @FXML
    private TableColumn<Supplier, String> supplierNameColumn;

    @FXML
    private TableColumn<Supplier, String> supplierContactColumn;

    @FXML
    private TableColumn<Supplier, String> supplierAddressColumn;

    @FXML
    private Label loadingLabel;

    private Supplier supplierModel = new Supplier();

    @FXML
    public void initialize() throws SQLException {
        supplierNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        supplierContactColumn.setCellValueFactory(new PropertyValueFactory<>("contact"));
        supplierAddressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        loadSuppliers();

        // Handle double-click on suppliersTable
        suppliersTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Supplier selectedSupplier = suppliersTable.getSelectionModel().getSelectedItem();
                if (selectedSupplier != null) {
                    showSupplierDetailsPopup(selectedSupplier);
                }
            }
        });
    }

    private void loadSuppliers() throws SQLException {
        ObservableList<Supplier> suppliers = FXCollections.observableArrayList();
        suppliers = supplierModel.listOfSupplier();
        suppliersTable.setItems(suppliers);
    }

    private void showSupplierDetailsPopup(Supplier supplier) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/pharmacy_management_system/supplier_details.fxml"));
            Parent root = loader.load();

            SupplierDetailsController controller = loader.getController();
            controller.initData(supplier);

            Stage popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.setTitle("Supplier Details");
            popupStage.setScene(new Scene(root));
            popupStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
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

    public void handleAddSupplier(ActionEvent event) {
        try {
            Parent homePage = FXMLLoader.load(getClass().getResource("/com/example/pharmacy_management_system/add_supplier.fxml"));
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
