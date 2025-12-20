import util.JDBC;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.DatabaseMetaData;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;

public class TestDB {
    public static void main(String[] args) {
        try (PrintWriter out = new PrintWriter(new FileWriter("diagnostic_log.txt"))) {
            out.println("=== DIAGNOSTIC TEST START ===");
            System.out.println("Running diagnostic... check diagnostic_log.txt");

            // 1. Connection Test
            out.println("\n[TEST 1] Connecting to Database...");
            try (Connection conn = JDBC.getConnection()) {
                out.println("SUCCESS: Connected to " + conn.getMetaData().getURL());

                // 2. Schema Test
                out.println("\n[Test 2] Checking Table Schema...");
                checkColumn(conn, "user", "role", out);

                // 3. Insert Test
                out.println("\n[TEST 3] Simulating Registration (INSERT)...");
                String testId = "test_" + System.currentTimeMillis();
                String testEmail = "test" + System.currentTimeMillis() + "@example.com";

                String sql = "INSERT INTO user (id, nama, email, password, role) VALUES (?, ?, ?, ?, ?)";
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, testId);
                    stmt.setString(2, "Test User");
                    stmt.setString(3, testEmail);
                    stmt.setString(4, "hashed");
                    stmt.setString(5, "user");

                    int rows = stmt.executeUpdate();
                    out.println("SUCCESS: Inserted " + rows + " row(s).");

                    conn.createStatement().execute("DELETE FROM user WHERE id = '" + testId + "'");
                    out.println("Cleanup: Deleted test user.");

                } catch (SQLException e) {
                    out.println("FAILURE: Insert Failed!");
                    out.println("State: " + e.getSQLState());
                    out.println("Code: " + e.getErrorCode());
                    out.println("Message: " + e.getMessage());
                    e.printStackTrace(out);
                }

            } catch (SQLException e) {
                out.println("FAILURE: Connection Failed!");
                out.println("Message: " + e.getMessage());
                e.printStackTrace(out);
            } catch (Exception e) {
                out.println("FAILURE: Critical Error!");
                e.printStackTrace(out);
            }
            out.println("\n=== DIAGNOSTIC TEST END ===");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void checkColumn(Connection conn, String table, String colToCheck, PrintWriter out)
            throws SQLException {
        DatabaseMetaData meta = conn.getMetaData();
        ResultSet rs = meta.getColumns(null, null, table, colToCheck);
        if (rs.next()) {
            out.println("Column '" + table + "." + colToCheck + "': FOUND (Type: " + rs.getString("TYPE_NAME") + ")");
        } else {
            out.println("Column '" + table + "." + colToCheck + "': MISSING");
        }
    }
}
