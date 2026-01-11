package controller;

import model.Anggaran;
import model.User;
import util.JDBC;
import util.ParseUtils;
import util.IdGenerator; // Added import

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.*;
import java.util.*;


    // ... existing imports ...
    import util.IdGenerator;

    public class AnggaranServlet extends HttpServlet {

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
                Anggaran anggaran = getById(id);
                if (!"admin".equals(currentUser.getRole()) && !anggaran.getUserId().equals(currentUser.getId())) {
                    res.sendError(HttpServletResponse.SC_FORBIDDEN, "Akses ditolak.");
                    return;
                }
                req.setAttribute("anggaran", anggaran);
                req.setAttribute("users", getAllUsers());
                req.getRequestDispatcher("/model/Anggaran-form.jsp").forward(req, res);
            } else if ("form".equals(action)) {
                req.setAttribute("users", getAllUsers());
                req.getRequestDispatcher("/model/Anggaran-form.jsp").forward(req, res);
            } else if ("delete".equals(action)) {
                delete(req.getParameter("id"));
                res.sendRedirect("AnggaranServlet");
            } else {
                List<Anggaran> list = getAll(currentUser);
                req.setAttribute("anggarans", list);
                req.getRequestDispatcher("/model/Anggaran-list.jsp").forward(req, res);
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

        String action = req.getParameter("action");
        if ("addFunds".equals(action) || "withdrawFunds".equals(action)) {
            handleFundTransfer(req, res, currentUser, action);
            return;
        }

        String id = req.getParameter("id");
        if (id == null || id.trim().isEmpty()) {
            try (Connection conn = JDBC.getConnection()) {
                id = IdGenerator.getNextId(conn, "anggaran");
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

        Anggaran a = new Anggaran(
            id,
            userIdToUse,
            req.getParameter("nama"),
            Double.parseDouble(req.getParameter("jumlah"))
        );

        try {
            Anggaran existing = getById(a.getId());
            if (existing != null) {
                // UPDATE RBAC Check
                if (!"admin".equals(currentUser.getRole()) && !existing.getUserId().equals(currentUser.getId())) {
                    res.sendError(HttpServletResponse.SC_FORBIDDEN, "Anda tidak memiliki akses untuk mengedit anggaran ini.");
                    return;
                }
                update(a);
            } else {
                insert(a);
            }
            res.sendRedirect("AnggaranServlet");
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private void handleFundTransfer(HttpServletRequest req, HttpServletResponse res, User currentUser, String action) throws ServletException, IOException {
        String id = req.getParameter("id");
        double amount = Double.parseDouble(req.getParameter("amount"));
        
        try {
            Anggaran anggaran = getById(id);
            if (anggaran == null) {
                res.sendError(HttpServletResponse.SC_NOT_FOUND, "Anggaran tidak ditemukan.");
                return;
            }

            // Verify ownership
            if (!"admin".equals(currentUser.getRole()) && !anggaran.getUserId().equals(currentUser.getId())) {
                res.sendError(HttpServletResponse.SC_FORBIDDEN, "Akses ditolak.");
                return;
            }

            boolean isAdd = "addFunds".equals(action);
            
            // Transaction Type Logic:
            // Add Funds to Budget -> Money leaves Pocket (Expense) -> Budget Increases
            // Withdraw Funds from Budget -> Money enters Pocket (Income) -> Budget Decreases
            String transType = isAdd ? "pengeluaran" : "pemasukan";
            String catId = getFirstCategoryId(transType);
            
            if (catId == null) {
                 // Fallback if no category exists, maybe create one or error?
                 // For now, let's assume at least one exists from seed data.
                 throw new ServletException("Tidak ada kategori " + transType + " ditemukan. Harap buat kategori terlebih dahulu.");
            }

            model.Transaksi t = new model.Transaksi();
            try (Connection conn = JDBC.getConnection()) {
                t.setId(IdGenerator.getNextId(conn, "transaksi"));
            }
            t.setUserId(anggaran.getUserId()); // Transaction belongs to the budget owner
            t.setJumlah(amount);
            t.setDeskripsi((isAdd ? "Tambah dana ke " : "Tarik dana dari ") + anggaran.getNama());
            t.setTanggal(new java.util.Date());
            t.setKategoriId(catId);
            t.setJenis(transType);

            // Update Budget Amount
            double newAmount = isAdd ? (anggaran.getJumlah() + amount) : (anggaran.getJumlah() - amount);
            anggaran.setJumlah(newAmount);

            // Execute Transaction and Update Budget Atomically (ideally)
            processTransfer(anggaran, t);

            res.sendRedirect("AnggaranServlet");

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private void processTransfer(Anggaran a, model.Transaksi t) throws SQLException {
        Connection conn = null;
        try {
            conn = JDBC.getConnection();
            conn.setAutoCommit(false);

            // 1. Update Anggaran
            String updateSql = "UPDATE anggaran SET jumlah=? WHERE id=?";
            try (PreparedStatement stmt = conn.prepareStatement(updateSql)) {
                stmt.setDouble(1, a.getJumlah());
                stmt.setString(2, a.getId());
                stmt.executeUpdate();
            }

            // 2. Insert Transaksi
            String insertSql = "INSERT INTO transaksi (id, user_id, jumlah, deskripsi, tanggal, kategori_id, jenis) VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(insertSql)) {
                stmt.setString(1, t.getId());
                stmt.setString(2, t.getUserId());
                stmt.setDouble(3, t.getJumlah());
                stmt.setString(4, t.getDeskripsi());
                stmt.setDate(5, new java.sql.Date(t.getTanggal().getTime()));
                stmt.setString(6, t.getKategoriId());
                stmt.setString(7, t.getJenis());
                stmt.executeUpdate();
            }

            conn.commit();
        } catch (SQLException e) {
            if (conn != null) conn.rollback();
            throw e;
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }

    private String getFirstCategoryId(String type) throws SQLException {
        String sql = "SELECT id FROM kategori WHERE tipe=? LIMIT 1";
        try (Connection conn = JDBC.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, type);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("id");
                }
            }
        }
        return null;
    }

    private List<Anggaran> getAll(User user) throws SQLException {
        List<Anggaran> list = new ArrayList<>();
        String sql;
        boolean isAdmin = "admin".equals(user.getRole());

        if (isAdmin) {
            sql = "SELECT * FROM anggaran";
        } else {
            sql = "SELECT * FROM anggaran WHERE user_id = ?";
        }

        try (Connection conn = JDBC.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            if (!isAdmin) {
                stmt.setString(1, user.getId());
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(new Anggaran(
                        rs.getString("id"),
                        rs.getString("user_id"),
                        rs.getString("nama"),
                        rs.getDouble("jumlah")
                    ));
                }
            }
        }
        return list;
    }

    private void insert(Anggaran a) throws SQLException {
        String sql = "INSERT INTO anggaran (id, user_id, nama, jumlah) VALUES (?, ?, ?, ?)";
        try (Connection conn = JDBC.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, a.getId());
            stmt.setString(2, a.getUserId());
            stmt.setString(3, a.getNama());
            stmt.setDouble(4, a.getJumlah());
            stmt.executeUpdate();
        }
    }

    private void update(Anggaran a) throws SQLException {
        String sql = "UPDATE anggaran SET user_id=?, nama=?, jumlah=? WHERE id=?";
        try (Connection conn = JDBC.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, a.getUserId());
            stmt.setString(2, a.getNama());
            stmt.setDouble(3, a.getJumlah());
            stmt.setString(4, a.getId());
            stmt.executeUpdate();
        }
    }

    private void delete(String id) throws SQLException {
        String sql = "DELETE FROM anggaran WHERE id=?";
        try (Connection conn = JDBC.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            stmt.executeUpdate();
        }
    }

    private Anggaran getById(String id) throws SQLException {
        String sql = "SELECT * FROM anggaran WHERE id=?";
        try (Connection conn = JDBC.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Anggaran(
                        rs.getString("id"),
                        rs.getString("user_id"),
                        rs.getString("nama"),
                        rs.getDouble("jumlah")
                    );
                }
            }
        }
        return null;
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
}

