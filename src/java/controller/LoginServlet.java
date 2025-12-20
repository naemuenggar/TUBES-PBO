
package controller;

import model.User;
import util.JDBC;
import util.PasswordUtil;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.*;

public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        // If already logged in, redirect to dashboard
        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            res.sendRedirect("index.jsp");
            return;
        }

        // Show login page
        req.getRequestDispatcher("/login.jsp").forward(req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        try {
            User user = authenticateUser(email, password);

            if (user != null) {
                // Login successful - create session
                HttpSession session = req.getSession(true);
                session.setAttribute("user", user);
                session.setAttribute("userId", user.getId());
                session.setAttribute("userRole", user.getRole());
                session.setMaxInactiveInterval(30 * 60); // 30 minutes timeout

                // Redirect to dashboard
                res.sendRedirect("index.jsp");
            } else {
                // Login failed - show error
                req.setAttribute("error", "Email atau password salah!");
                req.getRequestDispatcher("/login.jsp").forward(req, res);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            req.setAttribute("error", "Terjadi kesalahan sistem. Silakan coba lagi.");
            req.getRequestDispatcher("/login.jsp").forward(req, res);
        }
    }

    private User authenticateUser(String email, String password) throws SQLException {
        String sql = "SELECT * FROM user WHERE email = ?";

        try (Connection conn = JDBC.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String storedPassword = rs.getString("password");

                    // Verify password with hash
                    if (PasswordUtil.verifyPassword(password, storedPassword)) {
                        return new User(
                                rs.getString("id"),
                                rs.getString("nama"),
                                rs.getString("email"),
                                storedPassword,
                                rs.getString("role"));
                    }
                }
            }
        }

        return null; // Authentication failed
    }
}
