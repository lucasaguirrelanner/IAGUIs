import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class USERNAME_VALIDATION_HELPER {

    //the bolean will look at all username requirements
    public static boolean isUsernameValid(String username) {
        if (username == null) return false;
        if (username.length() < 4 || username.length() > 15) {
            return false;

        }

        return username.matches("^[a-zA-Z0-9_]+$");

    }

    //this helps display error messages and stops an invalid username from being created and stored:
    public static String getUsernameRequirements(String username) {
        if (username == null || username.isEmpty()) return "Username cannot be empty.";
        if (username.length() < 4) return "Username is too short (min 4 chars).";
        if (username.length() > 15) return "Username is too long (max 15 chars).";
        if (!username.matches("^[a-zA-Z0-9_]+$")) return "Use only letters, numbers, and underscores.";
        return "OK";

    }

    //Checks the DB to see if the username is already in use:

    public static boolean isUsernameTaken(String username) {
        String sql= "SELECT COUNT(*) FROM Patients WHERE username = ?";

        try(Connection conn = DatabaseHelper.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;

                }
            }

        } catch (Exception e){

            System.err.println("Username Check Error: " + e.getMessage());

        }

        return false;
    }

}
