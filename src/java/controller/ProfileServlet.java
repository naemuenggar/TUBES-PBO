package controller;

import model.User;
import util.JDBC;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.*;

public class ProfileServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        // Get current user from session
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            res.sendRedirect("login.jsp");
            return;
        }

        // Fetch fresh user data from database
        try {
            User freshUser = getUserById(user.getId());
            if (freshUser != null) {
                req.setAttribute("profileUser", freshUser);
            } else {
                req.setAttribute("profileUser", user);
            }
        } catch (SQLException e) {
            req.setAttribute("profileUser", user);
        }

        req.getRequestDispatcher("/profile.jsp").forward(req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            res.sendRedirect("login.jsp");
            return;
        }

        String currentPassword = req.getParameter("currentPassword");
        String newPassword = req.getParameter("newPassword");
        String confirmPassword = req.getParameter("confirmPassword");

        try {
            // Get fresh user data from database
            User freshUser = getUserById(user.getId());

            if (freshUser == null) {
                req.setAttribute("error", "User tidak ditemukan!");
                req.setAttribute("profileUser", user);
                req.getRequestDispatcher("/profile.jsp").forward(req, res);
                return;
            }

            // Validate current password
            if (!freshUser.getPassword().equals(currentPassword)) {
                req.setAttribute("error", "Password saat ini salah!");
                req.setAttribute("profileUser", freshUser);
                req.getRequestDispatcher("/profile.jsp").forward(req, res);
                return;
            }

            // Validate new password not empty
            if (newPassword == null || newPassword.trim().isEmpty()) {
                req.setAttribute("error", "Password baru tidak boleh kosong!");
                req.setAttribute("profileUser", freshUser);
                req.getRequestDispatcher("/profile.jsp").forward(req, res);
                return;
            }

            // Validate confirmation matches
            if (!newPassword.equals(confirmPassword)) {
                req.setAttribute("error", "Konfirmasi password tidak cocok!");
                req.setAttribute("profileUser", freshUser);
                req.getRequestDispatcher("/profile.jsp").forward(req, res);
                return;
            }

            // Update password in database
            updatePassword(user.getId(), newPassword);

            // Update session user
            freshUser.setPassword(newPassword);
            session.setAttribute("user", freshUser);

            req.setAttribute("success", "Password berhasil diubah!");
            req.setAttribute("profileUser", freshUser);
            req.getRequestDispatcher("/profile.jsp").forward(req, res);

        } catch (SQLException e) {
            req.setAttribute("error", "Terjadi kesalahan: " + e.getMessage());
            req.setAttribute("profileUser", user);
            req.getRequestDispatcher("/profile.jsp").forward(req, res);
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

    private void updatePassword(String userId, String newPassword) throws SQLException {
        String sql = "UPDATE user SET password=? WHERE id=?";
        try (Connection conn = JDBC.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newPassword);
            stmt.setString(2, userId);
            stmt.executeUpdate();
        }
    }
}
