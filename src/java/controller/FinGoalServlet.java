
package controller;

import model.FinGoal;
import util.JDBC;
import util.ParseUtils; // Added import

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.*;
import java.util.*;

public class FinGoalServlet extends HttpServlet {

    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String action = req.getParameter("action");
        try {
            if ("edit".equals(action)) {
                String id = req.getParameter("id");
                FinGoal goal = getById(id);
                req.setAttribute("goal", goal);
                req.getRequestDispatcher("/model/FinGoal-form.jsp").forward(req, res);
            } else if ("delete".equals(action)) {
                delete(req.getParameter("id"));
                res.sendRedirect("FinGoalServlet");
            } else {
                List<FinGoal> list = getAll();
                req.setAttribute("fingoals", list);
                req.getRequestDispatcher("/model/FinGoal-list.jsp").forward(req, res);
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        FinGoal f = new FinGoal(
            req.getParameter("id"),
            req.getParameter("targetId"),
            Double.parseDouble(req.getParameter("progress")),
            req.getParameter("status")
        );

        try {
            if (getById(f.getId()) != null) {
                update(f);
            } else {
                insert(f);
            }
            res.sendRedirect("FinGoalServlet");
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private void insert(FinGoal f) throws SQLException {
        String sql = "INSERT INTO fingoal (id, target_id, progress, status) VALUES (?, ?, ?, ?)";
        try (Connection conn = JDBC.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, f.getId());
            stmt.setString(2, f.getTargetId());
            stmt.setDouble(3, f.getProgress());
            stmt.setString(4, f.getStatus());
            stmt.executeUpdate();
        }
    }

    private void update(FinGoal f) throws SQLException {
        String sql = "UPDATE fingoal SET target_id=?, progress=?, status=? WHERE id=?";
        try (Connection conn = JDBC.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, f.getTargetId());
            stmt.setDouble(2, f.getProgress());
            stmt.setString(3, f.getStatus());
            stmt.setString(4, f.getId());
            stmt.executeUpdate();
        }
    }

    private void delete(String id) throws SQLException {
        String sql = "DELETE FROM fingoal WHERE id=?";
        try (Connection conn = JDBC.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            stmt.executeUpdate();
        }
    }

    private FinGoal getById(String id) throws SQLException {
        String sql = "SELECT * FROM fingoal WHERE id=?";
        try (Connection conn = JDBC.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new FinGoal(
                        rs.getString("id"),
                        rs.getString("target_id"),
                        rs.getDouble("progress"),
                        rs.getString("status")
                    );
                }
            }
        }
        return null;
    }

    private List<FinGoal> getAll() throws SQLException {
        List<FinGoal> list = new ArrayList<>();
        String sql = "SELECT * FROM fingoal";
        try (Connection conn = JDBC.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new FinGoal(
                    rs.getString("id"),
                    rs.getString("target_id"),
                    rs.getDouble("progress"),
                    rs.getString("status")
                ));
            }
        }
        return list;
    }
}

