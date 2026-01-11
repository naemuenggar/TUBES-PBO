<%@ page import="java.sql.*" %>
    <%@ page import="util.JDBC" %>
        <%@ page contentType="text/html" pageEncoding="UTF-8" %>
            <!DOCTYPE html>
            <html>

            <head>
                <title>Database Fixer: RBAC & Schema</title>
                <style>
                    body {
                        font-family: sans-serif;
                        padding: 2rem;
                        line-height: 1.6;
                    }

                    .success {
                        color: green;
                        font-weight: bold;
                    }

                    .error {
                        color: red;
                        font-weight: bold;
                    }

                    .log {
                        background: #f4f4f4;
                        padding: 1rem;
                        border-radius: 4px;
                        border: 1px solid #ddd;
                    }
                </style>
            </head>

            <body>
                <h1>Database Repair Tool</h1>
                <div class="log">
                    <% Connection conn=null; Statement stmt=null; try { conn=JDBC.getConnection();
                        stmt=conn.createStatement(); out.println("Wait... Connecting to database... OK<br />");

                    // 1. Check if 'role' column exists in 'user' table
                    boolean roleExists = false;
                    DatabaseMetaData meta = conn.getMetaData();
                    ResultSet rs = meta.getColumns(null, null, "user", "role");
                    if (rs.next()) {
                    roleExists = true;
                    out.println("Check: Column 'role' found. <br />");
                    } else {
                    out.println("Check: Column 'role' NOT found. Attempting to add... <br />");
                    }
                    rs.close();

                    // 2. Add or Modify Role Column
                    if (!roleExists) {
                    try {
                    stmt.executeUpdate("ALTER TABLE user ADD COLUMN role ENUM('admin', 'user') NOT NULL DEFAULT 'user'
                    AFTER password");
                    out.println("<span class='success'>SUCCESS: Added 'role' column.</span><br />");
                    } catch (Exception e) {
                    out.println("<span class='error'>WARN: Failed to add column (might already exist hidden?): " +
                        e.getMessage() + "</span><br />");
                    }
                    } else {
                    // Optional: Ensure definition is correct
                    try {
                    stmt.executeUpdate("ALTER TABLE user MODIFY COLUMN role ENUM('admin', 'user') NOT NULL DEFAULT
                    'user'");
                    out.println("<span class='success'>SUCCESS: Verified/Updated 'role' column
                        definition.</span><br />");
                    } catch (Exception e) {
                    out.println("<span class='error'>WARN: update role definition failed: " + e.getMessage() +
                        "</span><br />");
                    }
                    }

                    // 3. Seed Users (Idempotent)
                    String sqlAdmin = "INSERT INTO user (id, nama, email, password, role) VALUES ('admin001',
                    'Administrator', 'admin@moneymate.com', 'admin123', 'admin') ON DUPLICATE KEY UPDATE role='admin',
                    password='admin123'";
                    stmt.executeUpdate(sqlAdmin);
                    out.println("Info: Seeding Admin user... OK<br />");

                    String sqlUser = "INSERT INTO user (id, nama, email, password, role) VALUES ('user001', 'Demo User',
                    'user@moneymate.com', 'user123', 'user') ON DUPLICATE KEY UPDATE role='user', password='user123'";
                    stmt.executeUpdate(sqlUser);
                    out.println("Info: Seeding Demo user... OK<br />");

                    out.println("
                    <hr />");
                    out.println("<h2 class='success'>ALL DONE! Database is ready.</h2>");
                    out.println("<a href='index.jsp'>Go to Login / Dashboard</a>");

                    } catch (Exception e) {
                    out.println("<h2 class='error'>CRITICAL ERROR</h2>");
                    out.println(e.getMessage() + "<br />");
                    e.printStackTrace(new java.io.PrintWriter(out));
                    } finally {
                    if(stmt != null) try { stmt.close(); } catch(Exception e){}
                    if(conn != null) try { conn.close(); } catch(Exception e){}
                    }
                    %>
                </div>
            </body>

            </html>