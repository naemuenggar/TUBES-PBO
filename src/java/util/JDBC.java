
package util;

import java.sql.*;

public class JDBC {

    private static final String URL = "jdbc:mysql://localhost:3306/moneymate";
    private static final String USER = "root";
    private static final String PASS = "";

    public static Connection getConnection() throws SQLException {
        try {
            System.out.println("JDBC: Connecting to " + URL + " as " + USER + "...");
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(URL, USER, PASS);
            System.out.println("JDBC: Connection successful!");
            return conn;
        } catch (ClassNotFoundException e) {
            System.err.println("JDBC Error: Driver not found!");
            e.printStackTrace();
            throw new SQLException("Driver library missing", e);
        } catch (SQLException e) {
            System.err.println("JDBC Error: Connection failed! " + e.getMessage());
            throw e;
        }
    }

    public static void close(Connection conn, Statement stmt, ResultSet rs) {
        try {
            if (rs != null)
                rs.close();
        } catch (Exception ignored) {
        }
        try {
            if (stmt != null)
                stmt.close();
        } catch (Exception ignored) {
        }
        try {
            if (conn != null)
                conn.close();
        } catch (Exception ignored) {
        }
    }
}
