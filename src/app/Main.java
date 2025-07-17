package app;

import models.Drug;
import services.DrugService;
import utils.FileUtils;
import utils.InputUtils;

import java.time.LocalDate;

/**
 * Main application class for Atinka Meds Pharmacy Inventory System
 */
public class Main {
    private static DrugService drugService;

    public static void main(String[] args) {
        // Initialize the system
        FileUtils.initializeDataFiles();
        drugService = new DrugService();

        InputUtils.printHeader("ATINKA MEDS PHARMACY INVENTORY SYSTEM");
        System.out.println("Welcome to the Atinka Meds Inventory Management System");
        System.out.println("Designed for offline-first performance in Adenta, Accra");
        System.out.println();

        // Main application loop
        boolean running = true;
        while (running) {
            try {
                displayMainMenu();
                int choice = InputUtils.readMenuChoice("Enter your choice", 9);

                switch (choice) {
                    case 1:
                        drugManagementMenu();
                        break;
                    case 2:
                        searchAndSortMenu();
                        break;
                    case 3:
                        stockManagementMenu();
                        break;
                    case 4:
                        supplierManagementMenu();
                        break;
                    case 5:
                        // Purchase/Sales menu (placeholder)
                        System.out.println("Purchase/Sales management coming soon...");
                        InputUtils.waitForEnter();
                        break;
                    case 6:
                        // Customer management (placeholder)
                        System.out.println("Customer management coming soon...");
                        InputUtils.waitForEnter();
                        break;
                    case 7:
                        reportsMenu();
                        break;
                    case 8:
                        systemInformationMenu();
                        break;
                    case 9:
                        running = false;
                        break;
                }
            } catch (Exception e) {
                System.err.println("An error occurred: " + e.getMessage());
                InputUtils.waitForEnter("Press Enter to continue...");
            }
        }

        // Cleanup
        System.out.println("Thank you for using Atinka Meds Inventory System!");
        InputUtils.closeScanner();
    }

    /**
     * Display the main menu
     */
    private static void displayMainMenu() {
        InputUtils.clearScreen();
        InputUtils.printHeader("MAIN MENU");
        System.out.println("1. Drug Management");
        System.out.println("2. Search & Sort Drugs");
        System.out.println("3. Stock Management");
        System.out.println("4. Supplier Management");
        System.out.println("5. Purchase & Sales");
        System.out.println("6. Customer Management");
        System.out.println("7. Reports & Analytics");
        System.out.println("8. System Information");
        System.out.println("9. Exit");
        System.out.println();
    }

    /**
     * Drug Management submenu
     */
    private static void drugManagementMenu() {
        boolean back = false;
        while (!back) {
            InputUtils.clearScreen();
            InputUtils.printHeader("DRUG MANAGEMENT");
            System.out.println("1. Add New Drug");
            System.out.println("2. Update Drug Information");
            System.out.println("3. Remove Drug");
            System.out.println("4. View Drug Details");
            System.out.println("5. List All Drugs");
            System.out.println("6. Back to Main Menu");
            System.out.println();

            int choice = InputUtils.readMenuChoice("Enter your choice", 6);

            switch (choice) {
                case 1:
                    addNewDrug();
                    break;
                case 2:
                    updateDrug();
                    break;
                case 3:
                    removeDrug();
                    break;
                case 4:
                    viewDrugDetails();
                    break;
                case 5:
                    listAllDrugs();
                    break;
                case 6:
                    back = true;
                    break;
            }
        }
    }

    /**
     * Add a new drug
     */
    private static void addNewDrug() {
        InputUtils.printHeader("ADD NEW DRUG");

        String drugCode = InputUtils.readNonEmptyString("Drug Code");
        String name = InputUtils.readNonEmptyString("Drug Name");
        double price = InputUtils.readPositiveDouble("Price");
        int stockLevel = InputUtils.readNonNegativeInt("Initial Stock Level");
        LocalDate expirationDate = InputUtils.readFutureDate("Expiration Date");
        int minThreshold = InputUtils.readNonNegativeInt("Minimum Stock Threshold");

        if (drugService.addDrug(drugCode, name, price, stockLevel, expirationDate, minThreshold)) {
            System.out.println("Drug added successfully!");

            // Ask if user wants to add suppliers
            if (InputUtils.readConfirmation("Would you like to add suppliers for this drug?")) {
                addSuppliersToNewDrug(drugCode);
            }
        }

        InputUtils.waitForEnter();
    }

