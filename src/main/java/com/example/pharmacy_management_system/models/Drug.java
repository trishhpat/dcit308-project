package com.example.pharmacy_management_system.models;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.example.pharmacy_management_system.utils.DatabaseConnection;

public class Drug {
    private int id;
    private String drugName;
    private String description;
    private int quantity;
    private double price;
    private int supplier_id;
    private int remainingQuantity; // Track remaining quantity based on purchase history
    private String supplierName; // Add this field

    public Drug(){}

    public Drug(String drugName, String description, int quantity, double price, int supplierId) {
        this.drugName = drugName ;
        this.description = description;
        this.quantity = quantity;
        this.price = price;
        this.supplier_id = supplierId;
    }

    public Drug(int id, String drugName, String description, int quantity, double price, int supplierId) {
        this.id = id;
        this.drugName = drugName ;
        this.description = description;
        this.quantity = quantity;
        this.price = price;
        this.supplier_id = supplierId;
    }

    

    // Getters and Setters for id
    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id ; 
    }


    // Getters and Setters for drugName
    public String getDrugName() {
        return this.drugName;
    }

    public void setDrugName(String drugName) {
        this.drugName = drugName;
    }


    // Getters and Setters for description
    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // Getters and Setters for quantity
    public int getQuantity() {
        return this.quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    // Getters and Setters for price
    public double getPrice() {
        return this.price;
    }

    public void setPrice(double price) {
        this.price = price;
    }


    public int getRemainingQuantity() {
        return remainingQuantity;
    }

    public void setRemainingQuantity(int remainingQuantity) {
        this.remainingQuantity = remainingQuantity;
    }



    public boolean isSuccessfullyStoredInDatabase() throws SQLException{
        Connection connection = DatabaseConnection.getConnection();
        String insertQuery = "INSERT INTO drugs (drug_name, description, quantity, price, supplier_id) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(insertQuery)) {
            pstmt.setString(1, this.drugName);
            pstmt.setString(2, this.description);
            pstmt.setInt(3, this.quantity);
            pstmt.setDouble(4, this.price);
            pstmt.setInt(5, this.supplier_id);

            pstmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; 
    }


    
    public ObservableList<Drug> getDrugsInDatabase(){
        ObservableList<Drug>  drugs = FXCollections.observableArrayList();

        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM drugs";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Drug drugItem = new Drug(   resultSet.getInt("id"),
                                            resultSet.getString("drug_name"), 
                                            resultSet.getString("description"),
                                            resultSet.getInt("quantity"), 
                                            resultSet.getDouble("price"),
                                            resultSet.getInt("supplier_id")
                                            );                    
                drugs.add(drugItem);
            }
            return drugs;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean drugUpdateWithQuantity(int id, String newName, double newPrice, String newDescription, int additionalQuantity) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String sql = "UPDATE drugs SET drug_name = ?, price = ?, description = ?, quantity = quantity + ? WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, newName);
            statement.setDouble(2, newPrice);
            statement.setString(3, newDescription);
            statement.setInt(4, additionalQuantity);
            statement.setInt(5, id);

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public ObservableList<Drug> searchDrugByName(String searchTerm)throws Exception{
        ObservableList<Drug>  searchResults = FXCollections.observableArrayList();

        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM drugs WHERE LOWER(drug_name) LIKE LOWER(?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, "%" + searchTerm.toLowerCase() + "%");

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String drugName = resultSet.getString("drug_name");
                String description = resultSet.getString("description");
                int quantity = resultSet.getInt("quantity");
                double price = resultSet.getDouble("price");
                int supplier_id = resultSet.getInt("supplier_id");

                Drug drug = new Drug(id, drugName, description, quantity, price, supplier_id);
                searchResults.add(drug);
            }
            resultSet.close();
            statement.close();
            return searchResults;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }



    public boolean drugUpdateWasSuccessfulOnPurchaseWithQuantity(int id, int initialQuantiy, int purchaseQuantity){
        int newQuantity = initialQuantiy;
        try (Connection connection = DatabaseConnection.getConnection()) {
            String sql = "UPDATE drugs SET quantity=?  WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, newQuantity);
            statement.setInt(2, id);
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    public boolean drugIsDeletedSuccessfully(int id){
        try (Connection connection = DatabaseConnection.getConnection()) {
            String sql = "DELETE FROM drugs WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;

    }








//    public boolean drugUpdateWasSuccessfulOnMoreOrderWithQuantity(int id, int initialQuantiy, int orderQuantity){
//        int newQuantity = initialQuantiy + orderQuantity;
//        try (Connection connection = DatabaseConnection.getConnection()) {
//            String sql = "UPDATE drugs SET quantity=?  WHERE id = ?";
//            PreparedStatement statement = connection.prepareStatement(sql);
//            statement.setInt(1, newQuantity);
//            statement.setInt(2, id);
//            statement.executeUpdate();
//
//            return true;
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return false;
//    }

    //    public boolean drugUpdateWasSuccessfulNoQuantity(int id, String name,double price, String description){
//        try (Connection connection = DatabaseConnection.getConnection()) {
//            String sql = "UPDATE drugs SET drug_name = ?, price = ?, description = ? WHERE id = ?";
//            PreparedStatement statement = connection.prepareStatement(sql);
//            statement.setString(1,name);
//            statement.setDouble(2, price);
//            statement.setString(3,description);
//            statement.setInt(4, id);
//            statement.executeUpdate();
//            return true;
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return false;
//    }

    // Calculate remaining quantity based on purchase history
//    public void updateRemainingQuantity(List<PurchaseHistory> purchaseHistory) {
//        int initialQuantity = this.getQuantity();
//        int totalBought = 0;
//
//        // Sum up quantities bought for this drug
//        for (PurchaseHistory purchase : purchaseHistory) {
//            if (purchase.getDrugName().equals(this.getDrugName())) {
//                totalBought += purchase.getQuantity();
//            }
//        }
//
//        // Calculate remaining quantity
//        int remaining = initialQuantity - totalBought;
//        this.setRemainingQuantity(remaining);
//    }
}
