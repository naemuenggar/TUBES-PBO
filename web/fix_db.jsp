<%@ page import="java.sql.*" %>
    <%@ page import="util.JDBC" %>
        <%@ page contentType="text/html" pageEncoding="UTF-8" %>
            <html>

            <head>
                <title>DB Fixer</title>
            </head>

            <body>
                <h1>Fixing Database...</h1>
                <% Connection conn=null; Statement stmt=null; try { conn=JDBC.getConnection();
                    stmt=conn.createStatement(); // Users stmt.executeUpdate("INSERT IGNORE INTO user
                    (id,nama,email,password,role) VALUES ('u1','Admin','admin@moneymate.com','admin123','admin')");
                    stmt.executeUpdate("INSERT IGNORE INTO user (id,nama,email,password,role) VALUES
                    ('u2','User','user@moneymate.com','user123','user')"); // Income stmt.executeUpdate("INSERT IGNORE
                    INTO kategori (id,nama,tipe) VALUES ('k1','Gaji','pemasukan')"); stmt.executeUpdate("INSERT IGNORE
                    INTO kategori (id,nama,tipe) VALUES ('k2','Hadiah','pemasukan')"); stmt.executeUpdate("INSERT IGNORE
                    INTO kategori (id,nama,tipe) VALUES ('k3','Investasi','pemasukan')"); stmt.executeUpdate("INSERT
                    IGNORE INTO kategori (id,nama,tipe) VALUES ('k4','Penjualan','pemasukan')");
                    stmt.executeUpdate("INSERT IGNORE INTO kategori (id,nama,tipe) VALUES
                    ('k5','Lainnya','pemasukan')"); // Expense stmt.executeUpdate("INSERT IGNORE INTO kategori
                    (id,nama,tipe) VALUES ('k6','Makanan','pengeluaran')"); stmt.executeUpdate("INSERT IGNORE INTO
                    kategori (id,nama,tipe) VALUES ('k7','Transport','pengeluaran')"); stmt.executeUpdate("INSERT IGNORE
                    INTO kategori (id,nama,tipe) VALUES ('k8','Belanja','pengeluaran')"); stmt.executeUpdate("INSERT
                    IGNORE INTO kategori (id,nama,tipe) VALUES ('k9','Tagihan','pengeluaran')");
                    stmt.executeUpdate("INSERT IGNORE INTO kategori (id,nama,tipe) VALUES
                    ('k10','Hiburan','pengeluaran')"); stmt.executeUpdate("INSERT IGNORE INTO kategori (id,nama,tipe)
                    VALUES ('k11','Kesehatan','pengeluaran')"); stmt.executeUpdate("INSERT IGNORE INTO kategori
                    (id,nama,tipe) VALUES ('k12','Pendidikan','pengeluaran')"); stmt.executeUpdate("INSERT IGNORE INTO
                    kategori (id,nama,tipe) VALUES ('k13','Asuransi','pengeluaran')"); out.println("<h2
                    style='color:green'>SUCCESS! Fixed.</h2>");
                    out.println("<a href='index.jsp'>Go to Dashboard</a>");

                    } catch (Throwable t) {
                    out.println("<h2 style='color:red'>ERROR</h2>");
                    out.println(t.getMessage());
                    t.printStackTrace(new java.io.PrintWriter(out));
                    } finally {
                    if(stmt!=null) try{stmt.close();}catch(Exception e){}
                    if(conn!=null) try{conn.close();}catch(Exception e){}
                    }
                    %>
            </body>

            </html>