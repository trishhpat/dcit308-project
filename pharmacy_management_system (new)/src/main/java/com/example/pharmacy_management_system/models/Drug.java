package com.example.pharmacy_management_system.models;

import javafx.beans.property.*;

import java.util.List;

public class Drug {
    private final IntegerProperty id;
    private final StringProperty drugName;
    private final StringProperty description;
    private final IntegerProperty quantity;
    private final DoubleProperty price;
    private final IntegerProperty supplier_id;
    private int remainingQuantity; // Track remaining quantity based on purchase history




    private String supplierName; // Add this field


    public Drug(int id, String drugName, String description, int quantity, double price, int supplierId) {
        this.id = new SimpleIntegerProperty(id);
        this.drugName = new SimpleStringProperty(drugName);
        this.description = new SimpleStringProperty(description);
        this.quantity = new SimpleIntegerProperty(quantity);
        this.price = new SimpleDoubleProperty(price);
        this.supplier_id = new SimpleIntegerProperty(supplierId);
        this.remainingQuantity = quantity; // Initialize remaining quantity with initial quantity

    }

    // Getters and Setters for id
    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public IntegerProperty idProperty() {
        return id;
    }

    // Getters and Setters for drugName
    public String getDrugName() {
        return drugName.get();
    }

    public void setDrugName(String drugName) {
        this.drugName.set(drugName);
    }

    public StringProperty drugNameProperty() {
        return drugName;
    }

    // Getters and Setters for description
    public String getDescription() {
        return description.get();
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    // Getters and Setters for quantity
    public int getQuantity() {
        return quantity.get();
    }

    public void setQuantity(int quantity) {
        this.quantity.set(quantity);
    }

    public IntegerProperty quantityProperty() {
        return quantity;
    }

    // Getters and Setters for price
    public double getPrice() {
        return price.get();
    }

    public void setPrice(double price) {
        this.price.set(price);
    }

    public DoubleProperty priceProperty() {
        return price;
    }
    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public int getRemainingQuantity() {
        return remainingQuantity;
    }

    public void setRemainingQuantity(int remainingQuantity) {
        this.remainingQuantity = remainingQuantity;
    }

    // Calculate remaining quantity based on purchase history
    public void updateRemainingQuantity(List<PurchaseHistory> purchaseHistory) {
        int initialQuantity = this.getQuantity();
        int totalBought = 0;

        // Sum up quantities bought for this drug
        for (PurchaseHistory purchase : purchaseHistory) {
            if (purchase.getDrugName().equals(this.getDrugName())) {
                totalBought += purchase.getQuantity();
            }
        }

        // Calculate remaining quantity
        int remaining = initialQuantity - totalBought;
        this.setRemainingQuantity(remaining);
    }


}
