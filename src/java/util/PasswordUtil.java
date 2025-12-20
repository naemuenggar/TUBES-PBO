
package util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Utility class for password hashing using SHA-256 with salt
 */
public class PasswordUtil {

    private static final int SALT_LENGTH = 16;

    /**
     * Generates a random salt for password hashing
     */
    private static byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_LENGTH];
        random.nextBytes(salt);
        return salt;
    }

    /**
     * Hashes a password using SHA-256 with a random salt
     * Returns the salt and hash combined as: salt$hash (both Base64 encoded)
     */
    public static String hashPassword(String password) {
        try {
            byte[] salt = generateSalt();
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt);
            byte[] hashedPassword = md.digest(password.getBytes());

            String saltBase64 = Base64.getEncoder().encodeToString(salt);
            String hashBase64 = Base64.getEncoder().encodeToString(hashedPassword);

            return saltBase64 + "$" + hashBase64;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }

    /**
     * Verifies a password against a stored hash
     * The storedHash should be in format: salt$hash
     */
    public static boolean verifyPassword(String password, String storedHash) {
        try {
            if (storedHash == null || !storedHash.contains("$")) {
                // Legacy password (not hashed) - for backward compatibility
                return password.equals(storedHash);
            }

            String[] parts = storedHash.split("\\$");
            if (parts.length != 2) {
                return false;
            }

            byte[] salt = Base64.getDecoder().decode(parts[0]);
            byte[] expectedHash = Base64.getDecoder().decode(parts[1]);

            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt);
            byte[] actualHash = md.digest(password.getBytes());

            // Constant-time comparison to prevent timing attacks
            if (expectedHash.length != actualHash.length) {
                return false;
            }

            int result = 0;
            for (int i = 0; i < expectedHash.length; i++) {
                result |= expectedHash[i] ^ actualHash[i];
            }

            return result == 0;
        } catch (Exception e) {
            return false;
        }
    }
}
