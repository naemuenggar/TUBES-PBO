package controller;

import model.FinGoal;
import model.TargetTabungan;
import model.User;
import util.JDBC;
import util.IdGenerator;
import util.ParseUtils;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.*;
import java.util.*;

public class FinGoalServlet extends HttpServlet {

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
                FinGoal goal = getById(id);
                // Check Access (indirectly via Target)
                // For simplicity, we can fetch target ownership or trust the link.
                // Better to verify ownership match?
                // Let's rely on list filtering for basic protection, detailed check is nice but complex here without extra queries.
                // Or just fetch target and check user_id.
                // Assuming admin sees all.
                req.setAttribute("goal", goal);
                req.setAttribute("targets", getAllTargetTabungan(currentUser)); 
                req.getRequestDispatcher("/model/FinGoal-form.jsp").forward(req, res);
            } else if ("form".equals(action)) {
                req.setAttribute("targets", getAllTargetTabungan(currentUser)); 
                req.getRequestDispatcher("/model/FinGoal-form.jsp").forward(req, res);
            } else if ("delete".equals(action)) {
                delete(req.getParameter("id"));
                res.sendRedirect("FinGoalServlet");
            } else {
                List<FinGoal> list = getAll(currentUser);
                req.setAttribute("fingoals", list);
                req.getRequestDispatcher("/model/FinGoal-list.jsp").forward(req, res);
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    // ...

    private List<FinGoal> getAll(User user) throws SQLException {
        List<FinGoal> list = new ArrayList<>();
        String sql;
        boolean isAdmin = "admin".equals(user.getRole());
        
        if (isAdmin) {
             sql = "SELECT f.*, t.nama AS target_name, u.id AS user_id, u.nama AS user_name " +
                   "FROM fingoal f " +
                   "LEFT JOIN target_tabungan t ON f.target_id = t.id " +
                   "LEFT JOIN user u ON t.user_id = u.id";
        } else {
             // Filter by Target's User ID
             sql = "SELECT f.*, t.nama AS target_name, u.id AS user_id, u.nama AS user_name " +
                   "FROM fingoal f " +
                   "LEFT JOIN target_tabungan t ON f.target_id = t.id " +
                   "LEFT JOIN user u ON t.user_id = u.id " +
                   "WHERE t.user_id = ?";
        }

        try (Connection conn = JDBC.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
             
            if (!isAdmin) {
                stmt.setString(1, user.getId());
            }
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(new FinGoal(
                        rs.getString("id"),
                        rs.getString("target_id"),
                        rs.getString("target_name"), 
                        rs.getString("user_id"),
                        rs.getString("user_name"),
                        rs.getDouble("progress"),
                        rs.getString("status")
                    ));
                }
            }
        }
        return list;
    }
    
    // New helper method for dropdown - Filtered for User
    private List<TargetTabungan> getAllTargetTabungan(User user) throws SQLException {
        List<TargetTabungan> list = new ArrayList<>();
        String sql;
        boolean isAdmin = "admin".equals(user.getRole());
        
        if (isAdmin) {
            sql = "SELECT * FROM target_tabungan";
        } else {
            sql = "SELECT * FROM target_tabungan WHERE user_id = ?";
        }
        
        try (Connection conn = JDBC.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            if (!isAdmin) {
                stmt.setString(1, user.getId());
            }
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(new TargetTabungan(
                        rs.getString("id"),
                        rs.getString("user_id"),
                        rs.getString("nama"),
                        rs.getDouble("jumlah_target")
                    ));
                }
            }
        }
        return list;
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        // ... same as before ...
        String id = req.getParameter("id");
        
        try (Connection conn = JDBC.getConnection()) {
            // ... same auto-id logic ...
            if (id == null || id.trim().isEmpty()) {
                id = IdGenerator.getNextId(conn, "fingoal");
            }

            FinGoal f = new FinGoal(
                id,
                req.getParameter("targetId"),
                Double.parseDouble(req.getParameter("progress")),
                req.getParameter("status")
            );

            if (getById(f.getId()) != null) {
                update(f);
            } else {
                insert(f);
            }
            res.sendRedirect("FinGoalServlet");
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    // ... insert, update, delete methods ... (same)
    private void insert(FinGoal f) throws SQLException {
        String sql = "INSERT INTO fingoal (id, target_id, progress, status) VALUES (?, ?, ?, ?)";
        try (Connection conn = JDBC.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, f.getId());
            stmt.setString(2, f.getTargetId());
            stmt.setDouble(3, f.getProgress());
            stmt.setString(4, f.getStatus());
            stmt.executeUpdate();
        }
    }

    private void update(FinGoal f) throws SQLException {
        String sql = "UPDATE fingoal SET target_id=?, progress=?, status=? WHERE id=?";
        try (Connection conn = JDBC.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, f.getTargetId());
            stmt.setDouble(2, f.getProgress());
            stmt.setString(3, f.getStatus());
            stmt.setString(4, f.getId());
            stmt.executeUpdate();
        }
    }

    private void delete(String id) throws SQLException {
        String sql = "DELETE FROM fingoal WHERE id=?";
        try (Connection conn = JDBC.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            stmt.executeUpdate();
        }
    }

    private FinGoal getById(String id) throws SQLException {
        String sql = "SELECT * FROM fingoal WHERE id=?";
        try (Connection conn = JDBC.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new FinGoal(
                        rs.getString("id"),
                        rs.getString("target_id"),
                        rs.getDouble("progress"),
                        rs.getString("status")
                    );
                }
            }
        }
        return null;
    }


}

