<%@ page import="java.sql.*" %>
    <%@ page import="util.JDBC" %>
        <%@ page contentType="text/html" pageEncoding="UTF-8" %>
            <!DOCTYPE html>
            <html>

            <head>
                <title>Seed Final</title>
            </head>

            <body>
                <h1>Seeding Data (Safe Mode)</h1>
                <% Connection conn=null; Statement stmt=null; try { conn=JDBC.getConnection();
                    stmt=conn.createStatement(); // 1. Users String
                    sqlUsers="INSERT IGNORE INTO user (id, nama, email, password, role) VALUES "
                    + "('u1', 'Admin MoneyMate', 'admin@moneymate.com', 'admin123', 'admin'), "
                    + "('u2', 'User Test', 'user@moneymate.com', 'user123', 'user')" ; stmt.executeUpdate(sqlUsers);
                    out.println("Users OK<br />");

                // 2. Categories Income
                String sqlCatIn = "INSERT IGNORE INTO kategori (id, nama, tipe) VALUES " +
                "('k1', 'Gaji', 'pemasukan'), " +
                "('k2', 'Hadiah', 'pemasukan'), " +
                "('k3', 'Investasi', 'pemasukan'), " +
                "('k4', 'Penjualan', 'pemasukan'), " +
                "('k5', 'Lainnya', 'pemasukan')";
                stmt.executeUpdate(sqlCatIn);
                out.println("Income OK<br />");

                // 3. Categories Expense
                String sqlCatOut = "INSERT IGNORE INTO kategori (id, nama, tipe) VALUES " +
                "('k6', 'Makanan', 'pengeluaran'), " +
                "('k7', 'Transport', 'pengeluaran'), " +
                "('k8', 'Belanja', 'pengeluaran'), " +
                "('k9', 'Tagihan', 'pengeluaran')";
                stmt.executeUpdate(sqlCatOut);
                out.println("Expense OK<br />");

                out.println("<h2>SUCCESS! Database Ready.</h2>");
                out.println("<a href='index.jsp'>Back to Dashboard</a>");

                } catch (Throwable t) {
                out.println("<h2 style='color:red'>ERROR: " + t.getMessage() + "</h2>");
                t.printStackTrace(new java.io.PrintWriter(out));
                } finally {
                if (stmt != null) try { stmt.close(); } catch (Exception e) {}
                if (conn != null) try { conn.close(); } catch (Exception e) {}
                }
                %>
            </body>

            </html>