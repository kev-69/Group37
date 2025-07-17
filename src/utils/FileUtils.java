package utils;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for file operations
 */
public class FileUtils {

    private static final String DATA_DIR = "data/";
    private static final String REPORTS_DIR = "reports/";

    /**
     * Read all lines from a file
     */
    public static List<String> readLines(String filename) {
        List<String> lines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(DATA_DIR + filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            System.err.println("Error reading file " + filename + ": " + e.getMessage());
        }

        return lines;
    }

    /**
     * Write lines to a file (overwrites existing content)
     */
    public static boolean writeLines(String filename, List<String> lines) {
        return writeLines(filename, lines, false);
    }

    /**
     * Write lines to a file
     * 
     * @param filename the name of the file
     * @param lines    the lines to write
     * @param append   if true, append to file; if false, overwrite
     */
    public static boolean writeLines(String filename, List<String> lines, boolean append) {
        try {
            // Create data directory if it doesn't exist
            File dataDir = new File(DATA_DIR);
            if (!dataDir.exists()) {
                dataDir.mkdirs();
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(DATA_DIR + filename, append))) {
                for (String line : lines) {
                    writer.write(line);
                    writer.newLine();
                }
                return true;
            }
        } catch (IOException e) {
            System.err.println("Error writing to file " + filename + ": " + e.getMessage());
            return false;
        }
    }

    /**
     * Append a single line to a file
     */
    public static boolean appendLine(String filename, String line) {
        List<String> lines = new ArrayList<>();
        lines.add(line);
        return writeLines(filename, lines, true);
    }

    /**
     * Write content to a report file
     */
    public static boolean writeReport(String filename, String content) {
        try {
            // Create reports directory if it doesn't exist
            File reportsDir = new File(REPORTS_DIR);
            if (!reportsDir.exists()) {
                reportsDir.mkdirs();
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(REPORTS_DIR + filename))) {
                writer.write(content);
                return true;
            }
        } catch (IOException e) {
            System.err.println("Error writing report " + filename + ": " + e.getMessage());
            return false;
        }
    }

    /**
     * Check if a file exists
     */
    public static boolean fileExists(String filename) {
        File file = new File(DATA_DIR + filename);
        return file.exists() && file.isFile();
    }

    /**
     * Get file size in bytes
     */
    public static long getFileSize(String filename) {
        File file = new File(DATA_DIR + filename);
        return file.exists() ? file.length() : -1;
    }

    /**
     * Delete a file
     */
    public static boolean deleteFile(String filename) {
        File file = new File(DATA_DIR + filename);
        return file.delete();
    }

    /**
     * Create backup of a file
     */
    public static boolean backupFile(String filename) {
        if (!fileExists(filename)) {
            return false;
        }

        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String backupName = filename.replaceFirst("\\.", "_backup_" + timestamp + ".");

        try {
            File original = new File(DATA_DIR + filename);
            File backup = new File(DATA_DIR + backupName);

            try (FileInputStream fis = new FileInputStream(original);
                    FileOutputStream fos = new FileOutputStream(backup)) {

                byte[] buffer = new byte[1024];
                int length;
                while ((length = fis.read(buffer)) > 0) {
                    fos.write(buffer, 0, length);
                }
                return true;
            }
        } catch (IOException e) {
            System.err.println("Error creating backup of " + filename + ": " + e.getMessage());
            return false;
        }
    }

    /**
     * Parse date from string (yyyy-MM-dd format)
     */
    public static LocalDate parseDate(String dateStr) {
        try {
            return LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        } catch (Exception e) {
            System.err.println("Error parsing date: " + dateStr);
            return null;
        }
    }

    /**
     * Parse datetime from string (yyyy-MM-dd HH:mm:ss format)
     */
    public static LocalDateTime parseDateTime(String dateTimeStr) {
        try {
            return LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        } catch (Exception e) {
            System.err.println("Error parsing datetime: " + dateTimeStr);
            return null;
        }
    }

    /**
     * Format date to string (yyyy-MM-dd format)
     */
    public static String formatDate(LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    /**
     * Format datetime to string (yyyy-MM-dd HH:mm:ss format)
     */
    public static String formatDateTime(LocalDateTime dateTime) {
        return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    /**
     * Split CSV line handling quoted fields
     */
    public static String[] splitCsvLine(String line) {
        if (line == null || line.trim().isEmpty()) {
            return new String[0];
        }

        List<String> fields = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);

            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                fields.add(current.toString().trim());
                current = new StringBuilder();
            } else {
                current.append(c);
            }
        }

        fields.add(current.toString().trim());
        return fields.toArray(new String[0]);
    }

    /**
     * Escape field for CSV format
     */
    public static String escapeCsvField(String field) {
        if (field == null) {
            return "";
        }

        if (field.contains(",") || field.contains("\"") || field.contains("\n")) {
            return "\"" + field.replace("\"", "\"\"") + "\"";
        }

        return field;
    }

    /**
     * Join fields into CSV line
     */
    public static String joinCsvLine(String... fields) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < fields.length; i++) {
            if (i > 0) {
                sb.append(",");
            }
            sb.append(escapeCsvField(fields[i]));
        }
        return sb.toString();
    }

    /**
     * Initialize data files with headers if they don't exist
     */
    public static void initializeDataFiles() {
        // Create directories
        new File(DATA_DIR).mkdirs();
        new File(REPORTS_DIR).mkdirs();

        // Initialize files with headers if they don't exist
        if (!fileExists("drugs.txt")) {
            List<String> headers = new ArrayList<>();
            headers.add("# Drug Code,Name,Price,Stock Level,Expiration Date,Min Threshold,Suppliers");
            writeLines("drugs.txt", headers);
        }

        if (!fileExists("customers.txt")) {
            List<String> headers = new ArrayList<>();
            headers.add("# Customer ID,Name,Contact,Email,Address");
            writeLines("customers.txt", headers);
        }

        if (!fileExists("suppliers.txt")) {
            List<String> headers = new ArrayList<>();
            headers.add("# Supplier ID,Name,Contact,Email,Location,Delivery Days");
            writeLines("suppliers.txt", headers);
        }

        if (!fileExists("transactions.txt")) {
            List<String> headers = new ArrayList<>();
            headers.add("# Transaction ID,Drug Code,Customer ID,Quantity,Unit Price,Total Cost,Timestamp,Type");
            writeLines("transactions.txt", headers);
        }

        if (!fileExists("saleslog.txt")) {
            List<String> headers = new ArrayList<>();
            headers.add("# Sale ID,Drug Code,Customer ID,Quantity,Unit Price,Total,Timestamp");
            writeLines("saleslog.txt", headers);
        }
    }
}
