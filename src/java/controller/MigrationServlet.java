package controller;

import util.JDBC;
import util.IdGenerator;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.*;

public class MigrationServlet extends HttpServlet {

    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("text/html");
        PrintWriter out = res.getWriter();

        out.println("<html><body>");
        out.println("<h2>Starting Database Migration to UUIDs...</h2>");
        out.println("<pre>");

        try (Connection conn = JDBC.getConnection()) {
            conn.setAutoCommit(false); // Transaction start

            try {
                // 1. Disable Foreign Key Checks
                out.println("Disabling foreign key checks...");
                try (Statement stmt = conn.createStatement()) {
                    stmt.execute("SET FOREIGN_KEY_CHECKS=0");
                }

                // 2. Migrate Users
                out.println("Migrating Users...");
                migrateTable(conn, out, "user", "id",
                        Arrays.asList(
                                new ForeignKeyRef("transaksi", "user_id"),
                                new ForeignKeyRef("tagihan", "user_id"),
                                new ForeignKeyRef("target_tabungan", "user_id")));

                // 3. Migrate Kategori
                out.println("Migrating Kategori...");
                migrateTable(conn, out, "kategori", "id",
                        Arrays.asList(
                                new ForeignKeyRef("transaksi", "kategori_id")));

                // 4. Migrate Transaksi (No children)
                out.println("Migrating Transaksi...");
                migrateTable(conn, out, "transaksi", "id", Collections.emptyList());

                // 5. Migrate Tagihan (No children)
                out.println("Migrating Tagihan...");
                migrateTable(conn, out, "tagihan", "id", Collections.emptyList());

                // 6. Migrate Target Tabungan (No children)
                out.println("Migrating Target Tabungan...");
                migrateTable(conn, out, "target_tabungan", "id", Collections.emptyList());

                // 7. Re-enable Foreign Key Checks
                out.println("Re-enabling foreign key checks...");
                try (Statement stmt = conn.createStatement()) {
                    stmt.execute("SET FOREIGN_KEY_CHECKS=1");
                }

                conn.commit();
                out.println("\nSUCCESS: Migration completed successfully!");
            } catch (Exception e) {
                conn.rollback();
                out.println("\nERROR: Migration failed! Logic rolled back.");
                e.printStackTrace(out);
            }

        } catch (SQLException e) {
            out.println("\nCRITICAL ERROR: Could not connect to database.");
            e.printStackTrace(out);
        }

        out.println("</pre>");
        out.println("<br><a href='index.jsp'>Go to Home</a>");
        out.println("</body></html>");
    }

    private void migrateTable(Connection conn, PrintWriter out, String tableName, String pkCol,
            List<ForeignKeyRef> children) throws SQLException {
        // Fetch all IDs
        List<String> oldIds = new ArrayList<>();
        try (Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT " + pkCol + " FROM " + tableName)) {
            while (rs.next()) {
                oldIds.add(rs.getString(pkCol));
            }
        }

        out.println("  - Found " + oldIds.size() + " records in " + tableName);

        String updateParentSql = "UPDATE " + tableName + " SET " + pkCol + " = ? WHERE " + pkCol + " = ?";

        try (PreparedStatement updateParent = conn.prepareStatement(updateParentSql)) {
            for (String oldId : oldIds) {
                // Skip if already a UUID (simple check: length > 20 and contains '-')
                if (oldId.length() > 20 && oldId.contains("-")) {
                    out.println("    - Skipping " + oldId + " (Already UUID)");
                    continue;
                }

                String newId;

                // Special handling for User table: Generate ID from email
                if (tableName.equalsIgnoreCase("user")) {
                    String email = "";
                    // We need to fetch the email for this specific user ID
                    try (PreparedStatement stmtEmail = conn.prepareStatement("SELECT email FROM user WHERE id = ?")) {
                        stmtEmail.setString(1, oldId);
                        try (ResultSet rsEmail = stmtEmail.executeQuery()) {
                            if (rsEmail.next()) {
                                email = rsEmail.getString("email");
                            }
                        }
                    }
                    newId = IdGenerator.generateFromEmail(email);
                } else {
                    // For all other tables, use simple random ID
                    newId = IdGenerator.generateSimple();
                }

                // Update parent
                updateParent.setString(1, newId);
                updateParent.setString(2, oldId);
                updateParent.executeUpdate();

                // Update children
                for (ForeignKeyRef child : children) {
                    String updateChildSql = "UPDATE " + child.table + " SET " + child.col + " = ? WHERE " + child.col
                            + " = ?";
                    try (PreparedStatement updateChild = conn.prepareStatement(updateChildSql)) {
                        updateChild.setString(1, newId);
                        updateChild.setString(2, oldId);
                        int rows = updateChild.executeUpdate();
                        if (rows > 0) {
                            out.println("    - Updated " + rows + " references in " + child.table);
                        }
                    }
                }
                out.println("    - Migrated " + oldId + " -> " + newId);
            }
        }
    }

    private static class ForeignKeyRef {
        String table;
        String col;

        public ForeignKeyRef(String table, String col) {
            this.table = table;
            this.col = col;
        }
    }
}