    /**
     * Add suppliers to a newly created drug
     */
    private static void addSuppliersToNewDrug(String drugCode) {
        while (true) {
            String supplier = InputUtils.readString("Supplier Name (or press Enter to finish)");
            if (supplier.isEmpty()) {
                break;
            }

            drugService.addSupplierToDrug(drugCode, supplier);
            System.out.println("Supplier added: " + supplier);
        }
    }

    /**
     * Update existing drug
     */
    private static void updateDrug() {
        InputUtils.printHeader("UPDATE DRUG");

        String drugCode = InputUtils.readNonEmptyString("Enter Drug Code to update");
        Drug drug = drugService.getDrug(drugCode);

        if (drug == null) {
            System.out.println("Drug not found!");
            InputUtils.waitForEnter();
            return;
        }

        System.out.println("Current details:");
        System.out.println(drug);
        System.out.println();

        String name = InputUtils.readString("New Name (current: " + drug.getName() + ")");
        if (name.isEmpty())
            name = drug.getName();

        System.out.print("New Price (current: " + drug.getPrice() + "): ");
        String priceStr = InputUtils.readString("");
        double price = priceStr.isEmpty() ? drug.getPrice() : Double.parseDouble(priceStr);

        System.out.print("New Stock Level (current: " + drug.getStockLevel() + "): ");
        String stockStr = InputUtils.readString("");
        int stockLevel = stockStr.isEmpty() ? drug.getStockLevel() : Integer.parseInt(stockStr);

        System.out.print("New Expiration Date (current: " + drug.getExpirationDate() + ") (yyyy-MM-dd): ");
        String dateStr = InputUtils.readString("");
        LocalDate expirationDate = dateStr.isEmpty() ? drug.getExpirationDate() : LocalDate.parse(dateStr);

        System.out.print("New Min Threshold (current: " + drug.getMinStockThreshold() + "): ");
        String thresholdStr = InputUtils.readString("");
        int minThreshold = thresholdStr.isEmpty() ? drug.getMinStockThreshold() : Integer.parseInt(thresholdStr);

        if (drugService.updateDrug(drugCode, name, price, stockLevel, expirationDate, minThreshold)) {
            System.out.println("Drug updated successfully!");
        }

        InputUtils.waitForEnter();
    }

    /**
     * Remove a drug
     */
    private static void removeDrug() {
        InputUtils.printHeader("REMOVE DRUG");

        String drugCode = InputUtils.readNonEmptyString("Enter Drug Code to remove");
        Drug drug = drugService.getDrug(drugCode);

        if (drug == null) {
            System.out.println("Drug not found!");
            InputUtils.waitForEnter();
            return;
        }

        System.out.println("Drug to be removed:");
        System.out.println(drug);
        System.out.println();

        if (InputUtils.readConfirmation("Are you sure you want to remove this drug?")) {
            if (drugService.removeDrug(drugCode)) {
                System.out.println("Drug removed successfully!");
            }
        } else {
            System.out.println("Removal cancelled.");
        }

        InputUtils.waitForEnter();
    }

    /**
     * View drug details
     */
    private static void viewDrugDetails() {
        InputUtils.printHeader("VIEW DRUG DETAILS");

        String drugCode = InputUtils.readNonEmptyString("Enter Drug Code");
        Drug drug = drugService.getDrug(drugCode);

        if (drug == null) {
            System.out.println("Drug not found!");
        } else {
            System.out.println();
            System.out.println("Drug Details:");
            InputUtils.printSeparator();
            System.out.println("Code: " + drug.getDrugCode());
            System.out.println("Name: " + drug.getName());
            System.out.println("Price: $" + String.format("%.2f", drug.getPrice()));
            System.out.println("Stock Level: " + drug.getStockLevel());
            System.out.println("Min Threshold: " + drug.getMinStockThreshold());
            System.out.println("Expiration Date: " + drug.getExpirationDate());
            System.out.println("Status: " + (drug.isExpired() ? "EXPIRED" : drug.isLowStock() ? "LOW STOCK" : "OK"));

            System.out.print("Suppliers: ");
            String[] suppliers = drug.getSuppliers();
            boolean hasSuppliers = false;
            for (String supplier : suppliers) {
                if (supplier != null) {
                    if (hasSuppliers)
                        System.out.print(", ");
                    System.out.print(supplier);
                    hasSuppliers = true;
                }
            }
            if (!hasSuppliers) {
                System.out.print("None");
            }
            System.out.println();
        }

        InputUtils.waitForEnter();
    }

