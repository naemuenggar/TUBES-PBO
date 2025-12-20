
package controller;

import model.Pemasukan;
import util.JDBC;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.*;
import java.util.*;

public class PemasukanServlet extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        List<Pemasukan> pemasukanList = new ArrayList<>();
        try (Connection conn = JDBC.getConnection()) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM transaksi WHERE jenis='pemasukan'");
            while (rs.next()) {
                pemasukanList.add(new Pemasukan(
                    rs.getString("id"),
                    rs.getString("user_id"),
                    rs.getDouble("jumlah"),
                    rs.getString("deskripsi"),
                    rs.getDate("tanggal"),
                    rs.getString("kategori_id")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        req.setAttribute("pemasukanList", pemasukanList);
        RequestDispatcher dispatcher = req.getRequestDispatcher("/model/Pemasukan-list.jsp");
        dispatcher.forward(req, res);
    }
}

