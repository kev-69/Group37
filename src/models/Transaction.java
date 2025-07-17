package models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * Transaction model representing a purchase transaction
 */
public class Transaction {
    private String transactionId;
    private String drugCode;
    private String customerId;
    private int quantity;
    private double unitPrice;
    private double totalCost;
    private LocalDateTime timestamp;
    private String transactionType; // "SALE" or "PURCHASE"

    public Transaction(String transactionId, String drugCode, String customerId,
            int quantity, double unitPrice, String transactionType) {
        this.transactionId = transactionId;
        this.drugCode = drugCode;
        this.customerId = customerId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.totalCost = quantity * unitPrice;
        this.timestamp = LocalDateTime.now();
        this.transactionType = transactionType;
    }

    public Transaction(String transactionId, String drugCode, String customerId,
            int quantity, double unitPrice, LocalDateTime timestamp, String transactionType) {
        this.transactionId = transactionId;
        this.drugCode = drugCode;
        this.customerId = customerId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.totalCost = quantity * unitPrice;
        this.timestamp = timestamp;
        this.transactionType = transactionType;
    }

    // Getters
    public String getTransactionId() {
        return transactionId;
    }

    public String getDrugCode() {
        return drugCode;
    }

    public String getCustomerId() {
        return customerId;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getTransactionType() {
        return transactionType;
    }

    // Setters
    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public void setDrugCode(String drugCode) {
        this.drugCode = drugCode;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
        this.totalCost = quantity * unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
        this.totalCost = quantity * unitPrice;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    // Utility methods
    public String getFormattedTimestamp() {
        return timestamp.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public String getDateOnly() {
        return timestamp.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    public boolean isToday() {
        return timestamp.toLocalDate().equals(java.time.LocalDate.now());
    }

    public boolean isSale() {
        return "SALE".equals(transactionType);
    }

    public boolean isPurchase() {
        return "PURCHASE".equals(transactionType);
    }

    @Override
    public String toString() {
        return String.format(
                "Transaction{id='%s', drug='%s', customer='%s', qty=%d, price=%.2f, total=%.2f, time='%s', type='%s'}",
                transactionId, drugCode, customerId, quantity, unitPrice, totalCost, getFormattedTimestamp(),
                transactionType);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Transaction transaction = (Transaction) obj;
        return Objects.equals(transactionId, transaction.transactionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(transactionId);
    }
}
