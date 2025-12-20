
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
            }

        } catch (Exception e) {
            System.err.println("\nERROR: Could not setup database.");
            System.err.println("Make sure MySQL is running (XAMPP?) and password is empty for 'root'.");
            e.printStackTrace();
            System.exit(1);
        }
    }
}
