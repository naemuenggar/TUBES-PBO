<%@ page import="java.sql.*,util.JDBC" %>
    <%@ page contentType="text/html" pageEncoding="UTF-8" %>
        <html>

        <head>
            <title>Final Fix</title>
        </head>

        <body>
            <h1>Final Database Fix</h1>
            <% try { Connection c=JDBC.getConnection(); Statement s=c.createStatement(); String
                u1="INSERT IGNORE INTO user (id,nama,email,password,role) VALUES ('u1','Admin','admin@moneymate.com','admin123','admin')"
                ; s.executeUpdate(u1); String
                u2="INSERT IGNORE INTO user (id,nama,email,password,role) VALUES ('u2','User','user@moneymate.com','user123','user')"
                ; s.executeUpdate(u2); s.executeUpdate("INSERT IGNORE INTO kategori (id,nama,tipe) VALUES
                ('k1','Gaji','pemasukan')"); s.executeUpdate("INSERT IGNORE INTO kategori (id,nama,tipe) VALUES
                ('k2','Hadiah','pemasukan')"); s.executeUpdate("INSERT IGNORE INTO kategori (id,nama,tipe) VALUES
                ('k3','Investasi','pemasukan')"); s.executeUpdate("INSERT IGNORE INTO kategori (id,nama,tipe) VALUES
                ('k4','Penjualan','pemasukan')"); s.executeUpdate("INSERT IGNORE INTO kategori (id,nama,tipe) VALUES
                ('k5','Lainnya','pemasukan')"); s.executeUpdate("INSERT IGNORE INTO kategori (id,nama,tipe) VALUES
                ('k6','Makanan','pengeluaran')"); s.executeUpdate("INSERT IGNORE INTO kategori (id,nama,tipe) VALUES
                ('k7','Transport','pengeluaran')"); s.executeUpdate("INSERT IGNORE INTO kategori (id,nama,tipe) VALUES
                ('k8','Belanja','pengeluaran')"); s.executeUpdate("INSERT IGNORE INTO kategori (id,nama,tipe) VALUES
                ('k9','Tagihan','pengeluaran')"); s.executeUpdate("INSERT IGNORE INTO kategori (id,nama,tipe) VALUES
                ('k10','Hiburan','pengeluaran')"); s.executeUpdate("INSERT IGNORE INTO kategori (id,nama,tipe) VALUES
                ('k11','Kesehatan','pengeluaran')"); s.executeUpdate("INSERT IGNORE INTO kategori (id,nama,tipe) VALUES
                ('k12','Pendidikan','pengeluaran')"); s.executeUpdate("INSERT IGNORE INTO kategori (id,nama,tipe) VALUES
                ('k13','Asuransi','pengeluaran')"); out.println("<h2 style='color:green'>SUCCESS</h2>");
                out.println("<a href='index.jsp'>Dashboard</a>");
                c.close();
                } catch (Throwable t) {
                out.println("<h2 style='color:red'>ERROR</h2>");
                out.println(t.getMessage());
                }
                %>
        </body>

        </html>