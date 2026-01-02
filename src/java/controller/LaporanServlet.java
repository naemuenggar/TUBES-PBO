package controller;

import model.Laporan;
import util.JDBC;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.*;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

public class LaporanServlet extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        model.User currentUser = (session != null) ? (model.User) session.getAttribute("user") : null;

        if (currentUser == null) {
            res.sendRedirect("login.jsp");
            return;
        }

        try {
            Laporan laporan = getCurrentReport(currentUser);
            req.setAttribute("laporan", laporan);
            req.getRequestDispatcher("/model/Laporan-view.jsp").forward(req, res);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private Laporan getCurrentReport(model.User user) throws SQLException {
        double pemasukan = 0, pengeluaran = 0;
        boolean isAdmin = "admin".equals(user.getRole());
        String sqlPemasukan, sqlPengeluaran;

        if (isAdmin) {
             sqlPemasukan = "SELECT SUM(jumlah) as total FROM transaksi WHERE jenis='pemasukan'";
             sqlPengeluaran = "SELECT SUM(jumlah) as total FROM transaksi WHERE jenis='pengeluaran'";
        } else {
             sqlPemasukan = "SELECT SUM(jumlah) as total FROM transaksi WHERE jenis='pemasukan' AND user_id=?";
             sqlPengeluaran = "SELECT SUM(jumlah) as total FROM transaksi WHERE jenis='pengeluaran' AND user_id=?";
        }

        try (Connection conn = JDBC.getConnection()) {
            // Ambil total pemasukan
            try (PreparedStatement ps1 = conn.prepareStatement(sqlPemasukan)) {
                 if (!isAdmin) ps1.setString(1, user.getId());
                 try (ResultSet rs1 = ps1.executeQuery()) {
                    if (rs1.next())
                        pemasukan = rs1.getDouble("total");
                 }
            }

            // Ambil total pengeluaran
            try (PreparedStatement ps2 = conn.prepareStatement(sqlPengeluaran)) {
                 if (!isAdmin) ps2.setString(1, user.getId());
                 try (ResultSet rs2 = ps2.executeQuery()) {
                    if (rs2.next())
                        pengeluaran = rs2.getDouble("total");
                 }
            }
        }

        return new Laporan("SUMMARY", pemasukan, pengeluaran, new Date(), new Date());
    }
}
