package services;

import models.Drug;
import storage.DrugStore;
import utils.SortUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Service class for drug management operations
 */
public class DrugService {
    private DrugStore drugStore;

    public DrugService() {
        this.drugStore = new DrugStore();
    }

    /**
     * Add a new drug
     */
    public boolean addDrug(String drugCode, String name, double price, int stockLevel,
            LocalDate expirationDate, int minThreshold) {
        if (drugStore.drugExists(drugCode)) {
            System.out.println("Drug with code " + drugCode + " already exists.");
            return false;
        }

        Drug drug = new Drug(drugCode, name, price, stockLevel, expirationDate);
        drug.setMinStockThreshold(minThreshold);

        if (drugStore.addDrug(drug)) {
            System.out.println("Drug added successfully: " + drug.getName());
            return true;
        } else {
            System.out.println("Failed to add drug.");
            return false;
        }
    }

    /**
     * Update existing drug
     */
    public boolean updateDrug(String drugCode, String name, double price, int stockLevel,
            LocalDate expirationDate, int minThreshold) {
        Drug drug = drugStore.getDrug(drugCode);
        if (drug == null) {
            System.out.println("Drug with code " + drugCode + " not found.");
            return false;
        }

        drug.setName(name);
        drug.setPrice(price);
        drug.setStockLevel(stockLevel);
        drug.setExpirationDate(expirationDate);
        drug.setMinStockThreshold(minThreshold);

        if (drugStore.updateDrug(drug)) {
            System.out.println("Drug updated successfully: " + drug.getName());
            return true;
        } else {
            System.out.println("Failed to update drug.");
            return false;
        }
    }

    /**
     * Remove a drug
     */
    public boolean removeDrug(String drugCode) {
        Drug drug = drugStore.getDrug(drugCode);
        if (drug == null) {
            System.out.println("Drug with code " + drugCode + " not found.");
            return false;
        }

        if (drugStore.removeDrug(drugCode)) {
            System.out.println("Drug removed successfully: " + drug.getName());
            return true;
        } else {
            System.out.println("Failed to remove drug.");
            return false;
        }
    }

    /**
     * Get drug by code
     */
    public Drug getDrug(String drugCode) {
        return drugStore.getDrug(drugCode);
    }

    /**
     * Get all drugs
     */
    public Drug[] getAllDrugs() {
        return drugStore.getAllDrugs();
    }

    /**
     * Search drugs by name (linear search)
     */
    public Drug[] searchDrugsByName(String name) {
        Drug[] allDrugs = drugStore.getAllDrugs();
        List<Drug> matchingDrugs = new ArrayList<>();

        for (Drug drug : allDrugs) {
            if (drug.getName().toLowerCase().contains(name.toLowerCase())) {
                matchingDrugs.add(drug);
            }
        }

        return matchingDrugs.toArray(new Drug[0]);
    }

    /**
     * Search drug by code (direct lookup - O(1) with HashMap)
     */
    public Drug searchDrugByCode(String drugCode) {
        return drugStore.getDrug(drugCode);
    }

    /**
     * Search drugs by supplier
     */
    public Drug[] searchDrugsBySupplier(String supplier) {
        return drugStore.getDrugsBySupplier(supplier);
    }

    /**
     * Sort drugs alphabetically by name using merge sort
     */
    public Drug[] sortDrugsByName() {
        Drug[] drugs = drugStore.getAllDrugs();

        Comparator<Drug> nameComparator = (d1, d2) -> d1.getName().compareToIgnoreCase(d2.getName());
        SortUtils.mergeSort(drugs, nameComparator);

        return drugs;
    }

    /**
     * Sort drugs by price using quick sort
     */
    public Drug[] sortDrugsByPrice() {
        Drug[] drugs = drugStore.getAllDrugs();

        Comparator<Drug> priceComparator = (d1, d2) -> Double.compare(d1.getPrice(), d2.getPrice());
        SortUtils.quickSort(drugs, priceComparator);

        return drugs;
    }

