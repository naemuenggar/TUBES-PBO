<%@ page import="java.sql.*, util.JDBC" %>
    <%@ page contentType="text/html;charset=UTF-8" language="java" %>
        <html>

        <head>
            <title>Update Database Schema</title>
        </head>

        <body>
            <h2>Updating Database Schema...</h2>
            <pre>
<%
    try (Connection conn = JDBC.getConnection()) {
        Statement stmt = conn.createStatement();

        // 1. Add 'terisi' column to 'anggaran' table
        try {
            stmt.executeUpdate("ALTER TABLE anggaran ADD COLUMN terisi DECIMAL(20,2) NOT NULL DEFAULT 0");
            out.println("SUCCESS: Added 'terisi' column to 'anggaran'.");
        } catch (SQLException e) {
            if (e.getMessage().contains("Duplicate column")) {
                 out.println("INFO: Column 'terisi' already exists.");
            } else {
                 out.println("ERROR adding column: " + e.getMessage());
            }
        }

        // 2. Add 'Alokasi Anggaran' category
        try {
            stmt.executeUpdate("INSERT INTO kategori (id, nama, tipe) VALUES ('k_alloc', 'Alokasi Anggaran', 'pengeluaran')");
            out.println("SUCCESS: Added 'Alokasi Anggaran' category.");
        } catch (SQLException e) {
             out.println("INFO: Category 'Alokasi Anggaran' might already exist: " + e.getMessage());
        }

        // 3. Add 'Tarik Anggaran' category
        try {
            stmt.executeUpdate("INSERT INTO kategori (id, nama, tipe) VALUES ('k_pull', 'Tarik Anggaran', 'pemasukan')");
            out.println("SUCCESS: Added 'Tarik Anggaran' category.");
        } catch (SQLException e) {
             out.println("INFO: Category 'Tarik Anggaran' might already exist: " + e.getMessage());
        }

    } catch (Exception e) {
        out.println("CRITICAL ERROR: " + e.getMessage());
        e.printStackTrace(new java.io.PrintWriter(out));
    }
%>
    </pre>
            <p>Done.</p>
        </body>

        </html>