    /**
     * List all drugs
     */
    private static void listAllDrugs() {
        InputUtils.printHeader("ALL DRUGS");

        Drug[] drugs = drugService.getAllDrugs();

        if (drugs.length == 0) {
            System.out.println("No drugs found in the system.");
        } else {
            System.out.printf("%-10s %-25s %-10s %-8s %-12s %-8s%n",
                    "Code", "Name", "Price", "Stock", "Expiry", "Status");
            InputUtils.printSeparator();

            for (Drug drug : drugs) {
                String status = drug.isExpired() ? "EXPIRED" : drug.isLowStock() ? "LOW" : "OK";
                System.out.printf("%-10s %-25s $%-9.2f %-8d %-12s %-8s%n",
                        drug.getDrugCode(),
                        drug.getName().length() > 25 ? drug.getName().substring(0, 22) + "..." : drug.getName(),
                        drug.getPrice(),
                        drug.getStockLevel(),
                        drug.getExpirationDate(),
                        status);
            }

            System.out.println("\nTotal drugs: " + drugs.length);
        }

        InputUtils.waitForEnter();
    }

    /**
     * Search and Sort submenu
     */
    private static void searchAndSortMenu() {
        boolean back = false;
        while (!back) {
            InputUtils.clearScreen();
            InputUtils.printHeader("SEARCH & SORT DRUGS");
            System.out.println("1. Search by Drug Code");
            System.out.println("2. Search by Drug Name");
            System.out.println("3. Search by Supplier");
            System.out.println("4. Sort by Name (Alphabetical)");
            System.out.println("5. Sort by Price");
            System.out.println("6. Sort by Stock Level");
            System.out.println("7. Sort by Expiration Date");
            System.out.println("8. Back to Main Menu");
            System.out.println();

            int choice = InputUtils.readMenuChoice("Enter your choice", 8);

            switch (choice) {
                case 1:
                    searchByCode();
                    break;
                case 2:
                    searchByName();
                    break;
                case 3:
                    searchBySupplier();
                    break;
                case 4:
                    displaySortedDrugs(drugService.sortDrugsByName(), "Name (Alphabetical)");
                    break;
                case 5:
                    displaySortedDrugs(drugService.sortDrugsByPrice(), "Price");
                    break;
                case 6:
                    displaySortedDrugs(drugService.sortDrugsByStock(), "Stock Level");
                    break;
                case 7:
                    displaySortedDrugs(drugService.sortDrugsByExpirationDate(), "Expiration Date");
                    break;
                case 8:
                    back = true;
                    break;
            }
        }
    }

    /**
     * Search by drug code
     */
    private static void searchByCode() {
        InputUtils.printHeader("SEARCH BY CODE");

        String drugCode = InputUtils.readNonEmptyString("Enter Drug Code");
        Drug drug = drugService.searchDrugByCode(drugCode);

        if (drug == null) {
            System.out.println("No drug found with code: " + drugCode);
        } else {
            System.out.println("Drug found:");
            System.out.println(drug);
        }

        InputUtils.waitForEnter();
    }

    /**
     * Search by drug name
     */
    private static void searchByName() {
        InputUtils.printHeader("SEARCH BY NAME");

        String name = InputUtils.readNonEmptyString("Enter Drug Name (partial matches allowed)");
        Drug[] drugs = drugService.searchDrugsByName(name);

        if (drugs.length == 0) {
            System.out.println("No drugs found with name containing: " + name);
        } else {
            System.out.println("Found " + drugs.length + " drug(s):");
            InputUtils.printSeparator();
            for (Drug drug : drugs) {
                System.out.println(drug);
            }
        }

        InputUtils.waitForEnter();
    }

    /**
     * Search by supplier
     */
    private static void searchBySupplier() {
        InputUtils.printHeader("SEARCH BY SUPPLIER");

        String supplier = InputUtils.readNonEmptyString("Enter Supplier Name");
        Drug[] drugs = drugService.searchDrugsBySupplier(supplier);

        if (drugs.length == 0) {
            System.out.println("No drugs found from supplier: " + supplier);
        } else {
            System.out.println("Found " + drugs.length + " drug(s) from " + supplier + ":");
            InputUtils.printSeparator();
            for (Drug drug : drugs) {
                System.out.println(drug);
            }
        }

        InputUtils.waitForEnter();
    }

