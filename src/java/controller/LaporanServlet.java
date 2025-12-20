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
        try {
            Laporan laporan = getCurrentReport();
            req.setAttribute("laporan", laporan);
            req.getRequestDispatcher("/model/Laporan-view.jsp").forward(req, res);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private Laporan getCurrentReport() throws SQLException {
        double pemasukan = 0, pengeluaran = 0;

        try (Connection conn = JDBC.getConnection()) {
            // Ambil total pemasukan
            try (PreparedStatement ps1 = conn
                    .prepareStatement("SELECT SUM(jumlah) as total FROM transaksi WHERE jenis='pemasukan'");
                    ResultSet rs1 = ps1.executeQuery()) {
                if (rs1.next())
                    pemasukan = rs1.getDouble("total");
            }

            // Ambil total pengeluaran
            try (PreparedStatement ps2 = conn
                    .prepareStatement("SELECT SUM(jumlah) as total FROM transaksi WHERE jenis='pengeluaran'");
                    ResultSet rs2 = ps2.executeQuery()) {
                if (rs2.next())
                    pengeluaran = rs2.getDouble("total");
            }
        }

        // Create a dummy ID and Date range for now, as this is a summary
        return new Laporan("SUMMARY", pemasukan, pengeluaran, new Date(), new Date());
    }
}
