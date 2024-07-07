package com.example.pharmacy_management_system.services;

import com.example.pharmacy_management_system.models.Drug;
import com.example.pharmacy_management_system.utils.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class DrugService {
    public ObservableList<Drug> getAllDrugs() {
        ObservableList<Drug> drugs = FXCollections.observableArrayList();

        String sql = "SELECT * FROM drugs";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String description = rs.getString("description");
                int quantity = rs.getInt("quantity");
                double price = rs.getDouble("price");
                int supplierId = rs.getInt("supplier_id");

                Drug drug = new Drug(id, name, description, quantity, price, supplierId);
                drugs.add(drug);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return drugs;
    }
}
