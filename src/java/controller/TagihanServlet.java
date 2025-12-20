
package controller;

import model.Tagihan;
import util.JDBC;
import java.sql.Date;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.*;
import java.util.*;

import model.User;

public class TagihanServlet extends HttpServlet {

    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String action = req.getParameter("action");
        try {
            if ("edit".equals(action)) {
                String id = req.getParameter("id");
                Tagihan tagihan = getById(id);
                req.setAttribute("tagihan", tagihan);
                req.setAttribute("users", getAllUsers());
                req.getRequestDispatcher("/model/Tagihan-form.jsp").forward(req, res);
            } else if ("form".equals(action)) {
                req.setAttribute("users", getAllUsers());
                req.getRequestDispatcher("/model/Tagihan-form.jsp").forward(req, res);
            } else if ("delete".equals(action)) {
                delete(req.getParameter("id"));
                res.sendRedirect("TagihanServlet");
            } else {
                List<Tagihan> list = getAll();
                req.setAttribute("tagihanList", list);
                req.getRequestDispatcher("/model/Tagihan-list.jsp").forward(req, res);
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        Tagihan t = new Tagihan(
                req.getParameter("id"),
                req.getParameter("userId"),
                req.getParameter("nama"),
                Double.parseDouble(req.getParameter("jumlah")),
                java.sql.Date.valueOf(req.getParameter("tanggalJatuhTempo")));

        try {
            if (getById(t.getId()) != null) {
                update(t);
            } else {
                insert(t);
            }
            res.sendRedirect("TagihanServlet");
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private void insert(Tagihan t) throws SQLException {
        String sql = "INSERT INTO tagihan (id, user_id, nama, jumlah, tanggal_jatuh_tempo) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = JDBC.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, t.getId());
            stmt.setString(2, t.getUserId());
            stmt.setString(3, t.getNama());
            stmt.setDouble(4, t.getJumlah());
            stmt.setDate(5, (java.sql.Date) t.getTanggalJatuhTempo());
            stmt.executeUpdate();
        }
    }

    private void update(Tagihan t) throws SQLException {
        String sql = "UPDATE tagihan SET user_id=?, nama=?, jumlah=?, tanggal_jatuh_tempo=? WHERE id=?";
        try (Connection conn = JDBC.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, t.getUserId());
            stmt.setString(2, t.getNama());
            stmt.setDouble(3, t.getJumlah());
            stmt.setDate(4, (java.sql.Date) t.getTanggalJatuhTempo());
            stmt.setString(5, t.getId());
            stmt.executeUpdate();
        }
    }

    private void delete(String id) throws SQLException {
        String sql = "DELETE FROM tagihan WHERE id=?";
        try (Connection conn = JDBC.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            stmt.executeUpdate();
        }
    }

    private Tagihan getById(String id) throws SQLException {
        String sql = "SELECT * FROM tagihan WHERE id=?";
        try (Connection conn = JDBC.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Tagihan(
                            rs.getString("id"),
                            rs.getString("user_id"),
                            rs.getString("nama"),
                            rs.getDouble("jumlah"),
                            rs.getDate("tanggal_jatuh_tempo"));
                }
            }
        }
        return null;
    }

    private List<Tagihan> getAll() throws SQLException {
        List<Tagihan> list = new ArrayList<>();
        String sql = "SELECT * FROM tagihan";
        try (Connection conn = JDBC.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Tagihan(
                        rs.getString("id"),
                        rs.getString("user_id"),
                        rs.getString("nama"),
                        rs.getDouble("jumlah"),
                        rs.getDate("tanggal_jatuh_tempo")));
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
