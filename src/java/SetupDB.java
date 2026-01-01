
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class SetupDB {
    private static final String URL_NO_DB = "jdbc:mysql://localhost:3306/";
    private static final String USER = "root";
    private static final String PASS = ""; // Default XAMPP password

    public static void main(String[] args) {
        System.out.println("Initializing Database Setup...");

        try {
            // 1. Connect without DB to create it
            try (Connection conn = DriverManager.getConnection(URL_NO_DB, USER, PASS);
                    Statement stmt = conn.createStatement()) {

                System.out.println("Connected to MySQL. Creating database if not exists...");
                stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS MoneyMate");
                System.out.println("Database 'MoneyMate' ensured.");
            }

            // 2. Connect to the new DB and run the script
            String urlWithDB = URL_NO_DB + "MoneyMate";
            try (Connection conn = DriverManager.getConnection(urlWithDB, USER, PASS);
                    Statement stmt = conn.createStatement()) {

                System.out.println("Connected to 'MoneyMate'. Executing schema script...");

                File sqlFile = new File("database/MoneyMate.sql");
                BufferedReader reader = new BufferedReader(new FileReader(sqlFile));
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
                            System.out.print(".");
                        } catch (Exception e) {
                            System.out.println("\nWarning executing: " + sql.toString());
                            System.out.println(e.getMessage());
                        }
                        sql = new StringBuilder();
                    }
                }
                System.out.println("\nDatabase schema imported successfully!");
                
                // 3. Seed Data
                seedData(conn);
            }

        } catch (Exception e) {
            System.err.println("\nERROR: Could not setup database.");
            System.err.println("Make sure MySQL is running (XAMPP?) and password is empty for 'root'.");
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static void seedData(Connection conn) {
        System.out.println("Seeding initial data...");
        try (Statement stmt = conn.createStatement()) {
            // Seed Users
            safeExec(stmt, "INSERT IGNORE INTO user (id, nama, email, password, role) VALUES " +
                    "('u1', 'Admin MoneyMate', 'admin@moneymate.com', 'admin123', 'admin'), " +
                    "('u2', 'User Test', 'user@moneymate.com', 'user123', 'user')");
            
            // Seed Categories (Pemasukan)
            safeExec(stmt, "INSERT IGNORE INTO kategori (id, nama, tipe) VALUES " +
                    "('k1', 'Gaji', 'pemasukan'), " +
                    "('k2', 'Hadiah', 'pemasukan'), " +
                    "('k3', 'Investasi', 'pemasukan'), " +
                    "('k4', 'Penjualan', 'pemasukan'), " +
                    "('k5', 'Lainnya', 'pemasukan')");

            // Seed Categories (Pengeluaran)
            safeExec(stmt, "INSERT IGNORE INTO kategori (id, nama, tipe) VALUES " +
                    "('k6', 'Makanan & Minuman', 'pengeluaran'), " +
                    "('k7', 'Transportasi', 'pengeluaran'), " +
                    "('k8', 'Belanja', 'pengeluaran'), " +
                    "('k9', 'Tagihan & Utilitas', 'pengeluaran'), " +
                    "('k10', 'Hiburan', 'pengeluaran'), " +
                    "('k11', 'Kesehatan', 'pengeluaran'), " +
                    "('k12', 'Pendidikan', 'pengeluaran'), " +
                    "('k13', 'Asuransi', 'pengeluaran')");

            System.out.println("Data seeding completed!");
        } catch (Exception e) {
            System.err.println("Error seeding data: " + e.getMessage());
        }
    }

    private static void safeExec(Statement stmt, String sql) {
        try {
            stmt.executeUpdate(sql);
        } catch (Exception e) {
            // Ignore (likely duplicate)
            System.out.println("Info: " + e.getMessage());
        }
    }
}
