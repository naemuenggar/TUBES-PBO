
package controller;

import model.User;
import util.JDBC;
import util.PasswordUtil;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.*;
// import java.util.UUID; // Removed
import util.IdGenerator;

public class RegisterServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        // If already logged in, redirect to dashboard
        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            res.sendRedirect("index.jsp");
            return;
        }

        // Show registration page
        req.getRequestDispatcher("/register.jsp").forward(req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String nama = req.getParameter("nama");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String confirmPassword = req.getParameter("confirmPassword");

        // Validation
        if (nama == null || nama.trim().isEmpty()) {
            req.setAttribute("error", "Nama tidak boleh kosong!");
            req.getRequestDispatcher("/register.jsp").forward(req, res);
            return;
        }

        if (email == null || email.trim().isEmpty()) {
            req.setAttribute("error", "Email tidak boleh kosong!");
            req.getRequestDispatcher("/register.jsp").forward(req, res);
            return;
        }

        if (password == null || password.length() < 6) {
            req.setAttribute("error", "Password minimal 6 karakter!");
            req.getRequestDispatcher("/register.jsp").forward(req, res);
            return;
        }

        if (!password.equals(confirmPassword)) {
            req.setAttribute("error", "Password dan konfirmasi password tidak cocok!");
            req.getRequestDispatcher("/register.jsp").forward(req, res);
            return;
        }

        try {
            // Check if email already exists
            if (emailExists(email)) {
                req.setAttribute("error", "Email sudah terdaftar! Silakan gunakan email lain.");
                req.getRequestDispatcher("/register.jsp").forward(req, res);
                return;
            }

            // Generate unique ID (Username from email)
            String userId = IdGenerator.generateFromEmail(email);

            // Hash password before storing
            String hashedPassword = PasswordUtil.hashPassword(password);

            // Create new user with 'user' role (not admin)
            User newUser = new User(userId, nama, email, hashedPassword, "user");
            insertUser(newUser);

            // Auto-login after successful registration
            HttpSession session = req.getSession(true);
            session.setAttribute("user", newUser);
            session.setAttribute("userId", newUser.getId());
            session.setAttribute("userRole", newUser.getRole());
            session.setMaxInactiveInterval(30 * 60); // 30 minutes

            // Redirect to dashboard
            res.sendRedirect("index.jsp");

        } catch (SQLException e) {
            e.printStackTrace();
            req.setAttribute("error", "Terjadi kesalahan sistem. Silakan coba lagi.");
            req.getRequestDispatcher("/register.jsp").forward(req, res);
        }
    }

    private boolean emailExists(String email) throws SQLException {
        String sql = "SELECT COUNT(*) as count FROM user WHERE email = ?";

        try (Connection conn = JDBC.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("count") > 0;
                }
            }
        }

        return false;
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
}
