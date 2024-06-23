package com.example.pharmacy_management_system.services;

import com.example.pharmacy_management_system.models.Drug;
import com.example.pharmacy_management_system.models.PurchaseHistory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PharmacyService {
    private Map<String, Integer> remainingQuantities = new HashMap<>();

    // Update remaining quantities based on purchase history
    public void updateRemainingQuantities(List<Drug> drugs, List<PurchaseHistory> purchaseHistory) {
        for (Drug drug : drugs) {
            int initialQuantity = drug.getQuantity();
            int totalBought = 0;

            // Sum up quantities bought for this drug
            for (PurchaseHistory purchase : purchaseHistory) {
                if (purchase.getDrugName().equals(drug.getDrugName())) {
                    totalBought += purchase.getQuantity();
                }
            }

            // Calculate remaining quantity
            int remaining = initialQuantity - totalBought;
            drug.setRemainingQuantity(remaining);
            remainingQuantities.put(drug.getDrugName(), remaining);
        }
    }

    // Check if purchase quantity is valid based on remaining quantity
    public boolean canPurchase(String drugName, int quantityToPurchase) {
        Integer remaining = remainingQuantities.get(drugName);
        if (remaining != null && remaining >= quantityToPurchase) {
            return true;
        }
        return false;
    }

    // Get remaining quantities map for external use
    public Map<String, Integer> getRemainingQuantities() {
        return remainingQuantities;
    }
}
