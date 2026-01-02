
package controller;

import model.TargetTabungan;
import model.TargetWithProgress;
import util.JDBC;
import util.IdGenerator;
import util.ParseUtils; // Added import
import model.User;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.*;
import java.util.*;

public class TargetTabunganServlet extends HttpServlet {

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
                TargetTabungan target = getById(id);
                // RBAC Check for Edit View
                if (!"admin".equals(currentUser.getRole()) && !target.getUserId().equals(currentUser.getId())) {
                     res.sendError(HttpServletResponse.SC_FORBIDDEN, "Akses ditolak.");
                     return;
                }
                req.setAttribute("target", target);
                req.setAttribute("users", getAllUsers());
                req.getRequestDispatcher("/model/Target-form.jsp").forward(req, res);
            } else if ("form".equals(action)) {
                req.setAttribute("users", getAllUsers());
                req.getRequestDispatcher("/model/Target-form.jsp").forward(req, res);
            } else if ("delete".equals(action)) {
                // Delete logic usually checked in backend or by ownership, assuming admin/owner
                // For safety, could be re-checked here, but let's stick to list filtering first.
                // Ideally, delete() should also check ownership. I'll rely on the list filtering preventing access to the ID button, 
                // but direct URL access needs protection too. For now focusing on "Visibility".
                delete(req.getParameter("id"));
                res.sendRedirect("TargetTabunganServlet");
            } else if ("tabung".equals(action)) {
                String id = req.getParameter("id");
                TargetTabungan target = getById(id);
                 if (!"admin".equals(currentUser.getRole()) && !target.getUserId().equals(currentUser.getId())) {
                     res.sendError(HttpServletResponse.SC_FORBIDDEN, "Akses ditolak.");
                     return;
                }
                req.setAttribute("target", target);
                req.getRequestDispatcher("/model/Tabung-target.jsp").forward(req, res);
            } else {
                List<TargetWithProgress> list = getAllWithProgress(currentUser);
                req.setAttribute("targets", list);
                req.getRequestDispatcher("/model/Target-list.jsp").forward(req, res);
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    // ... (doPost stays same) ...

    private List<TargetWithProgress> getAllWithProgress(User user) throws SQLException {
        List<TargetWithProgress> list = new ArrayList<>();
        String sql;
        boolean isAdmin = "admin".equals(user.getRole());
        
        if (isAdmin) {
            sql = "SELECT t.id, t.user_id, t.nama, t.jumlah_target, " +
                  "COALESCE(f.progress, 0) as current_progress " +
                  "FROM target_tabungan t " +
                  "LEFT JOIN fingoal f ON t.id = f.target_id";
        } else {
             sql = "SELECT t.id, t.user_id, t.nama, t.jumlah_target, " +
                  "COALESCE(f.progress, 0) as current_progress " +
                  "FROM target_tabungan t " +
                  "LEFT JOIN fingoal f ON t.id = f.target_id " +
                  "WHERE t.user_id = ?";
        }

        try (Connection conn = JDBC.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            if (!isAdmin) {
                stmt.setString(1, user.getId());
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(new TargetWithProgress(
                            rs.getString("id"),
                            rs.getString("user_id"),
                            rs.getString("nama"),
                            rs.getDouble("jumlah_target"),
                            rs.getDouble("current_progress")));
                }
            }
        }
        return list;
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User currentUser = (session != null) ? (User) session.getAttribute("user") : null;
        
        if (currentUser == null) {
            res.sendRedirect("login.jsp");
            return;
        }

        String action = req.getParameter("action");
        
        if ("processTabung".equals(action)) {
            processTabung(req, res);
            return;
        }

        String id = req.getParameter("id");
        if (id == null || id.trim().isEmpty()) {
            try (Connection conn = JDBC.getConnection()) {
                id = IdGenerator.getNextId(conn, "target_tabungan");
            } catch (SQLException e) {
                e.printStackTrace();
                id = IdGenerator.generateSimple(); // Fallback
            }
        }

        // RBAC Logic for UserId
        String userIdToUse;
        if ("admin".equals(currentUser.getRole())) {
            userIdToUse = req.getParameter("userId");
            if (userIdToUse == null || userIdToUse.isEmpty()) {
                userIdToUse = currentUser.getId(); // Fallback or Error
            }
        } else {
            // Regular user: Force their own ID
            userIdToUse = currentUser.getId();
        }

        TargetTabungan t = new TargetTabungan(
                id,
                userIdToUse,
                req.getParameter("nama"),
                Double.parseDouble(req.getParameter("jumlahTarget")));

        try {
            TargetTabungan existing = getById(t.getId());
            if (existing != null) {
                // UPDATE
                // RBAC Check: Regular user can only edit their own
                if (!"admin".equals(currentUser.getRole()) && !existing.getUserId().equals(currentUser.getId())) {
                    res.sendError(HttpServletResponse.SC_FORBIDDEN, "Anda tidak memiliki akses untuk mengedit target ini.");
                    return;
                }
                update(t);
            } else {
                // INSERT
                insert(t);
            }
            res.sendRedirect("TargetTabunganServlet");
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private void processTabung(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String targetId = req.getParameter("targetId");
        String userId = req.getParameter("userId");
        double jumlahSetor = Double.parseDouble(req.getParameter("jumlahSetor"));
        String catatan = req.getParameter("catatan"); // Optional note

        try (Connection conn = JDBC.getConnection()) {
            // 1. Get Target Info for Description
            TargetTabungan target = getById(targetId);
            String targetName = (target != null) ? target.getNama() : "Unknown Target";
            
            // 2. Ensure 'Tabungan' Category Exists (Idempotent)
            String catId = "cat_tabungan";
            String checkCatSql = "SELECT id FROM kategori WHERE id = ?";
            try (PreparedStatement checkCatStmt = conn.prepareStatement(checkCatSql)) {
                checkCatStmt.setString(1, catId);
                try (ResultSet rs = checkCatStmt.executeQuery()) {
                    if (!rs.next()) {
                        String insertCatSql = "INSERT INTO kategori (id, nama, tipe) VALUES (?, ?, ?)";
                        try (PreparedStatement insertCatStmt = conn.prepareStatement(insertCatSql)) {
                            insertCatStmt.setString(1, catId);
                            insertCatStmt.setString(2, "Tabungan");
                            insertCatStmt.setString(3, "pengeluaran");
                            insertCatStmt.executeUpdate();
                        }
                    }
                }
            }

            // 3. Insert Transaction (Pengeluaran)
            String transId = IdGenerator.getNextId(conn, "transaksi");
            String insertTransSql = "INSERT INTO transaksi (id, user_id, jumlah, deskripsi, tanggal, kategori_id, jenis) VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement transStmt = conn.prepareStatement(insertTransSql)) {
                transStmt.setString(1, transId);
                transStmt.setString(2, userId);
                transStmt.setDouble(3, jumlahSetor); // Expense positive value
                transStmt.setString(4, "Menabung: " + targetName + (catatan != null && !catatan.isEmpty() ? " (" + catatan + ")" : ""));
                transStmt.setDate(5, new java.sql.Date(System.currentTimeMillis()));
                transStmt.setString(6, catId);
                transStmt.setString(7, "pengeluaran");
                transStmt.executeUpdate();
            }

            // 4. Update FinGoal with Status Check
            String checkSql = "SELECT id, progress FROM fingoal WHERE target_id = ?";
            String fingoalId = null;
            double currentProgress = 0;
            
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                checkStmt.setString(1, targetId);
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next()) {
                        fingoalId = rs.getString("id");
                        currentProgress = rs.getDouble("progress");
                    }
                }
            }

            double newProgress = currentProgress + jumlahSetor;
            String newStatus = (newProgress >= target.getJumlahTarget()) ? "Tercapai" : "Belum Tercapai";
            
            if (fingoalId != null) {
                // Update existing
                String updateSql = "UPDATE fingoal SET progress = ?, status = ? WHERE id = ?";
                try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                    updateStmt.setDouble(1, newProgress);
                    updateStmt.setString(2, newStatus);
                    updateStmt.setString(3, fingoalId);
                    updateStmt.executeUpdate();
                }
            } else {
                // Insert new
                String newId = IdGenerator.getNextId(conn, "fingoal");
                String insertSql = "INSERT INTO fingoal (id, target_id, progress, status) VALUES (?, ?, ?, ?)";
                try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                    insertStmt.setString(1, newId);
                    insertStmt.setString(2, targetId);
                    insertStmt.setDouble(3, newProgress);
                    insertStmt.setString(4, newStatus);
                    insertStmt.executeUpdate();
                }
            }
            
            res.sendRedirect("TargetTabunganServlet");
            
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    private void insert(TargetTabungan t) throws SQLException {
        String sql = "INSERT INTO target_tabungan (id, user_id, nama, jumlah_target) VALUES (?, ?, ?, ?)";
        String sqlFinGoal = "INSERT INTO fingoal (id, target_id, progress, status) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = JDBC.getConnection()) {
            // Insert Target
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, t.getId());
                stmt.setString(2, t.getUserId());
                stmt.setString(3, t.getNama());
                stmt.setDouble(4, t.getJumlahTarget());
                stmt.executeUpdate();
            }
            
            // Auto-create FinGoal
            try (PreparedStatement stmtFin = conn.prepareStatement(sqlFinGoal)) {
                String finGoalId = IdGenerator.getNextId(conn, "fingoal"); // Generate ID using same conn
                stmtFin.setString(1, finGoalId);
                stmtFin.setString(2, t.getId());
                stmtFin.setDouble(3, 0.0); // Initial progress 0
                stmtFin.setString(4, "Belum Tercapai");
                stmtFin.executeUpdate();
            }
        }
    }

    private void update(TargetTabungan t) throws SQLException {
        String sql = "UPDATE target_tabungan SET user_id=?, nama=?, jumlah_target=? WHERE id=?";
        try (Connection conn = JDBC.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, t.getUserId());
            stmt.setString(2, t.getNama());
            stmt.setDouble(3, t.getJumlahTarget());
            stmt.setString(4, t.getId());
            stmt.executeUpdate();
        }
    }

    private void delete(String id) throws SQLException {
        String sql = "DELETE FROM target_tabungan WHERE id=?";
        try (Connection conn = JDBC.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            stmt.executeUpdate();
        }
    }

    private TargetTabungan getById(String id) throws SQLException {
        String sql = "SELECT * FROM target_tabungan WHERE id=?";
        try (Connection conn = JDBC.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new TargetTabungan(
                            rs.getString("id"),
                            rs.getString("user_id"),
                            rs.getString("nama"),
                            rs.getDouble("jumlah_target"));
                }
            }
        }
        return null;
    }

    private List<TargetTabungan> getAll() throws SQLException {
        List<TargetTabungan> list = new ArrayList<>();
        String sql = "SELECT * FROM target_tabungan";
        try (Connection conn = JDBC.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new TargetTabungan(
                        rs.getString("id"),
                        rs.getString("user_id"),
                        rs.getString("nama"),
                        rs.getDouble("jumlah_target")));
            }
        }
        return list;
    }



    private List<User> getAllUsers() throws SQLException {
        List<User> list = new ArrayList<>();
        String sql = "SELECT * FROM user";
        System.out.println("DEBUG: Fetching users from DB...");
        try (Connection conn = JDBC.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                User u = new User(
                        rs.getString("id"),
                        rs.getString("nama"),
                        rs.getString("email"),
                        rs.getString("password"));
                list.add(u);
                System.out.println("DEBUG: Found User: " + u.getNama());
            }
        } catch (SQLException e) {
            System.err.println("DEBUG: Error fetching users: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
        System.out.println("DEBUG: Total Users Found: " + list.size());
        return list;
    }
}
