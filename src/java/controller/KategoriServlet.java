
package controller;

import model.Kategori;
import util.JDBC;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.*;
import java.util.*;

public class KategoriServlet extends HttpServlet {

    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String action = req.getParameter("action");
        try {
            if ("edit".equals(action)) {
                String id = req.getParameter("id");
                Kategori kategori = getKategoriById(id);
                req.setAttribute("kategori", kategori);
                req.getRequestDispatcher("/model/Kategori-form.jsp").forward(req, res);
            } else if ("form".equals(action)) {
                req.getRequestDispatcher("/model/Kategori-form.jsp").forward(req, res);
            } else if ("delete".equals(action)) {
                deleteKategori(req.getParameter("id"));
                res.sendRedirect("KategoriServlet");
            } else {
                List<Kategori> list = getAllKategori();
                req.setAttribute("kategoris", list);
                req.getRequestDispatcher("/model/Kategori-list.jsp").forward(req, res);
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String id = req.getParameter("id");
        String nama = req.getParameter("nama");
        String tipe = req.getParameter("tipe");

        System.out.println("DEBUG KATEGORI: " + id + ", " + nama + ", " + tipe);

        Kategori kategori = new Kategori(id, nama, tipe);

        try {
            if (getKategoriById(kategori.getId()) != null) {
                updateKategori(kategori);
            } else {
                insertKategori(kategori);
            }
            res.sendRedirect("KategoriServlet");
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException(e);
        }
    }

    private void insertKategori(Kategori k) throws SQLException {
        String sql = "INSERT INTO kategori (id, nama, tipe) VALUES (?, ?, ?)";
        try (Connection conn = JDBC.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, k.getId());
            stmt.setString(2, k.getNama());
            stmt.setString(3, k.getTipe());
            int rows = stmt.executeUpdate();
            System.out.println("DEBUG INSERT ROWS: " + rows);
        }
    }

    private void updateKategori(Kategori k) throws SQLException {
        String sql = "UPDATE kategori SET nama=?, tipe=? WHERE id=?";
        try (Connection conn = JDBC.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, k.getNama());
            stmt.setString(2, k.getTipe());
            stmt.setString(3, k.getId());
            stmt.executeUpdate();
        }
    }

    private void deleteKategori(String id) throws SQLException {
        String sql = "DELETE FROM kategori WHERE id=?";
        try (Connection conn = JDBC.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            stmt.executeUpdate();
        }
    }

    private Kategori getKategoriById(String id) throws SQLException {
        String sql = "SELECT * FROM kategori WHERE id=?";
        try (Connection conn = JDBC.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Kategori(
                            rs.getString("id"),
                            rs.getString("nama"),
                            rs.getString("tipe"));
                }
            }
        }
        return null;
    }

    private List<Kategori> getAllKategori() throws SQLException {
        List<Kategori> list = new ArrayList<>();
        String sql = "SELECT * FROM kategori";
        try (Connection conn = JDBC.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Kategori(
                        rs.getString("id"),
                        rs.getString("nama"),
                        rs.getString("tipe")));
            }
        }
        return list;
    }
}