    /**
     * Display sorted drugs
     */
    private static void displaySortedDrugs(Drug[] drugs, String sortType) {
        InputUtils.printHeader("DRUGS SORTED BY " + sortType.toUpperCase());

        if (drugs.length == 0) {
            System.out.println("No drugs found in the system.");
        } else {
            System.out.printf("%-10s %-25s %-10s %-8s %-12s%n",
                    "Code", "Name", "Price", "Stock", "Expiry");
            InputUtils.printSeparator();

            for (Drug drug : drugs) {
                System.out.printf("%-10s %-25s $%-9.2f %-8d %-12s%n",
                        drug.getDrugCode(),
                        drug.getName().length() > 25 ? drug.getName().substring(0, 22) + "..." : drug.getName(),
                        drug.getPrice(),
                        drug.getStockLevel(),
                        drug.getExpirationDate());
            }

            System.out.println("\nTotal drugs: " + drugs.length);
        }

        InputUtils.waitForEnter();
    }

    /**
     * Stock Management submenu
     */
    private static void stockManagementMenu() {
        boolean back = false;
        while (!back) {
            InputUtils.clearScreen();
            InputUtils.printHeader("STOCK MANAGEMENT");
            System.out.println("1. Update Stock Level");
            System.out.println("2. Add Stock (Restock)");
            System.out.println("3. View Low Stock Drugs");
            System.out.println("4. View Expired Drugs");
            System.out.println("5. Stock Alerts");
            System.out.println("6. Back to Main Menu");
            System.out.println();

            int choice = InputUtils.readMenuChoice("Enter your choice", 6);

            switch (choice) {
                case 1:
                    updateStockLevel();
                    break;
                case 2:
                    addStock();
                    break;
                case 3:
                    viewLowStockDrugs();
                    break;
                case 4:
                    viewExpiredDrugs();
                    break;
                case 5:
                    showStockAlerts();
                    break;
                case 6:
                    back = true;
                    break;
            }
        }
    }

    /**
     * Update stock level
     */
    private static void updateStockLevel() {
        InputUtils.printHeader("UPDATE STOCK LEVEL");

        String drugCode = InputUtils.readNonEmptyString("Enter Drug Code");
        Drug drug = drugService.getDrug(drugCode);

        if (drug == null) {
            System.out.println("Drug not found!");
            InputUtils.waitForEnter();
            return;
        }

        System.out.println("Current stock level: " + drug.getStockLevel());
        int newStock = InputUtils.readNonNegativeInt("Enter new stock level");

        if (drugService.updateStock(drugCode, newStock)) {
            System.out.println("Stock level updated successfully!");
        }

        InputUtils.waitForEnter();
    }

    /**
     * Add stock (restock)
     */
    private static void addStock() {
        InputUtils.printHeader("ADD STOCK (RESTOCK)");

        String drugCode = InputUtils.readNonEmptyString("Enter Drug Code");
        Drug drug = drugService.getDrug(drugCode);

        if (drug == null) {
            System.out.println("Drug not found!");
            InputUtils.waitForEnter();
            return;
        }

        System.out.println("Current stock level: " + drug.getStockLevel());
        int quantity = InputUtils.readPositiveInt("Enter quantity to add");

        if (drugService.addStock(drugCode, quantity)) {
            System.out.println("Stock added successfully!");
            System.out.println("New stock level: " + drugService.getDrug(drugCode).getStockLevel());
        }

        InputUtils.waitForEnter();
    }

    /**
     * View low stock drugs
     */
    private static void viewLowStockDrugs() {
        InputUtils.printHeader("LOW STOCK DRUGS");

        Drug[] lowStockDrugs = drugService.getLowStockDrugs();

        if (lowStockDrugs.length == 0) {
            System.out.println("No drugs with low stock!");
        } else {
            System.out.printf("%-10s %-25s %-8s %-12s%n", "Code", "Name", "Stock", "Threshold");
            InputUtils.printSeparator();

            for (Drug drug : lowStockDrugs) {
                System.out.printf("%-10s %-25s %-8d %-12d%n",
                        drug.getDrugCode(),
                        drug.getName().length() > 25 ? drug.getName().substring(0, 22) + "..." : drug.getName(),
                        drug.getStockLevel(),
                        drug.getMinStockThreshold());
            }

            System.out.println("\nTotal low stock drugs: " + lowStockDrugs.length);
        }

        InputUtils.waitForEnter();
    }

    /**
     * View expired drugs
     */
    private static void viewExpiredDrugs() {
        InputUtils.printHeader("EXPIRED DRUGS");

        Drug[] expiredDrugs = drugService.getExpiredDrugs();

        if (expiredDrugs.length == 0) {
            System.out.println("No expired drugs found!");
        } else {
            System.out.printf("%-10s %-25s %-12s %-8s%n", "Code", "Name", "Expiry", "Stock");
            InputUtils.printSeparator();

            for (Drug drug : expiredDrugs) {
                System.out.printf("%-10s %-25s %-12s %-8d%n",
                        drug.getDrugCode(),
                        drug.getName().length() > 25 ? drug.getName().substring(0, 22) + "..." : drug.getName(),
                        drug.getExpirationDate(),
                        drug.getStockLevel());
            }

            System.out.println("\nTotal expired drugs: " + expiredDrugs.length);
            System.out.println("⚠️  WARNING: These drugs should be removed from inventory!");
        }

        InputUtils.waitForEnter();
    }

