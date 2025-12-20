package util;

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
}
