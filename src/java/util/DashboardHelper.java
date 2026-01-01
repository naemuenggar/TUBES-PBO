package util;

import java.sql.*;
import java.util.*;
import model.Tagihan;
import model.Pengingat;
import java.util.Date;

public class DashboardHelper {

    public static List<Tagihan> getDueTagihan(String userId) {
        List<Tagihan> list = new ArrayList<>();
        // Get tagihan due in next 3 days
        String sql = "SELECT * FROM tagihan WHERE user_id=? AND tanggal_jatuh_tempo BETWEEN CURDATE() AND DATE_ADD(CURDATE(), INTERVAL 3 DAY)";
        try (Connection conn = JDBC.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(new Tagihan(
                    rs.getString("id"),
                    rs.getString("user_id"),
                    rs.getString("nama"),
                    rs.getDouble("jumlah"),
                    rs.getDate("tanggal_jatuh_tempo")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    
    public static List<Pengingat> getTodayPengingat(String userId) {
        List<Pengingat> list = new ArrayList<>();
        // Get reminders for today
        String sql = "SELECT * FROM pengingat WHERE user_id=? AND tanggal = CURDATE()";
        try (Connection conn = JDBC.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                // Using constructor: id, userId, pesan, tanggal
                list.add(new Pengingat(
                    rs.getString("id"),
                    rs.getString("user_id"),
                    rs.getString("pesan"),
                    rs.getDate("tanggal")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
