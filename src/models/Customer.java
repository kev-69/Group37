package models;

import java.util.Objects;

/**
 * Customer model representing a pharmacy customer
 */
public class Customer {
    private String customerId;
    private String name;
    private String contact;
    private String email;
    private String address;
    private String[] transactionHistory; // Array to store transaction IDs
    private int transactionCount;

    public Customer(String customerId, String name, String contact) {
        this.customerId = customerId;
        this.name = name;
        this.contact = contact;
        this.transactionHistory = new String[100]; // Max 100 transactions per customer
        this.transactionCount = 0;
    }

    public Customer(String customerId, String name, String contact, String email, String address) {
        this(customerId, name, contact);
        this.email = email;
        this.address = address;
    }

    // Getters
    public String getCustomerId() {
        return customerId;
    }

    public String getName() {
        return name;
    }

    public String getContact() {
        return contact;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public String[] getTransactionHistory() {
        return transactionHistory;
    }

    public int getTransactionCount() {
        return transactionCount;
    }

    // Setters
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    // Transaction history management
    public void addTransaction(String transactionId) {
        if (transactionCount < transactionHistory.length) {
            transactionHistory[transactionCount] = transactionId;
            transactionCount++;
        }
    }

    public String getLastTransaction() {
        if (transactionCount > 0) {
            return transactionHistory[transactionCount - 1];
        }
        return null;
    }

    public String[] getRecentTransactions(int count) {
        int actualCount = Math.min(count, transactionCount);
        String[] recent = new String[actualCount];

        for (int i = 0; i < actualCount; i++) {
            recent[i] = transactionHistory[transactionCount - actualCount + i];
        }

        return recent;
    }

    @Override
    public String toString() {
        return String.format("Customer{id='%s', name='%s', contact='%s', email='%s', transactions=%d}",
                customerId, name, contact, email != null ? email : "N/A", transactionCount);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Customer customer = (Customer) obj;
        return Objects.equals(customerId, customer.customerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customerId);
    }
}
