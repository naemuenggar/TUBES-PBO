
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SimpleCheck {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/MoneyMate";
        String user = "root";
        String pass = "";
        
        System.out.println("Testing connection to: " + url);
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, user, pass);
            System.out.println("SUCCESS: Connection established!");
            conn.close();
        } catch (ClassNotFoundException e) {
            System.out.println("ERROR: Driver not found!");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("ERROR: Connection failed!");
            System.out.println("Message: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
        } catch (Exception e) {
            System.out.println("ERROR: Other exception!");
            e.printStackTrace();
        }
    }
}
