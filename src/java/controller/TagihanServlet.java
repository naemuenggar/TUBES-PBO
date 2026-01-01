
package controller;

import model.Tagihan;
import util.JDBC;
import model.Kategori; // Added import
import util.ParseUtils; // Added import
import java.sql.Date;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.*;
import java.util.*;
import util.IdGenerator;

import model.User;

public class TagihanServlet extends HttpServlet {

    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String action = req.getParameter("action");
        try {
            if ("edit".equals(action)) {
                String id = req.getParameter("id");
                Tagihan tagihan = getById(id);
                req.setAttribute("tagihan", tagihan);
                req.setAttribute("users", getAllUsers());
                req.getRequestDispatcher("/model/Tagihan-form.jsp").forward(req, res);
                req.getRequestDispatcher("/model/Tagihan-form.jsp").forward(req, res);
            } else if ("payBill".equals(action)) {
                String id = req.getParameter("id");
                Tagihan tagihan = getById(id);
                req.setAttribute("tagihan", tagihan);
                req.setAttribute("kategoris", getAllKategori()); // Fetch categories for dropdown
                req.getRequestDispatcher("/model/Bayar-tagihan.jsp").forward(req, res);
            } else if ("form".equals(action)) {
                req.setAttribute("users", getAllUsers());
                req.getRequestDispatcher("/model/Tagihan-form.jsp").forward(req, res);
            } else if ("delete".equals(action)) {
                delete(req.getParameter("id"));
                res.sendRedirect("TagihanServlet");
            } else {
                List<Tagihan> list = getAll();
                req.setAttribute("tagihanList", list);
                req.getRequestDispatcher("/model/Tagihan-list.jsp").forward(req, res);
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String action = req.getParameter("action");
        if ("processPayBill".equals(action)) {
            processPayBill(req, res);
            return;
        }
        
        String id = req.getParameter("id");
        if (id == null || id.trim().isEmpty()) {
            try (Connection conn = JDBC.getConnection()) {
                id = IdGenerator.getNextId(conn, "tagihan");
            } catch (SQLException e) {
                e.printStackTrace();
                id = IdGenerator.generateSimple(); // Fallback
            }
        }

        Tagihan t = new Tagihan(
                id,
                req.getParameter("userId"),
                req.getParameter("nama"),
                Double.parseDouble(req.getParameter("jumlah")),
                java.sql.Date.valueOf(req.getParameter("tanggalJatuhTempo")));

        try {
            if (getById(t.getId()) != null) {
                update(t);
            } else {
                insert(t);
            }
            res.sendRedirect("TagihanServlet");
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private void insert(Tagihan t) throws SQLException {
        String sql = "INSERT INTO tagihan (id, user_id, nama, jumlah, tanggal_jatuh_tempo) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = JDBC.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, t.getId());
            stmt.setString(2, t.getUserId());
            stmt.setString(3, t.getNama());
            stmt.setDouble(4, t.getJumlah());
            stmt.setDate(5, (java.sql.Date) t.getTanggalJatuhTempo());
            stmt.executeUpdate();
        }
    }

    private void update(Tagihan t) throws SQLException {
        String sql = "UPDATE tagihan SET user_id=?, nama=?, jumlah=?, tanggal_jatuh_tempo=? WHERE id=?";
        try (Connection conn = JDBC.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, t.getUserId());
            stmt.setString(2, t.getNama());
            stmt.setDouble(3, t.getJumlah());
            stmt.setDate(4, (java.sql.Date) t.getTanggalJatuhTempo());
            stmt.setString(5, t.getId());
            stmt.executeUpdate();
        }
    }

    private void delete(String id) throws SQLException {
        String sql = "DELETE FROM tagihan WHERE id=?";
        try (Connection conn = JDBC.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            stmt.executeUpdate();
        }
    }

    private Tagihan getById(String id) throws SQLException {
        String sql = "SELECT * FROM tagihan WHERE id=?";
        try (Connection conn = JDBC.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Tagihan(
                            rs.getString("id"),
                            rs.getString("user_id"),
                            rs.getString("nama"),
                            rs.getDouble("jumlah"),
                            rs.getDate("tanggal_jatuh_tempo"));
                }
            }
        }
        return null;
    }

    private List<Tagihan> getAll() throws SQLException {
        List<Tagihan> list = new ArrayList<>();
        String sql = "SELECT * FROM tagihan";
        try (Connection conn = JDBC.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Tagihan(
                        rs.getString("id"),
                        rs.getString("user_id"),
                        rs.getString("nama"),
                        rs.getDouble("jumlah"),
                        rs.getDate("tanggal_jatuh_tempo")));
            }
        }
        return list;
    }

    private List<User> getAllUsers() throws SQLException {
        List<User> list = new ArrayList<>();
        String sql = "SELECT * FROM user";
        try (Connection conn = JDBC.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new User(
                        rs.getString("id"),
                        rs.getString("nama"),
                        rs.getString("email"),
                        rs.getString("password")));
            }
        }
        return list;
    }
    private void processPayBill(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String tagihanId = req.getParameter("tagihanId");
        String userId = req.getParameter("userId");
        double jumlah = Double.parseDouble(req.getParameter("jumlah"));
        String kategoriId = req.getParameter("kategoriId");
        String deskripsi = req.getParameter("deskripsi");
        Date tanggal = Date.valueOf(req.getParameter("tanggal"));

        // 1. Insert Transaction (Pengeluaran)
        String transaksiId;
        try (Connection conn = JDBC.getConnection()) {
            transaksiId = IdGenerator.getNextId(conn, "transaksi");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ServletException("Error generating ID", e);
        }

        String insertSql = "INSERT INTO transaksi (id, user_id, jumlah, deskripsi, tanggal, kategori_id, jenis) VALUES (?, ?, ?, ?, ?, ?, ?)";
        String deleteSql = "DELETE FROM tagihan WHERE id=?";

        try (Connection conn = JDBC.getConnection()) {
            conn.setAutoCommit(false); // Transaction block
            try {
                // Insert Transaksi
                try (PreparedStatement stmt = conn.prepareStatement(insertSql)) {
                    stmt.setString(1, transaksiId);
                    stmt.setString(2, userId);
                    stmt.setDouble(3, jumlah);
                    stmt.setString(4, deskripsi);
                    stmt.setDate(5, tanggal);
                    stmt.setString(6, kategoriId);
                    stmt.setString(7, "pengeluaran");
                    stmt.executeUpdate();
                }

                // Delete Tagihan
                try (PreparedStatement stmt = conn.prepareStatement(deleteSql)) {
                    stmt.setString(1, tagihanId);
                    stmt.executeUpdate();
                }

                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new ServletException(e);
        }

        res.sendRedirect("TagihanServlet");
    }

    private List<Kategori> getAllKategori() throws SQLException {
        List<Kategori> list = new ArrayList<>();
        String sql = "SELECT * FROM kategori";
        try (Connection conn = JDBC.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Kategori(rs.getString("id"), rs.getString("nama")));
            }
        }
        return list;
    }
}
