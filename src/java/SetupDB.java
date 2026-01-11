
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SetupDB {
    private static final String URL_NO_DB = "jdbc:mysql://localhost:3306/";
    private static final String USER = "root";
    private static final String PASS = ""; // Default XAMPP password
    private static final Logger LOGGER = Logger.getLogger(SetupDB.class.getName());

    public static void main(String[] args) {
        LOGGER.log(Level.INFO, "Initializing Database Setup...");

        try {
            // 1. Connect without DB to create it
            try (Connection conn = DriverManager.getConnection(URL_NO_DB, USER, PASS);
                    Statement stmt = conn.createStatement()) {

                LOGGER.log(Level.INFO, "Connected to MySQL. Creating database if not exists...");
                stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS MoneyMate");
                LOGGER.log(Level.INFO, "Database 'MoneyMate' ensured.");
            }

            // 2. Connect to the new DB and run the script
            String urlWithDB = URL_NO_DB + "MoneyMate";
            try (Connection conn = DriverManager.getConnection(urlWithDB, USER, PASS);
                    Statement stmt = conn.createStatement()) {

                LOGGER.log(Level.INFO, "Connected to 'MoneyMate'. Executing schema script...");

                File sqlFile = new File("database/MoneyMate.sql");
                try (BufferedReader reader = new BufferedReader(new FileReader(sqlFile))) {
                    String line;
                    StringBuilder sql = new StringBuilder();

                    while ((line = reader.readLine()) != null) {
                        // Primitive SQL parser: split by semicolon if at end of line
                        if (line.trim().startsWith("--") || line.trim().isEmpty()) {
                            continue;
                        }
                        sql.append(line).append(" ");
                        if (line.trim().endsWith(";")) {
                            // Execute chunk
                            try {
                                stmt.execute(sql.toString());
                                // System.out.print("."); // Removed Code Smell
                            } catch (SQLException e) {
                                LOGGER.log(Level.WARNING, "Warning executing SQL: " + sql.toString(), e);
                            }
                            sql = new StringBuilder();
                        }
                    }
                }
                LOGGER.log(Level.INFO, "Database schema imported successfully!");

                // 3. Seed Data
                seedData(conn);
            }

        } catch (SQLException e) { // Changed from generic Exception
            LOGGER.log(Level.SEVERE,
                    "Database setup failed due to SQL error. Ensure MySQL is running and credentials are correct.", e);
            System.exit(1);
        } catch (IOException e) { // Added specific catch for file operations
            LOGGER.log(Level.SEVERE, "Database setup failed due to file I/O error (e.g., SQL script not found).", e);
            System.exit(1);
        }
    }

    private static void seedData(Connection conn) {
        LOGGER.log(Level.INFO, "Seeding initial data..."); // Changed from System.out.println

        String insertUser = "INSERT IGNORE INTO user (id, nama, email, password, role) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement testPstmt = conn.prepareStatement(insertUser)) {
            // Seed Users
            safeExecUser(testPstmt, "u1", "Admin MoneyMate", "admin@moneymate.com", "admin123", "admin");
            safeExecUser(testPstmt, "u2", "User Test", "user@moneymate.com", "user123", "user");

            // Seed Categories (Pemasukan)
            String insertKategori = "INSERT IGNORE INTO kategori (id, nama, tipe) VALUES (?, ?, ?)";
            try (PreparedStatement katPstmt = conn.prepareStatement(insertKategori)) {
                safeExecKategori(katPstmt, "k1", "Gaji", "pemasukan");
                safeExecKategori(katPstmt, "k2", "Hadiah", "pemasukan");
                safeExecKategori(katPstmt, "k3", "Investasi", "pemasukan");
                safeExecKategori(katPstmt, "k4", "Penjualan", "pemasukan");
                safeExecKategori(katPstmt, "k5", "Lainnya", "pemasukan");

                // Seed Categories (Pengeluaran)
                safeExecKategori(katPstmt, "k6", "Makanan & Minuman", "pengeluaran");
                safeExecKategori(katPstmt, "k7", "Transportasi", "pengeluaran");
                safeExecKategori(katPstmt, "k8", "Belanja", "pengeluaran");
                safeExecKategori(katPstmt, "k9", "Tagihan & Utilitas", "pengeluaran");
                safeExecKategori(katPstmt, "k10", "Hiburan", "pengeluaran");
                safeExecKategori(katPstmt, "k11", "Kesehatan", "pengeluaran");
                safeExecKategori(katPstmt, "k12", "Pendidikan", "pengeluaran");
                safeExecKategori(katPstmt, "k13", "Asuransi", "pengeluaran");
            }

            LOGGER.log(Level.INFO, "Data seeding completed!");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error seeding data", e);
        }
    }

    private static void safeExecUser(PreparedStatement pstmt, String id, String nama, String email, String password,
            String role) {
        try {
            pstmt.setString(1, id);
            pstmt.setString(2, nama);
            pstmt.setString(3, email);
            pstmt.setString(4, password);
            pstmt.setString(5, role);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            // Ignore (likely duplicate or constraint violation)
            LOGGER.log(Level.INFO, "Skipping duplicate/error user: " + id, e);
        }
    }

    private static void safeExecKategori(PreparedStatement pstmt, String id, String nama, String tipe) {
        try {
            pstmt.setString(1, id);
            pstmt.setString(2, nama);
            pstmt.setString(3, tipe);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            // Ignore (likely duplicate or constraint violation)
            LOGGER.log(Level.INFO, "Skipping duplicate/error kategori: " + id, e);
        }
    }
}
