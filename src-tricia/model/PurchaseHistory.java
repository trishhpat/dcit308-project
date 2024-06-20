package model;

import java.time.LocalDateTime;

public class PurchaseHistory {
    private int id; // Assuming auto-incremented primary key from the database
    private int drugId; // Foreign key referencing Drug
    private LocalDateTime purchaseDate;
    private String buyer;
    private int quantity;
    private double totalAmount;

    public PurchaseHistory(int drugId, LocalDateTime purchaseDate, String buyer, int quantity, double totalAmount) {
        this.drugId = drugId;
        this.purchaseDate = purchaseDate;
        this.buyer = buyer;
        this.quantity = quantity;
        this.totalAmount = totalAmount;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDrugId() {
        return drugId;
    }

    public void setDrugId(int drugId) {
        this.drugId = drugId;
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
                ", drugId=" + drugId +
                ", purchaseDate=" + purchaseDate +
                ", buyer='" + buyer + '\'' +
                ", quantity=" + quantity +
                ", totalAmount=" + totalAmount +
                '}';
    }
}
