package com.example.pharmacy_management_system.services;

import com.example.pharmacy_management_system.models.PurchaseHistory;
import com.example.pharmacy_management_system.utils.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDateTime;

public class PurchaseHistoryService {
    public ObservableList<PurchaseHistory> getAllPurchaseHistories() {
        ObservableList<PurchaseHistory> purchaseHistories = FXCollections.observableArrayList();

        String sql = "SELECT * FROM purchase_history";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String drugName = rs.getString("drug_name");
                LocalDateTime purchaseDate = rs.getTimestamp("purchase_date").toLocalDateTime();
                String buyer = rs.getString("buyer");
                int quantity = rs.getInt("quantity");
                double totalAmount = rs.getDouble("total_amount");

                PurchaseHistory purchaseHistory = new PurchaseHistory(drugName, purchaseDate, buyer, quantity, totalAmount);
                purchaseHistories.add(purchaseHistory);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return purchaseHistories;
    }
}
