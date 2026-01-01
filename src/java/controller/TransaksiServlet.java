
package controller;

import model.Transaksi;
import model.User;
import model.Kategori;
import util.JDBC;
import util.ParseUtils; // Added import
import java.sql.Date;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.*;
import java.util.*;
// import java.util.UUID; // Removed since using IdGenerator
import util.IdGenerator;

public class TransaksiServlet extends HttpServlet {

    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String action = req.getParameter("action");
        try {
            if ("edit".equals(action)) {
                String id = req.getParameter("id");
                Transaksi transaksi = getTransaksiById(id);
                req.setAttribute("transaksi", transaksi);

                // Helper data for dropdowns
                req.setAttribute("users", getAllUsers());
                req.setAttribute("kategoris", getAllKategori());

                req.getRequestDispatcher("/model/Transaksi-form.jsp").forward(req, res);
            } else if ("form".equals(action)) {
                // Prepare data for new transaction form
                req.setAttribute("users", getAllUsers());
                req.setAttribute("kategoris", getAllKategori());
                req.getRequestDispatcher("/model/Transaksi-form.jsp").forward(req, res);
            } else if ("payBill".equals(action)) {
                // Pre-fill form from Tagihan data
                String nama = req.getParameter("nama");
                Double jumlah = Double.parseDouble(req.getParameter("jumlah"));
                String userId = req.getParameter("userId");
                String tagihanId = req.getParameter("id"); // To delete later

                Transaksi t = new Transaksi();
                t.setDeskripsi("Bayar Tagihan: " + nama);
                t.setJumlah(jumlah);
                t.setUserId(userId);

                t.setTanggal(new java.util.Date()); // Today
                t.setJenis("pengeluaran"); // Default

                req.setAttribute("transaksi", t);
                req.setAttribute("users", getAllUsers());
                req.setAttribute("kategoris", getAllKategori());
                req.setAttribute("sourceTagihanId", tagihanId); // Pass to form

                req.getRequestDispatcher("/model/Transaksi-form.jsp").forward(req, res);
            } else if ("delete".equals(action)) {
                deleteTransaksi(req.getParameter("id"));
                res.sendRedirect("TransaksiServlet");
            } else {
                List<Transaksi> list = getAllTransaksi();
                req.setAttribute("transaksis", list);
                req.getRequestDispatcher("/model/Transaksi-list.jsp").forward(req, res);
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        System.out.println("DEBUG POST: userId=" + req.getParameter("userId"));
        System.out.println("DEBUG POST: kategoriId=" + req.getParameter("kategoriId"));
        System.out.println("DEBUG POST: jumlah=" + req.getParameter("jumlah"));

        String id = req.getParameter("id");
        if (id == null || id.trim().isEmpty()) {
            try (Connection conn = JDBC.getConnection()) {
                id = IdGenerator.getNextId(conn, "transaksi");
            } catch (SQLException e) {
                e.printStackTrace();
                id = IdGenerator.generateSimple(); // Fallback
            }
        }

        Transaksi t = new Transaksi(
                id,
                req.getParameter("userId"),
                Double.parseDouble(req.getParameter("jumlah")),
                req.getParameter("deskripsi"),
                Date.valueOf(req.getParameter("tanggal")),
                req.getParameter("kategoriId"),
                req.getParameter("jenis"));

        try {
            if (getTransaksiById(t.getId()) != null) {
                updateTransaksi(t);
            } else {
                insertTransaksi(t);

                // Logic: Delete Tagihan if this was a bill payment
                String sourceTagihanId = req.getParameter("sourceTagihanId");
                if (sourceTagihanId != null && !sourceTagihanId.isEmpty()) {
                    deleteTagihan(sourceTagihanId);
                }
            }
            res.sendRedirect("TransaksiServlet");
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private void insertTransaksi(Transaksi t) throws SQLException {
        String sql = "INSERT INTO transaksi (id, user_id, jumlah, deskripsi, tanggal, kategori_id, jenis) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = JDBC.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, t.getId());
            stmt.setString(2, t.getUserId());
            stmt.setDouble(3, t.getJumlah());
            stmt.setString(4, t.getDeskripsi());
            stmt.setDate(5, new java.sql.Date(t.getTanggal().getTime()));
            stmt.setString(6, t.getKategoriId());
            stmt.setString(7, t.getJenis());
            stmt.executeUpdate();
        }
    }

    private void updateTransaksi(Transaksi t) throws SQLException {
        String sql = "UPDATE transaksi SET user_id=?, jumlah=?, deskripsi=?, tanggal=?, kategori_id=?, jenis=? WHERE id=?";
        try (Connection conn = JDBC.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, t.getUserId());
            stmt.setDouble(2, t.getJumlah());
            stmt.setString(3, t.getDeskripsi());
            stmt.setDate(4, new java.sql.Date(t.getTanggal().getTime()));
            stmt.setString(5, t.getKategoriId());
            stmt.setString(6, t.getJenis());
            stmt.setString(7, t.getId());
            stmt.executeUpdate();
        }
    }

    private void deleteTransaksi(String id) throws SQLException {
        String sql = "DELETE FROM transaksi WHERE id=?";
        try (Connection conn = JDBC.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            stmt.executeUpdate();
        }
    }

    private void deleteTagihan(String id) throws SQLException {
        String sql = "DELETE FROM tagihan WHERE id=?";
        try (Connection conn = JDBC.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            stmt.executeUpdate();
        }
    }

    private Transaksi getTransaksiById(String id) throws SQLException {
        String sql = "SELECT * FROM transaksi WHERE id=?";
        try (Connection conn = JDBC.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Transaksi(
                            rs.getString("id"),
                            rs.getString("user_id"),
                            rs.getDouble("jumlah"),
                            rs.getString("deskripsi"),
                            rs.getDate("tanggal"),
                            rs.getString("kategori_id"),
                            rs.getString("jenis"));
                }
            }
        }
        return null;
    }

    private List<Transaksi> getAllTransaksi() throws SQLException {
        List<Transaksi> list = new ArrayList<>();
        // JOINT QUERY to get category name
        String sql = "SELECT t.*, k.nama AS kategori_nama " +
                     "FROM transaksi t " +
                     "LEFT JOIN kategori k ON t.kategori_id = k.id";
        
        try (Connection conn = JDBC.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Transaksi t = new Transaksi(
                        rs.getString("id"),
                        rs.getString("user_id"),
                        rs.getDouble("jumlah"),
                        rs.getString("deskripsi"),
                        rs.getDate("tanggal"),
                        rs.getString("kategori_id"),
                        rs.getString("jenis"));
                // Set the name from the join result
                t.setKategoriNama(rs.getString("kategori_nama"));
                list.add(t);
            }
        }
        return list;
    }

    // Helpers for Dropdowns
    private List<User> getAllUsers() throws SQLException {
        List<User> list = new ArrayList<>();
        String sql = "SELECT * FROM user";
        try (Connection conn = JDBC.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new User(rs.getString("id"), rs.getString("nama"), rs.getString("email"),
                        rs.getString("password")));
            }
        }
        return list;
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
