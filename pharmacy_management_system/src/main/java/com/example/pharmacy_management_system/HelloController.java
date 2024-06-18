package com.example.pharmacy_management_system;


import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.event.ActionEvent;

import java.io.IOException;

public class HelloController {


    public void navigate(String fxml, ActionEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.getScene().setRoot(root);
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
        try {
            navigate("/com/example/pharmacy_management_system/add_drug.fxml", event);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void handleSearchDrugs(ActionEvent event) {
        try {
            navigate("/com/example/pharmacy_management_system/search_drug.fxml", event);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void handleViewSuppliers(ActionEvent event) {
        try {
            navigate("/com/example/pharmacy_management_system/view_suppliers.fxml", event);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void handleViewDrugs(ActionEvent event) {
        try {
            navigate("/com/example/pharmacy_management_system/view_drugs.fxml", event);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void handleViewPurchaseHistory(ActionEvent event) {
        try {
            navigate("/com/example/pharmacy_management_system/view_purchase_history.fxml", event);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void handleStatisticsAndReports(ActionEvent event) {
        try {
            navigate("/com/example/pharmacy_management_system/statistics.fxml", event);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
