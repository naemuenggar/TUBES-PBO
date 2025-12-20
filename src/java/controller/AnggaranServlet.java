package controller;

import model.Anggaran;
import util.JDBC;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.*;
import java.util.*;

public class AnggaranServlet extends HttpServlet {

    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String action = req.getParameter("action");
        try {
            if ("edit".equals(action)) {
                String id = req.getParameter("id");
                Anggaran anggaran = getById(id);
                req.setAttribute("anggaran", anggaran);
                req.getRequestDispatcher("/model/Anggaran-form.jsp").forward(req, res);
            } else if ("delete".equals(action)) {
                delete(req.getParameter("id"));
                res.sendRedirect("AnggaranServlet");
            } else {
                List<Anggaran> list = getAll();
                req.setAttribute("anggarans", list);
                req.getRequestDispatcher("/model/Anggaran-list.jsp").forward(req, res);
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        Anggaran a = new Anggaran(
            req.getParameter("id"),
            req.getParameter("userId"),
            req.getParameter("nama"),
            Double.parseDouble(req.getParameter("jumlah"))
        );

        try {
            if (getById(a.getId()) != null) {
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

    private List<Anggaran> getAll() throws SQLException {
        List<Anggaran> list = new ArrayList<>();
        String sql = "SELECT * FROM anggaran";
        try (Connection conn = JDBC.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Anggaran(
                    rs.getString("id"),
                    rs.getString("user_id"),
                    rs.getString("nama"),
                    rs.getDouble("jumlah")
                ));
            }
        }
        return list;
    }
}

