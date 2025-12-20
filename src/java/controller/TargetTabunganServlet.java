
package controller;

import model.TargetTabungan;
import util.JDBC;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.*;
import java.util.*;
import util.IdGenerator;

import model.User;

public class TargetTabunganServlet extends HttpServlet {

    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String action = req.getParameter("action");
        try {
            if ("edit".equals(action)) {
                String id = req.getParameter("id");
                TargetTabungan target = getById(id);
                req.setAttribute("target", target);
                req.setAttribute("users", getAllUsers());
                req.getRequestDispatcher("/model/Target-form.jsp").forward(req, res);
            } else if ("form".equals(action)) {
                req.setAttribute("users", getAllUsers());
                req.getRequestDispatcher("/model/Target-form.jsp").forward(req, res);
            } else if ("delete".equals(action)) {
                delete(req.getParameter("id"));
                res.sendRedirect("TargetTabunganServlet");
            } else {
                List<TargetTabungan> list = getAll();
                req.setAttribute("targets", list);
                req.getRequestDispatcher("/model/Target-list.jsp").forward(req, res);
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String id = req.getParameter("id");
        if (id == null || id.trim().isEmpty()) {
            id = IdGenerator.generateSimple();
        }

        TargetTabungan t = new TargetTabungan(
                id,
                req.getParameter("userId"),
                req.getParameter("nama"),
                Double.parseDouble(req.getParameter("jumlahTarget")));

        try {
            if (getById(t.getId()) != null) {
                update(t);
            } else {
                insert(t);
            }
            res.sendRedirect("TargetTabunganServlet");
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private void insert(TargetTabungan t) throws SQLException {
        String sql = "INSERT INTO target_tabungan (id, user_id, nama, jumlah_target) VALUES (?, ?, ?, ?)";
        try (Connection conn = JDBC.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, t.getId());
            stmt.setString(2, t.getUserId());
            stmt.setString(3, t.getNama());
            stmt.setDouble(4, t.getJumlahTarget());
            stmt.executeUpdate();
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
