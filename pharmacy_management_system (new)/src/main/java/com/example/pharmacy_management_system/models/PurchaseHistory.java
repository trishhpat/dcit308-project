package com.example.pharmacy_management_system.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.example.pharmacy_management_system.utils.DatabaseConnection;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class PurchaseHistory {
    private int id; // Assuming auto-incremented primary key from the database
    private String drug_name; // Foreign key referencing Drug
    private LocalDateTime purchaseDate;
    private String buyer;
    private int quantity;
    private double totalAmount;


    public PurchaseHistory(String drug_name, LocalDateTime purchaseDate, String buyer, int quantity, double totalAmount) {
        this.drug_name = drug_name;
        this.purchaseDate = purchaseDate;
        this.buyer = buyer;
        this.quantity = quantity;
        this.totalAmount = totalAmount;
    }
    
    public PurchaseHistory(){}



    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDrugName() {
        return drug_name;
    }

    public void setDrugName(String drug_name) {
        this.drug_name = drug_name;
    }

    public LocalDateTime getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDateTime purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public String getBuyer() {
        return buyer;
    }

    public void setBuyer(String buyer) {
        this.buyer = buyer;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    @Override
    public String toString() {
        return "PurchaseHistory{" +
                "id=" + id +
                ", drug_name=" + drug_name +
                ", purchaseDate=" + purchaseDate +
                ", buyer='" + buyer + '\'' +
                ", quantity=" + quantity +
                ", totalAmount=" + totalAmount +
                '}';
    }


    public boolean  isStoredInDatabaeSuccessfully() {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "INSERT INTO purchase_history (drug_name, buyer, quantity, total_amount, purchase_date) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, this.drug_name);
            statement.setString(2, this.buyer);
            statement.setInt(3, this.quantity);
            statement.setDouble(4, this.totalAmount);
            statement.setTimestamp(5, Timestamp.valueOf(this.purchaseDate));

            statement.executeUpdate();
            statement.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public ObservableList<PurchaseHistory>  listOfPurchases(){
        ObservableList<PurchaseHistory> purchaseHistories = FXCollections.observableArrayList();

        try(Connection connection = DatabaseConnection.getConnection()) {
            String sql = "SELECT ph.*, d.drug_name " +
                    "FROM purchase_history ph " +
                    "JOIN drugs d ON ph.drug_name = d.drug_name";

            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                int id = rs.getInt("id");
                String drugName = rs.getString("drug_name");
                LocalDateTime purchaseDate = rs.getTimestamp("purchase_date").toLocalDateTime();
                String buyer = rs.getString("buyer");
                int quantity = rs.getInt("quantity");
                double totalAmount = rs.getDouble("total_amount");

                PurchaseHistory purchaseHistory = new PurchaseHistory(drugName, purchaseDate, buyer, quantity, totalAmount);
                purchaseHistory.setId(id); // Set the ID retrieved from the database
                purchaseHistories.add(purchaseHistory);
            }
            return purchaseHistories;
        } catch (Exception e) {
            e.printStackTrace();
        } 
        return null;
    }

}
