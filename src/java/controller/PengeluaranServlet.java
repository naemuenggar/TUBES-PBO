
package controller;

import model.Pengeluaran;
import util.JDBC;
import java.sql.Date;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.*;
import java.util.*;

public class PengeluaranServlet extends HttpServlet {

    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String action = req.getParameter("action");
        try {
            if ("edit".equals(action)) {
                String id = req.getParameter("id");
                Pengeluaran p = getById(id);
                req.setAttribute("pengeluaran", p);
                req.getRequestDispatcher("/model/Pengeluaran-form.jsp").forward(req, res);
            } else if ("delete".equals(action)) {
                delete(req.getParameter("id"));
                res.sendRedirect("PengeluaranServlet");
            } else {
                List<Pengeluaran> list = getAll();
                req.setAttribute("pengeluaranList", list);
                req.getRequestDispatcher("/model/Pengeluaran-list.jsp").forward(req, res);
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        Pengeluaran p = new Pengeluaran(
            req.getParameter("id"),
            req.getParameter("userId"),
            Double.parseDouble(req.getParameter("jumlah")),
            req.getParameter("deskripsi"),
            Date.valueOf(req.getParameter("tanggal")),
            req.getParameter("kategoriId")
        );

        try {
            if (getById(p.getId()) != null) {
                update(p);
            } else {
                insert(p);
            }
            res.sendRedirect("PengeluaranServlet");
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private void insert(Pengeluaran p) throws SQLException {
        String sql = "INSERT INTO transaksi (id, user_id, jumlah, deskripsi, tanggal, kategori_id, jenis) VALUES (?, ?, ?, ?, ?, ?, 'pengeluaran')";
        try (Connection conn = JDBC.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, p.getId());
            stmt.setString(2, p.getUserId());
            stmt.setDouble(3, p.getJumlah());
            stmt.setString(4, p.getDeskripsi());
            stmt.setDate(5, new java.sql.Date(p.getTanggal().getTime()));
            stmt.setString(6, p.getKategoriId());
            stmt.executeUpdate();
        }
    }

    private void update(Pengeluaran p) throws SQLException {
        String sql = "UPDATE transaksi SET user_id=?, jumlah=?, deskripsi=?, tanggal=?, kategori_id=? WHERE id=? AND jenis='pengeluaran'";
        try (Connection conn = JDBC.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, p.getUserId());
            stmt.setDouble(2, p.getJumlah());
            stmt.setString(3, p.getDeskripsi());
            stmt.setDate(4, new java.sql.Date(p.getTanggal().getTime()));
            stmt.setString(5, p.getKategoriId());
            stmt.setString(6, p.getId());
            stmt.executeUpdate();
        }
    }

    private void delete(String id) throws SQLException {
        String sql = "DELETE FROM transaksi WHERE id=? AND jenis='pengeluaran'";
        try (Connection conn = JDBC.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            stmt.executeUpdate();
        }
    }

    private Pengeluaran getById(String id) throws SQLException {
        String sql = "SELECT * FROM transaksi WHERE id=? AND jenis='pengeluaran'";
        try (Connection conn = JDBC.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Pengeluaran(
                        rs.getString("id"),
                        rs.getString("user_id"),
                        rs.getDouble("jumlah"),
                        rs.getString("deskripsi"),
                        rs.getDate("tanggal"),
                        rs.getString("kategori_id")
                    );
                }
            }
        }
        return null;
    }

    private List<Pengeluaran> getAll() throws SQLException {
        List<Pengeluaran> list = new ArrayList<>();
        String sql = "SELECT * FROM transaksi WHERE jenis='pengeluaran'";
        try (Connection conn = JDBC.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Pengeluaran(
                    rs.getString("id"),
                    rs.getString("user_id"),
                    rs.getDouble("jumlah"),
                    rs.getString("deskripsi"),
                    rs.getDate("tanggal"),
                    rs.getString("kategori_id")
                ));
            }
        }
        return list;
    }
}