    /**
     * Sort drugs by stock level using insertion sort
     */
    public Drug[] sortDrugsByStock() {
        Drug[] drugs = drugStore.getAllDrugs();

        Comparator<Drug> stockComparator = (d1, d2) -> Integer.compare(d1.getStockLevel(), d2.getStockLevel());
        SortUtils.insertionSort(drugs, stockComparator);

        return drugs;
    }

    /**
     * Sort drugs by expiration date
     */
    public Drug[] sortDrugsByExpirationDate() {
        Drug[] drugs = drugStore.getAllDrugs();

        Comparator<Drug> expirationComparator = (d1, d2) -> d1.getExpirationDate().compareTo(d2.getExpirationDate());
        SortUtils.mergeSort(drugs, expirationComparator);

        return drugs;
    }

    /**
     * Get low stock drugs
     */
    public Drug[] getLowStockDrugs() {
        return drugStore.getLowStockDrugs();
    }

    /**
     * Get expired drugs
     */
    public Drug[] getExpiredDrugs() {
        return drugStore.getExpiredDrugs();
    }

    /**
     * Update stock level
     */
    public boolean updateStock(String drugCode, int newStockLevel) {
        Drug drug = drugStore.getDrug(drugCode);
        if (drug == null) {
            System.out.println("Drug with code " + drugCode + " not found.");
            return false;
        }

        drug.setStockLevel(newStockLevel);
        return drugStore.updateDrug(drug);
    }

    /**
     * Add stock to existing level
     */
    public boolean addStock(String drugCode, int quantity) {
        Drug drug = drugStore.getDrug(drugCode);
        if (drug == null) {
            System.out.println("Drug with code " + drugCode + " not found.");
            return false;
        }

        drug.addStock(quantity);
        return drugStore.updateDrug(drug);
    }

    /**
     * Reduce stock (for sales)
     */
    public boolean reduceStock(String drugCode, int quantity) {
        Drug drug = drugStore.getDrug(drugCode);
        if (drug == null) {
            System.out.println("Drug with code " + drugCode + " not found.");
            return false;
        }

        if (drug.reduceStock(quantity)) {
            drugStore.updateDrug(drug);
            return true;
        } else {
            System.out.println("Insufficient stock. Available: " + drug.getStockLevel() + ", Requested: " + quantity);
            return false;
        }
    }

    /**
     * Add supplier to drug
     */
    public boolean addSupplierToDrug(String drugCode, String supplier) {
        Drug drug = drugStore.getDrug(drugCode);
        if (drug == null) {
            System.out.println("Drug with code " + drugCode + " not found.");
            return false;
        }

        drug.addSupplier(supplier);
        return drugStore.updateDrug(drug);
    }

    /**
     * Remove supplier from drug
     */
    public boolean removeSupplierFromDrug(String drugCode, String supplier) {
        Drug drug = drugStore.getDrug(drugCode);
        if (drug == null) {
            System.out.println("Drug with code " + drugCode + " not found.");
            return false;
        }

        drug.removeSupplier(supplier);
        return drugStore.updateDrug(drug);
    }

    /**
     * Check drug availability for sale
     */
    public boolean isDrugAvailable(String drugCode, int quantity) {
        Drug drug = drugStore.getDrug(drugCode);
        if (drug == null) {
            return false;
        }

        return drug.getStockLevel() >= quantity && !drug.isExpired();
    }

    /**
     * Get drug count
     */
    public int getDrugCount() {
        return drugStore.getDrugCount();
    }

    /**
     * Binary search for drug by name (requires sorted array)
     */
    public Drug binarySearchByName(String name) {
        Drug[] sortedDrugs = sortDrugsByName();
        Drug searchDrug = new Drug("TEMP", name, 0, 0, LocalDate.now());

        Comparator<Drug> nameComparator = (d1, d2) -> d1.getName().compareToIgnoreCase(d2.getName());
        int index = SortUtils.binarySearch(sortedDrugs, searchDrug, nameComparator);

        return index >= 0 ? sortedDrugs[index] : null;
    }
}
