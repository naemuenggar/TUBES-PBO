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

    // ...

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

