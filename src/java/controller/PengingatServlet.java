
package controller;

import model.Pengingat;
import util.JDBC;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.*;
import java.util.*;

import model.User;

public class PengingatServlet extends HttpServlet {

    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String action = req.getParameter("action");
        try {
            if ("edit".equals(action)) {
                String id = req.getParameter("id");
                Pengingat pengingat = getById(id);
                req.setAttribute("pengingat", pengingat);
                req.setAttribute("users", getAllUsers());
                req.getRequestDispatcher("/model/Pengingat-form.jsp").forward(req, res);
            } else if ("form".equals(action)) {
                req.setAttribute("users", getAllUsers());
                req.getRequestDispatcher("/model/Pengingat-form.jsp").forward(req, res);
            } else if ("delete".equals(action)) {
                delete(req.getParameter("id"));
                res.sendRedirect("PengingatServlet");
            } else {
                List<Pengingat> list = getAll();
                req.setAttribute("pengingatList", list);
                req.getRequestDispatcher("/model/Pengingat-list.jsp").forward(req, res);
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        Pengingat p = new Pengingat(
                req.getParameter("id"),
                req.getParameter("userId"),
                req.getParameter("pesan"),
                java.sql.Date.valueOf(req.getParameter("tanggal")));

        try {
            if (getById(p.getId()) != null) {
                update(p);
            } else {
                insert(p);
            }
            res.sendRedirect("PengingatServlet");
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private void insert(Pengingat p) throws SQLException {
        String sql = "INSERT INTO pengingat (id, user_id, pesan, tanggal) VALUES (?, ?, ?, ?)";
        try (Connection conn = JDBC.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, p.getId());
            stmt.setString(2, p.getUserId());
            stmt.setString(3, p.getPesan());
            stmt.setDate(4, (java.sql.Date) p.getTanggal());
            stmt.executeUpdate();
        }
    }

    private void update(Pengingat p) throws SQLException {
        String sql = "UPDATE pengingat SET user_id=?, pesan=?, tanggal=? WHERE id=?";
        try (Connection conn = JDBC.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, p.getUserId());
            stmt.setString(2, p.getPesan());
            stmt.setDate(3, (java.sql.Date) p.getTanggal());
            stmt.setString(4, p.getId());
            stmt.executeUpdate();
        }
    }

    private void delete(String id) throws SQLException {
        String sql = "DELETE FROM pengingat WHERE id=?";
        try (Connection conn = JDBC.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            stmt.executeUpdate();
        }
    }

    private Pengingat getById(String id) throws SQLException {
        String sql = "SELECT * FROM pengingat WHERE id=?";
        try (Connection conn = JDBC.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Pengingat(
                            rs.getString("id"),
                            rs.getString("user_id"),
                            rs.getString("pesan"),
                            rs.getDate("tanggal"));
                }
            }
        }
        return null;
    }

    private List<Pengingat> getAll() throws SQLException {
        List<Pengingat> list = new ArrayList<>();
        String sql = "SELECT * FROM pengingat";
        try (Connection conn = JDBC.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Pengingat(
                        rs.getString("id"),
                        rs.getString("user_id"),
                        rs.getString("pesan"),
                        rs.getDate("tanggal")));
            }
        }
        return list;
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
