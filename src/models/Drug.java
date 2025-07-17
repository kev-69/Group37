package models;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Drug model representing a medication in the pharmacy inventory
 */
public class Drug {
    private String drugCode;
    private String name;
    private String[] suppliers; // Array to store multiple suppliers
    private LocalDate expirationDate;
    private double price;
    private int stockLevel;
    private int minStockThreshold;

    public Drug(String drugCode, String name, double price, int stockLevel, LocalDate expirationDate) {
        this.drugCode = drugCode;
        this.name = name;
        this.price = price;
        this.stockLevel = stockLevel;
        this.expirationDate = expirationDate;
        this.suppliers = new String[5]; // Max 5 suppliers per drug
        this.minStockThreshold = 10; // Default threshold
    }

    // Getters
    public String getDrugCode() {
        return drugCode;
    }

    public String getName() {
        return name;
    }

    public String[] getSuppliers() {
        return suppliers;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public double getPrice() {
        return price;
    }

    public int getStockLevel() {
        return stockLevel;
    }

    public int getMinStockThreshold() {
        return minStockThreshold;
    }

    // Setters
    public void setDrugCode(String drugCode) {
        this.drugCode = drugCode;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setStockLevel(int stockLevel) {
        this.stockLevel = stockLevel;
    }

    public void setMinStockThreshold(int minStockThreshold) {
        this.minStockThreshold = minStockThreshold;
    }

    // Supplier management
    public void addSupplier(String supplier) {
        for (int i = 0; i < suppliers.length; i++) {
            if (suppliers[i] == null) {
                suppliers[i] = supplier;
                return;
            }
        }
    }

    public void removeSupplier(String supplier) {
        for (int i = 0; i < suppliers.length; i++) {
            if (Objects.equals(suppliers[i], supplier)) {
                suppliers[i] = null;
                return;
            }
        }
    }

    public boolean hasSupplier(String supplier) {
        for (String s : suppliers) {
            if (Objects.equals(s, supplier)) {
                return true;
            }
        }
        return false;
    }

    // Stock operations
    public void addStock(int quantity) {
        this.stockLevel += quantity;
    }

    public boolean reduceStock(int quantity) {
        if (stockLevel >= quantity) {
            stockLevel -= quantity;
            return true;
        }
        return false;
    }

    public boolean isLowStock() {
        return stockLevel <= minStockThreshold;
    }

    public boolean isExpired() {
        return LocalDate.now().isAfter(expirationDate);
    }

    @Override
    public String toString() {
        StringBuilder supplierList = new StringBuilder();
        for (String supplier : suppliers) {
            if (supplier != null) {
                if (supplierList.length() > 0)
                    supplierList.append(", ");
                supplierList.append(supplier);
            }
        }

        return String.format("Drug{code='%s', name='%s', price=%.2f, stock=%d, expiry=%s, suppliers=[%s]}",
                drugCode, name, price, stockLevel, expirationDate, supplierList.toString());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Drug drug = (Drug) obj;
        return Objects.equals(drugCode, drug.drugCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(drugCode);
    }
}