    /**
     * Show stock alerts
     */
    private static void showStockAlerts() {
        InputUtils.printHeader("STOCK ALERTS");

        Drug[] lowStockDrugs = drugService.getLowStockDrugs();
        Drug[] expiredDrugs = drugService.getExpiredDrugs();

        System.out.println("📊 STOCK ALERT SUMMARY");
        InputUtils.printSeparator();
        System.out.println("Low Stock Drugs: " + lowStockDrugs.length);
        System.out.println("Expired Drugs: " + expiredDrugs.length);
        System.out.println("Total Drugs: " + drugService.getDrugCount());

        if (lowStockDrugs.length > 0 || expiredDrugs.length > 0) {
            System.out.println("\n⚠️  IMMEDIATE ATTENTION REQUIRED!");

            if (expiredDrugs.length > 0) {
                System.out.println("\n🚫 EXPIRED DRUGS (Remove immediately):");
                for (Drug drug : expiredDrugs) {
                    System.out.println("  • " + drug.getName() + " (" + drug.getDrugCode() +
                            ") - Expired: " + drug.getExpirationDate());
                }
            }

            if (lowStockDrugs.length > 0) {
                System.out.println("\n📦 LOW STOCK DRUGS (Reorder soon):");
                for (Drug drug : lowStockDrugs) {
                    System.out.println("  • " + drug.getName() + " (" + drug.getDrugCode() +
                            ") - Stock: " + drug.getStockLevel() + "/" + drug.getMinStockThreshold());
                }
            }
        } else {
            System.out.println("\n✅ All drugs are adequately stocked and not expired!");
        }

        InputUtils.waitForEnter();
    }

    /**
     * Supplier Management submenu (placeholder)
     */
    private static void supplierManagementMenu() {
        InputUtils.printHeader("SUPPLIER MANAGEMENT");
        System.out.println("This feature is under development.");
        System.out.println("Coming soon: Supplier registration, drug-supplier mapping, delivery tracking");
        InputUtils.waitForEnter();
    }

    /**
     * Reports menu (placeholder)
     */
    private static void reportsMenu() {
        InputUtils.printHeader("REPORTS & ANALYTICS");
        System.out.println("This feature is under development.");
        System.out.println("Coming soon: Sales reports, inventory analysis, algorithm performance reports");
        InputUtils.waitForEnter();
    }

    /**
     * System Information menu
     */
    private static void systemInformationMenu() {
        InputUtils.printHeader("SYSTEM INFORMATION");

        System.out.println("🏥 ATINKA MEDS PHARMACY INVENTORY SYSTEM");
        System.out.println("Location: Adenta, Accra, Ghana");
        System.out.println("Version: 1.0.0");
        System.out.println("Designed for: Offline-first operations");
        System.out.println();

        System.out.println("📊 CURRENT STATISTICS:");
        InputUtils.printSeparator();
        System.out.println("Total Drugs: " + drugService.getDrugCount());
        System.out.println("Low Stock Drugs: " + drugService.getLowStockDrugs().length);
        System.out.println("Expired Drugs: " + drugService.getExpiredDrugs().length);
        System.out.println();

        System.out.println("🛠️  DATA STRUCTURES USED:");
        System.out.println("• HashMap - Drug storage and fast lookups (O(1) average)");
        System.out.println("• LinkedList - Transaction history");
        System.out.println("• Queue - Purchase processing");
        System.out.println("• Stack - Sales log");
        System.out.println("• MinHeap - Stock priority management");
        System.out.println("• Binary Search Tree - Supplier organization");
        System.out.println();

        System.out.println("🔍 ALGORITHMS IMPLEMENTED:");
        System.out.println("• Merge Sort - Drug name sorting (O(n log n))");
        System.out.println("• Quick Sort - Price sorting (O(n log n) average)");
        System.out.println("• Insertion Sort - Stock level sorting (O(n²))");
        System.out.println("• Binary Search - Fast drug lookup (O(log n))");
        System.out.println("• Linear Search - Name-based searches (O(n))");

        InputUtils.waitForEnter();
    }
}