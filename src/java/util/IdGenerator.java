package util;

import java.sql.*;
import java.util.Random;

public class IdGenerator {

    private static final Random random = new Random();

    /**
     * Generates a simple random number ID (1-9999).
     */
    public static String generateSimple() {
        return String.valueOf(1 + random.nextInt(9999));
    }

    /**
     * Generates an ID from the email (username part).
     * Example: "budi@gmail.com" -> "budi"
     */
    public static String generateFromEmail(String email) {
        if (email == null || !email.contains("@")) {
            return "user" + generateSimple();
        }
        return email.split("@")[0];
    }

    /**
     * Gets the next sequential ID (MAX + 1) for a table.
     */
    public static String getNextId(Connection conn, String tableName) throws SQLException {
        String sql = "SELECT MAX(CAST(id AS UNSIGNED)) FROM " + tableName;
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                long max = rs.getLong(1);
                return String.valueOf(max + 1);
            }
        }
        return "1";
    }
}
