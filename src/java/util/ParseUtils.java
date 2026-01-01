package util;

public class ParseUtils {
    public static double parseDouble(String value) {
        if (value == null || value.trim().isEmpty()) {
            return 0.0;
        }
        // Remove commas and handle potential formatting issues
        String cleanValue = value.replace(",", "").trim();
        try {
            return Double.parseDouble(cleanValue);
        } catch (NumberFormatException e) {
            System.err.println("Error parsing double: " + value);
            return 0.0; // Fail safe
        }
    }
}
