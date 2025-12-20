
package controller;

import model.User;
import util.JDBC;
import util.PasswordUtil;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserServlet extends HttpServlet {

    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String action = req.getParameter("action");
        try {
            if ("edit".equals(action)) {
                String id = req.getParameter("id");
                User user = getUserById(id);
                req.setAttribute("user", user);
                req.getRequestDispatcher("/model/user-form.jsp").forward(req, res);
            } else if ("delete".equals(action)) {
                deleteUser(req.getParameter("id"));
                res.sendRedirect("UserServlet");
            } else {
                List<User> list = getAllUsers();
                req.setAttribute("users", list);
                req.getRequestDispatcher("/model/user-list.jsp").forward(req, res);
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String id = req.getParameter("id");
        String nama = req.getParameter("nama");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String role = req.getParameter("role") != null ? req.getParameter("role") : "user";

        try {
            User existingUser = getUserById(id);

            if (existingUser != null) {
                // Update existing user - only hash if password changed
                String finalPassword;
                if (password != null && !password.isEmpty() && !password.equals(existingUser.getPassword())) {
                    finalPassword = PasswordUtil.hashPassword(password);
                } else {
                    finalPassword = existingUser.getPassword();
                }
                User user = new User(id, nama, email, finalPassword, role);
                updateUser(user);
            } else {
                // New user - hash password
                String hashedPassword = PasswordUtil.hashPassword(password);
                User user = new User(id, nama, email, hashedPassword, role);
                insertUser(user);
            }
            res.sendRedirect("UserServlet");
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private void insertUser(User user) throws SQLException {
        String sql = "INSERT INTO user (id, nama, email, password, role) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = JDBC.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getId());
            stmt.setString(2, user.getNama());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getPassword());
            stmt.setString(5, user.getRole());
            stmt.executeUpdate();
        }
    }

    private void updateUser(User user) throws SQLException {
        String sql = "UPDATE user SET nama=?, email=?, password=?, role=? WHERE id=?";
        try (Connection conn = JDBC.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getNama());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPassword());
            stmt.setString(4, user.getRole());
            stmt.setString(5, user.getId());
            stmt.executeUpdate();
        }
    }

    private void deleteUser(String id) throws SQLException {
        String sql = "DELETE FROM user WHERE id=?";
        try (Connection conn = JDBC.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            stmt.executeUpdate();
        }
    }

    private User getUserById(String id) throws SQLException {
        String sql = "SELECT * FROM user WHERE id=?";
        try (Connection conn = JDBC.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new User(
                            rs.getString("id"),
                            rs.getString("nama"),
                            rs.getString("email"),
                            rs.getString("password"),
                            rs.getString("role"));
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
                        rs.getString("password"),
                        rs.getString("role")));
            }
        }
        return list;
    }
}
