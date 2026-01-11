
package controller;

import model.Transaksi;
import model.User;

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
import util.DataHelper;

public class TransaksiServlet extends HttpServlet {

    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String action = req.getParameter("action");
        HttpSession session = req.getSession(false);
        User currentUser = (session != null) ? (User) session.getAttribute("user") : null;

        if (currentUser == null) {
            res.sendRedirect("login.jsp");
            return;
        }

        try {
            if ("edit".equals(action)) {
                String id = req.getParameter("id");
                Transaksi transaksi = getTransaksiById(id);
                // RBAC Check
                if (!"admin".equals(currentUser.getRole()) && !transaksi.getUserId().equals(currentUser.getId())) {
                    res.sendError(HttpServletResponse.SC_FORBIDDEN, "Akses ditolak.");
                    return;
                }
                req.setAttribute("transaksi", transaksi);
                req.setAttribute("kategoris", DataHelper.getAllKategori());
                req.setAttribute("users", DataHelper.getAllUsers());
                req.getRequestDispatcher("/model/Transaksi-form.jsp").forward(req, res);
            } else if ("form".equals(action)) {
                req.setAttribute("kategoris", DataHelper.getAllKategori());
                req.setAttribute("users", DataHelper.getAllUsers());
                req.getRequestDispatcher("/model/Transaksi-form.jsp").forward(req, res);
            } else if ("payBill".equals(action)) {
                String nama = req.getParameter("nama");
                Double jumlah = Double.parseDouble(req.getParameter("jumlah"));
                String userId = req.getParameter("userId");
                String tagihanId = req.getParameter("id");

                Transaksi t = new Transaksi();
                t.setDeskripsi("Bayar Tagihan: " + nama);
                t.setJumlah(jumlah);
                t.setUserId(userId);
                t.setTanggal(new java.util.Date());
                t.setJenis("pengeluaran");

                req.setAttribute("transaksi", t);
                req.setAttribute("users", DataHelper.getAllUsers());
                req.setAttribute("kategoris", DataHelper.getAllKategori());
                req.setAttribute("sourceTagihanId", tagihanId);

                req.getRequestDispatcher("/model/Transaksi-form.jsp").forward(req, res);
            } else if ("delete".equals(action)) {
                deleteTransaksi(req.getParameter("id"));
                res.sendRedirect("TransaksiServlet");
            } else {
                List<Transaksi> list = getAllTransaksi(currentUser);
                req.setAttribute("transaksis", list);
                req.getRequestDispatcher("/model/Transaksi-list.jsp").forward(req, res);
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User currentUser = (session != null) ? (User) session.getAttribute("user") : null;

        if (currentUser == null) {
            res.sendRedirect("login.jsp");
            return;
        }

        String id = req.getParameter("id");
        if (id == null || id.trim().isEmpty()) {
            try (Connection conn = JDBC.getConnection()) {
                id = IdGenerator.getNextId(conn, "transaksi");
            } catch (SQLException e) {
                e.printStackTrace();
                id = IdGenerator.generateSimple(); // Fallback
            }
        }

        // RBAC Logic
        String userIdToUse;
        if ("admin".equals(currentUser.getRole())) {
            userIdToUse = req.getParameter("userId");
            if (userIdToUse == null || userIdToUse.isEmpty()) {
                userIdToUse = currentUser.getId();
            }
        } else {
            userIdToUse = currentUser.getId();
        }

        Transaksi t = new Transaksi(
                id,
                userIdToUse,
                Double.parseDouble(req.getParameter("jumlah")),
                req.getParameter("deskripsi"),
                Date.valueOf(req.getParameter("tanggal")),
                req.getParameter("kategoriId"),
                req.getParameter("jenis"));

        try {
            Transaksi existing = getTransaksiById(t.getId());
            if (existing != null) {
                // UPDATE RBAC Check
                if (!"admin".equals(currentUser.getRole()) && !existing.getUserId().equals(currentUser.getId())) {
                    res.sendError(HttpServletResponse.SC_FORBIDDEN,
                            "Anda tidak memiliki akses untuk mengedit transaksi ini.");
                    return;
                }
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

    private List<Transaksi> getAllTransaksi(User user) throws SQLException {
        List<Transaksi> list = new ArrayList<>();
        String sql;
        boolean isAdmin = "admin".equals(user.getRole());

        if (isAdmin) {
            // JOINT QUERY to get category name
            sql = "SELECT t.*, k.nama AS kategori_nama " +
                    "FROM transaksi t " +
                    "LEFT JOIN kategori k ON t.kategori_id = k.id";
        } else {
            sql = "SELECT t.*, k.nama AS kategori_nama " +
                    "FROM transaksi t " +
                    "LEFT JOIN kategori k ON t.kategori_id = k.id " +
                    "WHERE t.user_id = ?";
        }

        try (Connection conn = JDBC.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            if (!isAdmin) {
                stmt.setString(1, user.getId());
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Transaksi t = new Transaksi(
                            rs.getString("id"),
                            rs.getString("user_id"),
                            rs.getDouble("jumlah"),
                            rs.getString("deskripsi"),
                            rs.getDate("tanggal"),
                            rs.getString("kategori_id"),
                            rs.getString("jenis"));
                    t.setKategoriNama(rs.getString("kategori_nama"));
                    list.add(t);
                }
            }
        }
        return list;
    }

    // Helpers for Dropdowns

}
