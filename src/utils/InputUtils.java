package utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

/**
 * Utility class for handling user input
 */
public class InputUtils {

    private static final Scanner scanner = new Scanner(System.in);

    /**
     * Read a string from user input
     */
    public static String readString(String prompt) {
        System.out.print(prompt + ": ");
        return scanner.nextLine().trim();
    }

    /**
     * Read a non-empty string from user input
     */
    public static String readNonEmptyString(String prompt) {
        String input;
        do {
            input = readString(prompt);
            if (input.isEmpty()) {
                System.out.println("Input cannot be empty. Please try again.");
            }
        } while (input.isEmpty());
        return input;
    }

    /**
     * Read an integer from user input
     */
    public static int readInt(String prompt) {
        while (true) {
            try {
                System.out.print(prompt + ": ");
                String input = scanner.nextLine().trim();
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid integer.");
            }
        }
    }

    /**
     * Read an integer within a specific range
     */
    public static int readInt(String prompt, int min, int max) {
        while (true) {
            int value = readInt(prompt + " (" + min + "-" + max + ")");
            if (value >= min && value <= max) {
                return value;
            }
            System.out.println("Please enter a value between " + min + " and " + max + ".");
        }
    }

    /**
     * Read a positive integer
     */
    public static int readPositiveInt(String prompt) {
        while (true) {
            int value = readInt(prompt);
            if (value > 0) {
                return value;
            }
            System.out.println("Please enter a positive integer.");
        }
    }

    /**
     * Read a non-negative integer
     */
    public static int readNonNegativeInt(String prompt) {
        while (true) {
            int value = readInt(prompt);
            if (value >= 0) {
                return value;
            }
            System.out.println("Please enter a non-negative integer.");
        }
    }

    /**
     * Read a double from user input
     */
    public static double readDouble(String prompt) {
        while (true) {
            try {
                System.out.print(prompt + ": ");
                String input = scanner.nextLine().trim();
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    /**
     * Read a positive double
     */
    public static double readPositiveDouble(String prompt) {
        while (true) {
            double value = readDouble(prompt);
            if (value > 0) {
                return value;
            }
            System.out.println("Please enter a positive number.");
        }
    }

    /**
     * Read a non-negative double
     */
    public static double readNonNegativeDouble(String prompt) {
        while (true) {
            double value = readDouble(prompt);
            if (value >= 0) {
                return value;
            }
            System.out.println("Please enter a non-negative number.");
        }
    }

    /**
     * Read a date from user input (yyyy-MM-dd format)
     */
    public static LocalDate readDate(String prompt) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        while (true) {
            try {
                String input = readString(prompt + " (yyyy-MM-dd)");
                return LocalDate.parse(input, formatter);
            } catch (DateTimeParseException e) {
                System.out.println("Please enter a valid date in yyyy-MM-dd format.");
            }
        }
    }

    /**
     * Read a future date from user input
     */
    public static LocalDate readFutureDate(String prompt) {
        while (true) {
            LocalDate date = readDate(prompt);
            if (date.isAfter(LocalDate.now())) {
                return date;
            }
            System.out.println("Please enter a future date.");
        }
    }

    /**
     * Read yes/no confirmation from user
     */
    public static boolean readConfirmation(String prompt) {
        while (true) {
            String input = readString(prompt + " (y/n)").toLowerCase();
            if (input.equals("y") || input.equals("yes")) {
                return true;
            } else if (input.equals("n") || input.equals("no")) {
                return false;
            }
            System.out.println("Please enter 'y' for yes or 'n' for no.");
        }
    }

    /**
     * Read a choice from a menu
     */
    public static int readMenuChoice(String prompt, int maxChoice) {
        return readInt(prompt, 1, maxChoice);
    }

    /**
     * Wait for user to press Enter
     */
    public static void waitForEnter() {
        System.out.print("Press Enter to continue...");
        scanner.nextLine();
    }

    /**
     * Wait for user to press Enter with custom message
     */
    public static void waitForEnter(String message) {
        System.out.print(message);
        scanner.nextLine();
    }

    /**
     * Read an email address (basic validation)
     */
    public static String readEmail(String prompt) {
        while (true) {
            String email = readString(prompt);
            if (email.isEmpty()) {
                return email; // Allow empty email
            }
            if (isValidEmail(email)) {
                return email;
            }
            System.out.println("Please enter a valid email address.");
        }
    }

    /**
     * Read a phone number (basic validation)
     */
    public static String readPhoneNumber(String prompt) {
        while (true) {
            String phone = readString(prompt);
            if (phone.isEmpty()) {
                return phone; // Allow empty phone
            }
            if (isValidPhoneNumber(phone)) {
                return phone;
            }
            System.out.println("Please enter a valid phone number (digits, spaces, hyphens, and parentheses allowed).");
        }
    }

    /**
     * Basic email validation
     */
    private static boolean isValidEmail(String email) {
        return email.contains("@") && email.contains(".") && email.length() > 5;
    }

    /**
     * Basic phone number validation
     */
    private static boolean isValidPhoneNumber(String phone) {
        return phone.matches("^[\\d\\s\\-\\(\\)\\+]+$") && phone.replaceAll("[^\\d]", "").length() >= 10;
    }

    /**
     * Clear the console screen (works on most terminals)
     */
    public static void clearScreen() {
        try {
            // Try to clear screen using ANSI escape codes
            System.out.print("\033[2J\033[H");
            System.out.flush();
        } catch (Exception e) {
            // If that doesn't work, print some empty lines
            for (int i = 0; i < 50; i++) {
                System.out.println();
            }
        }
    }

    /**
     * Print a line separator
     */
    public static void printSeparator() {
        System.out.println("=" + "=".repeat(60));
    }

    /**
     * Print a header with borders
     */
    public static void printHeader(String title) {
        printSeparator();
        System.out.println("  " + title);
        printSeparator();
    }

    /**
     * Close the scanner (call this when application exits)
     */
    public static void closeScanner() {
        scanner.close();
    }
}
