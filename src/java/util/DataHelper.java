package util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Kategori;
import model.User;

public class DataHelper {

    public static List<User> getAllUsers() throws SQLException {
        List<User> list = new ArrayList<>();
        String sql = "SELECT * FROM user";
        try (Connection conn = JDBC.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                list.add(new User(
                        rs.getString("id"),
                        rs.getString("nama"),
                        rs.getString("email"),
                        rs.getString("password"), // Note: Ideally we shouldn't fetch password unless needed
                        rs.getString("role")));
            }
        }
        return list;
    }

    public static List<Kategori> getAllKategori() throws SQLException {
        List<Kategori> list = new ArrayList<>();
        String sql = "SELECT * FROM kategori";
        try (Connection conn = JDBC.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                list.add(new Kategori(rs.getString("id"), rs.getString("nama")));
            }
        }
        return list;
    }
}
