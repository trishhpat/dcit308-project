package com.example.pharmacy_management_system.models;

import com.example.pharmacy_management_system.utils.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Supplier {
    private int id;
    private String name;
    private String contact;
    private String address;

    public Supplier(int id, String name, String contact, String address) {
        this.id = id;
        this.name = name;
        this.contact = contact;
        this.address = address;
    }

    public Supplier(String name, String contact, String address) {
        this.name = name;
        this.contact = contact;
        this.address = address;
    }



    public Supplier(){}

    // Getters and setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }






    public ObservableList<Supplier> listOfSupplier() throws SQLException{
        ObservableList<Supplier> suppliersList = FXCollections.observableArrayList();
        Connection connection = DatabaseConnection.getConnection();
        String query = "SELECT * FROM suppliers";
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String contact = resultSet.getString("contact");
                String address = resultSet.getString("address");

                // Create Supplier object and add to the suppliersList
                Supplier supplier = new Supplier(id, name, contact, address);
                suppliersList.add(supplier);
            }
            return   suppliersList;
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Method to fetch all drugs associated with this supplier
    public ObservableList<Drug> getAssociatedDrugs() {
        ObservableList<Drug> drugs = FXCollections.observableArrayList();
        String sql = "SELECT * FROM drugs WHERE supplier_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, this.getId());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String drugName = rs.getString("drug_name");
                String description = rs.getString("description");
                int quantity = rs.getInt("quantity");
                double price = rs.getDouble("price");

                Drug drug = new Drug(id, drugName, description, quantity, price, this.getId());
                drugs.add(drug);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return drugs;
    }


    public boolean isSuccessfullyStoredInDatabase(){
        try(Connection connection = DatabaseConnection.getConnection()) {
            String sql = "INSERT INTO suppliers (name, contact, address) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, this.name);
            statement.setString(2, this.contact);
            statement.setString(3, this.address);
            statement.executeUpdate();
            return true;  
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
