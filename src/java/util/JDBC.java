
package util;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JDBC {

    private static final String URL = "jdbc:mysql://localhost:3306/MoneyMate";
    private static final String USER = "root";
    private static final String PASS = "";
    private static final Logger LOGGER = Logger.getLogger(JDBC.class.getName());

    public static Connection getConnection() throws SQLException {
        try {
            // System.out.println("JDBC: Connecting to " + URL + " as " + USER + "..."); //
            // Removed verbose logging
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(URL, USER, PASS);
            // System.out.println("JDBC: Connection successful!"); // Removed verbose
            // logging
            return conn;
        } catch (ClassNotFoundException e) {
            String msg = "JDBC Error: Driver not found! " + e.getMessage();
            LOGGER.log(Level.SEVERE, msg, e);
            throw new SQLException("Driver library missing", e);
        } catch (SQLException e) {
            String msg = "JDBC Error: Connection failed! " + e.getMessage();
            LOGGER.log(Level.SEVERE, msg, e);
            throw e;
        }
    }

    public static void close(Connection conn, Statement stmt, ResultSet rs) {
        try {
            if (rs != null)
                rs.close();
        } catch (Exception ignored) {
            // implicit close
        }
        try {
            if (stmt != null)
                stmt.close();
        } catch (Exception ignored) {
            // implicit close
        }
        try {
            if (conn != null)
                conn.close();
        } catch (Exception ignored) {
            // implicit close
        }
    }
}
