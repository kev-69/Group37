package storage;

import models.Drug;
import structures.MyHashMap;
import utils.FileUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Storage class for Drug data persistence
 */
public class DrugStore {
    private static final String DRUGS_FILE = "drugs.txt";
    private MyHashMap<String, Drug> drugMap; // Key: drugCode, Value: Drug

    public DrugStore() {
        this.drugMap = new MyHashMap<>();
        loadDrugs();
    }

    /**
     * Load drugs from file
     */
    private void loadDrugs() {
        List<String> lines = FileUtils.readLines(DRUGS_FILE);

        for (String line : lines) {
            if (line.startsWith("#") || line.trim().isEmpty()) {
                continue; // Skip comments and empty lines
            }

            Drug drug = parseDrugFromLine(line);
            if (drug != null) {
                drugMap.put(drug.getDrugCode(), drug);
            }
        }
    }

    /**
     * Parse a drug from a CSV line
     */
    private Drug parseDrugFromLine(String line) {
        try {
            String[] fields = FileUtils.splitCsvLine(line);
            if (fields.length < 6) {
                return null;
            }

            String drugCode = fields[0];
            String name = fields[1];
            double price = Double.parseDouble(fields[2]);
            int stockLevel = Integer.parseInt(fields[3]);
            LocalDate expirationDate = FileUtils.parseDate(fields[4]);
            int minThreshold = Integer.parseInt(fields[5]);

            Drug drug = new Drug(drugCode, name, price, stockLevel, expirationDate);
            drug.setMinStockThreshold(minThreshold);

            // Load suppliers (fields 6 onwards)
            for (int i = 6; i < fields.length; i++) {
                if (!fields[i].trim().isEmpty()) {
                    drug.addSupplier(fields[i].trim());
                }
            }

            return drug;
        } catch (Exception e) {
            System.err.println("Error parsing drug line: " + line + " - " + e.getMessage());
            return null;
        }
    }

    /**
     * Convert drug to CSV line
     */
    private String drugToLine(Drug drug) {
        List<String> fields = new ArrayList<>();
        fields.add(drug.getDrugCode());
        fields.add(drug.getName());
        fields.add(String.valueOf(drug.getPrice()));
        fields.add(String.valueOf(drug.getStockLevel()));
        fields.add(FileUtils.formatDate(drug.getExpirationDate()));
        fields.add(String.valueOf(drug.getMinStockThreshold()));

        // Add suppliers
        for (String supplier : drug.getSuppliers()) {
            if (supplier != null && !supplier.trim().isEmpty()) {
                fields.add(supplier);
            }
        }

        return FileUtils.joinCsvLine(fields.toArray(new String[0]));
    }

    /**
     * Save all drugs to file
     */
    public boolean saveDrugs() {
        List<String> lines = new ArrayList<>();
        lines.add("# Drug Code,Name,Price,Stock Level,Expiration Date,Min Threshold,Suppliers");

        Object[] drugCodes = drugMap.keySet();
        for (Object drugCodeObj : drugCodes) {
            if (drugCodeObj != null) {
                String drugCode = (String) drugCodeObj;
                Drug drug = drugMap.get(drugCode);
                if (drug != null) {
                    lines.add(drugToLine(drug));
                }
            }
        }

        return FileUtils.writeLines(DRUGS_FILE, lines);
    }

    /**
     * Add a new drug
     */
    public boolean addDrug(Drug drug) {
        if (drug == null || drugMap.containsKey(drug.getDrugCode())) {
            return false;
        }

        drugMap.put(drug.getDrugCode(), drug);
        return saveDrugs();
    }

    /**
     * Update an existing drug
     */
    public boolean updateDrug(Drug drug) {
        if (drug == null || !drugMap.containsKey(drug.getDrugCode())) {
            return false;
        }

        drugMap.put(drug.getDrugCode(), drug);
        return saveDrugs();
    }

    /**
     * Remove a drug
     */
    public boolean removeDrug(String drugCode) {
        if (drugCode == null || !drugMap.containsKey(drugCode)) {
            return false;
        }

        drugMap.remove(drugCode);
        return saveDrugs();
    }

    /**
     * Get drug by code
     */
    public Drug getDrug(String drugCode) {
        return drugMap.get(drugCode);
    }

    /**
     * Get all drugs
     */
    public Drug[] getAllDrugs() {
        List<Drug> drugs = new ArrayList<>();
        Object[] drugCodes = drugMap.keySet();

        for (Object drugCodeObj : drugCodes) {
            if (drugCodeObj != null) {
                String drugCode = (String) drugCodeObj;
                Drug drug = drugMap.get(drugCode);
                if (drug != null) {
                    drugs.add(drug);
                }
            }
        }

        return drugs.toArray(new Drug[0]);
    }

    /**
     * Check if drug exists
     */
    public boolean drugExists(String drugCode) {
        return drugMap.containsKey(drugCode);
    }

    /**
     * Get drugs by supplier
     */
    public Drug[] getDrugsBySupplier(String supplier) {
        List<Drug> drugs = new ArrayList<>();
        Object[] drugCodes = drugMap.keySet();

        for (Object drugCodeObj : drugCodes) {
            if (drugCodeObj != null) {
                String drugCode = (String) drugCodeObj;
                Drug drug = drugMap.get(drugCode);
                if (drug != null && drug.hasSupplier(supplier)) {
                    drugs.add(drug);
                }
            }
        }

        return drugs.toArray(new Drug[0]);
    }

    /**
     * Get low stock drugs
     */
    public Drug[] getLowStockDrugs() {
        List<Drug> lowStockDrugs = new ArrayList<>();
        Object[] drugCodes = drugMap.keySet();

        for (Object drugCodeObj : drugCodes) {
            if (drugCodeObj != null) {
                String drugCode = (String) drugCodeObj;
                Drug drug = drugMap.get(drugCode);
                if (drug != null && drug.isLowStock()) {
                    lowStockDrugs.add(drug);
                }
            }
        }

        return lowStockDrugs.toArray(new Drug[0]);
    }

    /**
     * Get expired drugs
     */
    public Drug[] getExpiredDrugs() {
        List<Drug> expiredDrugs = new ArrayList<>();
        Object[] drugCodes = drugMap.keySet();

        for (Object drugCodeObj : drugCodes) {
            if (drugCodeObj != null) {
                String drugCode = (String) drugCodeObj;
                Drug drug = drugMap.get(drugCode);
                if (drug != null && drug.isExpired()) {
                    expiredDrugs.add(drug);
                }
            }
        }

        return expiredDrugs.toArray(new Drug[0]);
    }

    /**
     * Get count of drugs
     */
    public int getDrugCount() {
        return drugMap.size();
    }
}
