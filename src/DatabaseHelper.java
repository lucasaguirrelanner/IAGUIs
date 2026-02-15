import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Properties;

public class DatabaseHelper {
    public static Connection getConnection() throws Exception {
        Properties properties = new Properties();

        try (InputStream input = new FileInputStream("src/config.properties")) {
            properties.load(input);

            String db_url = properties.getProperty("db.url");
            String db_user = properties.getProperty("db.user");
            String db_pass = properties.getProperty("db.password");

            return DriverManager.getConnection(db_url, db_user, db_pass);
        }

    }
        public static boolean Save_Patient(String fName, String lName, String email){
            String sql = "INSERT INTO Patients (first_name, last_name, email) VALUES (?, ?, ?)";

            try (Connection conn = getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql))
            {
                pstmt.setString(1, fName);
                pstmt.setString(2, lName);
                pstmt.setString(3, email);

                int rows = pstmt.executeUpdate();
                return rows > 0;

            } catch (Exception e)
            {
               e.printStackTrace();
               return false;
            }

        }

    }


