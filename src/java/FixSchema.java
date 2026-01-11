import util.JDBC;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FixSchema {
    private static final Logger LOGGER = Logger.getLogger(FixSchema.class.getName());

    public static void main(String[] args) {
        System.out.println("Starting Database Schema Fix...");
        try (Connection conn = JDBC.getConnection(); Statement stmt = conn.createStatement()) {

            // 1. Fix transaksi.jumlah
            System.out.println("Altering 'transaksi' table...");
            stmt.executeUpdate("ALTER TABLE transaksi MODIFY COLUMN jumlah DECIMAL(20,2) NOT NULL");

            // 2. Fix anggaran.jumlah
            System.out.println("Altering 'anggaran' table...");
            stmt.executeUpdate("ALTER TABLE anggaran MODIFY COLUMN jumlah DECIMAL(20,2) NOT NULL");

            // 3. Fix target_tabungan.jumlah_target
            System.out.println("Altering 'target_tabungan' table...");
            stmt.executeUpdate("ALTER TABLE target_tabungan MODIFY COLUMN jumlah_target DECIMAL(20,2) NOT NULL");

            // 4. Fix fingoal.progress
            System.out.println("Altering 'fingoal' table...");
            stmt.executeUpdate("ALTER TABLE fingoal MODIFY COLUMN progress DECIMAL(20,2) NOT NULL DEFAULT 0");

            // 5. Fix tagihan.jumlah
            System.out.println("Altering 'tagihan' table...");
            stmt.executeUpdate("ALTER TABLE tagihan MODIFY COLUMN jumlah DECIMAL(20,2) NOT NULL");

            System.out.println("SUCCESS: Database schema updated successfully!");
        } catch (SQLException e) {
            System.err.println("ERROR: Failed to update schema.");
            LOGGER.log(Level.SEVERE, "Failed to update schema", e);
        }
    }
}
