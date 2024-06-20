package controller;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class LandingPageController {
    @FXML
    private void handleAddDrugs() throws IOException {
        navigate("/view/add_drug.fxml", "Add Drugs");
    }

    @FXML
    private void handleSearchDrugs() throws IOException {
        navigate("/view/search_drug.fxml", "Search Drugs");
    }

    @FXML
    private void handleViewSuppliers() throws IOException {
        navigate("/view/view_suppliers.fxml", "View Suppliers");
    }

    @FXML
    private void handleViewDrugs() throws IOException {
        navigate("/view/view_drugs.fxml", "View Drugs");
    }

    @FXML
    private void handleViewPurchaseHistory() throws IOException {
        navigate("/view/view_purchase_history.fxml", "View Purchase History");
    }

    @FXML
    private void handleStatisticsAndReports() throws IOException {
        navigate("/view/statistics.fxml", "Statistics and Reports");
    }

    private void navigate(String fxmlPath, String title) throws IOException {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
        stage.setTitle(title);
        stage.setScene(new Scene(root));
        stage.show();
    }


}
