package models;

import java.util.Objects;

/**
 * Supplier model representing a drug supplier
 */
public class Supplier {
    private String supplierId;
    private String name;
    private String contact;
    private String email;
    private String location;
    private int deliveryTurnaroundDays;
    private String[] suppliedDrugs; // Array to store drug codes
    private int drugCount;

    public Supplier(String supplierId, String name, String contact, String location, int deliveryTurnaroundDays) {
        this.supplierId = supplierId;
        this.name = name;
        this.contact = contact;
        this.location = location;
        this.deliveryTurnaroundDays = deliveryTurnaroundDays;
        this.suppliedDrugs = new String[50]; // Max 50 drugs per supplier
        this.drugCount = 0;
    }

    // Getters
    public String getSupplierId() {
        return supplierId;
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

    public String getLocation() {
        return location;
    }

    public int getDeliveryTurnaroundDays() {
        return deliveryTurnaroundDays;
    }

    public String[] getSuppliedDrugs() {
        return suppliedDrugs;
    }

    public int getDrugCount() {
        return drugCount;
    }

    // Setters
    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
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

    public void setLocation(String location) {
        this.location = location;
    }

    public void setDeliveryTurnaroundDays(int deliveryTurnaroundDays) {
        this.deliveryTurnaroundDays = deliveryTurnaroundDays;
    }

    // Drug management
    public void addDrug(String drugCode) {
        if (drugCount < suppliedDrugs.length && !suppliesDrug(drugCode)) {
            suppliedDrugs[drugCount] = drugCode;
            drugCount++;
        }
    }

    public void removeDrug(String drugCode) {
        for (int i = 0; i < drugCount; i++) {
            if (Objects.equals(suppliedDrugs[i], drugCode)) {
                // Shift remaining elements
                for (int j = i; j < drugCount - 1; j++) {
                    suppliedDrugs[j] = suppliedDrugs[j + 1];
                }
                suppliedDrugs[drugCount - 1] = null;
                drugCount--;
                return;
            }
        }
    }

    public boolean suppliesDrug(String drugCode) {
        for (int i = 0; i < drugCount; i++) {
            if (Objects.equals(suppliedDrugs[i], drugCode)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return String.format("Supplier{id='%s', name='%s', location='%s', turnaround=%d days, drugs=%d}",
                supplierId, name, location, deliveryTurnaroundDays, drugCount);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Supplier supplier = (Supplier) obj;
        return Objects.equals(supplierId, supplier.supplierId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(supplierId);
    }
}
