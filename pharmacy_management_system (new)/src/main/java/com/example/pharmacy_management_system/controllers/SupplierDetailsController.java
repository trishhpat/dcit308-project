package com.example.pharmacy_management_system.controllers;

import com.example.pharmacy_management_system.models.Drug;
import com.example.pharmacy_management_system.models.Supplier;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class SupplierDetailsController {
    @FXML
    private Label supplierNameLabel;

    @FXML
    private Label supplierContactLabel;

    @FXML
    private Label supplierAddressLabel;

    @FXML
    private TableView<Drug> drugsTable;

    @FXML
    private TableColumn<Drug, String> drugNameColumn;

    @FXML
    private TableColumn<Drug, Integer> quantityColumn;

    public void initialize() {
        drugNameColumn.setCellValueFactory(new PropertyValueFactory<>("drugName"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
    }

    public void initData(Supplier supplier) {
        supplierNameLabel.setText(supplier.getName());
        supplierContactLabel.setText(supplier.getContact());
        supplierAddressLabel.setText(supplier.getAddress());

        ObservableList<Drug> drugs = supplier.getAssociatedDrugs();
        drugsTable.setItems(drugs);
    }
}
