package com.example.pharmacy_management_system.controllers;

import com.example.pharmacy_management_system.utils.DatabaseConnection;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import com.opencsv.CSVWriter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class StatisticsController {
    @FXML
    private Button addButton;
    @FXML
    private Label loadingLabel;


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

    public void handleViewReport(ActionEvent event) {
        navigate("/com/example/pharmacy_management_system/drug_report.fxml", event);
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

    public void handleDrugStats(ActionEvent event) {
        navigate("/com/example/pharmacy_management_system/drug_report.fxml", event);
    }
    public void handleSupplierData(ActionEvent event) {
        navigate("/com/example/pharmacy_management_system/supplier_statistics.fxml", event);
    }
//    public void handlePurchases(ActionEvent event) {
//        navigate("/com/example/pharmacy_management_system/.fxml", event);
//    }

    @FXML
    private void handleStatistics() {
        // Handle add button action
    }

    @FXML
    private void handleExportDataDrugs(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));
        File file = fileChooser.showSaveDialog(((Node) event.getSource()).getScene().getWindow());
        if (file != null) {
            String extension = getFileExtension(file);
            if ("csv".equals(extension)) {
                exportToCSVDrugs(file);
            } else if ("xlsx".equals(extension)) {
                exportToExcelDrugs(file);
            }
        }
    }

    @FXML
    private void handleExportDataSuppliers(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));
        File file = fileChooser.showSaveDialog(((Node) event.getSource()).getScene().getWindow());
        if (file != null) {
            String extension = getFileExtension(file);
            if ("csv".equals(extension)) {
                exportToCSVSuppliers(file);
            } else if ("xlsx".equals(extension)) {
                exportToExcelSuppliers(file);
            }
        }
    }

    @FXML
    private void handleExportDataPurchase(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));
        File file = fileChooser.showSaveDialog(((Node) event.getSource()).getScene().getWindow());
        if (file != null) {
            String extension = getFileExtension(file);
            if ("csv".equals(extension)) {
                exportToCSVPurchase(file);
            } else if ("xlsx".equals(extension)) {
                exportToExcelPurchase(file);
            }
        }
    }

    private String getFileExtension(File file) {
        String name = file.getName();
        int lastIndexOf = name.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return ""; // empty extension
        }
        return name.substring(lastIndexOf + 1);
    }

    private void exportToCSVDrugs(File file) {
        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM drugs");
             CSVWriter writer = new CSVWriter(new FileWriter(file))) {

            // Write header
//            writer.writeNext(resultSet.getMetaData().getColumnNames());

            // Write data
            while (resultSet.next()) {
                int columnCount = resultSet.getMetaData().getColumnCount();
                String[] row = new String[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    row[i - 1] = resultSet.getString(i);
                }
                writer.writeNext(row);
            }

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }
    private void exportToCSVSuppliers(File file) {
        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM suppliers");
             CSVWriter writer = new CSVWriter(new FileWriter(file))) {

            // Write header
//            writer.writeNext(resultSet.getMetaData().getColumnNames());

            // Write data
            while (resultSet.next()) {
                int columnCount = resultSet.getMetaData().getColumnCount();
                String[] row = new String[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    row[i - 1] = resultSet.getString(i);
                }
                writer.writeNext(row);
            }

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    private void exportToCSVPurchase(File file) {
        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM purchase_history");
             CSVWriter writer = new CSVWriter(new FileWriter(file))) {

            // Write header
//            writer.writeNext(resultSet.getMetaData().getColumnNames());

            // Write data
            while (resultSet.next()) {
                int columnCount = resultSet.getMetaData().getColumnCount();
                String[] row = new String[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    row[i - 1] = resultSet.getString(i);
                }
                writer.writeNext(row);
            }

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    private void exportToExcelDrugs(File file) {
        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM drugs");
             Workbook workbook = new XSSFWorkbook()) {

            Sheet sheet = workbook.createSheet("Data");

            // Write header
            Row headerRow = sheet.createRow(0);
            for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                headerRow.createCell(i - 1).setCellValue(resultSet.getMetaData().getColumnName(i));
            }

            // Write data
            int rowIndex = 1;
            while (resultSet.next()) {
                Row row = sheet.createRow(rowIndex++);
                for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                    row.createCell(i - 1).setCellValue(resultSet.getString(i));
                }
            }

            try (FileOutputStream fileOut = new FileOutputStream(file)) {
                workbook.write(fileOut);
            }

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    private void exportToExcelSuppliers(File file) {
        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM suppliers");
             Workbook workbook = new XSSFWorkbook()) {

            Sheet sheet = workbook.createSheet("Data");

            // Write header
            Row headerRow = sheet.createRow(0);
            for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                headerRow.createCell(i - 1).setCellValue(resultSet.getMetaData().getColumnName(i));
            }

            // Write data
            int rowIndex = 1;
            while (resultSet.next()) {
                Row row = sheet.createRow(rowIndex++);
                for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                    row.createCell(i - 1).setCellValue(resultSet.getString(i));
                }
            }

            try (FileOutputStream fileOut = new FileOutputStream(file)) {
                workbook.write(fileOut);
            }

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    private void exportToExcelPurchase(File file) {
        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM purchase_history");
             Workbook workbook = new XSSFWorkbook()) {

            Sheet sheet = workbook.createSheet("Data");

            // Write header
            Row headerRow = sheet.createRow(0);
            for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                headerRow.createCell(i - 1).setCellValue(resultSet.getMetaData().getColumnName(i));
            }

            // Write data
            int rowIndex = 1;
            while (resultSet.next()) {
                Row row = sheet.createRow(rowIndex++);
                for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                    row.createCell(i - 1).setCellValue(resultSet.getString(i));
                }
            }

            try (FileOutputStream fileOut = new FileOutputStream(file)) {
                workbook.write(fileOut);
            }

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }


}
