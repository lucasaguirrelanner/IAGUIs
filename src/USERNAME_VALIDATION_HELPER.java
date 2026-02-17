import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class USERNAME_VALIDATION_HELPER {

    /**
     * Checks if the format of the username is acceptable.
     * Logic: 4-15 characters, alphanumeric and underscores only.
     */
    public static boolean isUsernameValid(String username) {
        if (username == null) return false;
        if (username.length() < 4 || username.length() > 15) {
            return false;
        }
        // Allows letters, numbers, and underscores. No spaces or special chars.
        return username.matches("^[a-zA-Z0-9_]+$");
    }

    /**
     * Returns a specific message for the GUI to display if validation fails.
     */
    public static String getUsernameRequirements(String username) {
        if (username == null || username.isEmpty()) return "Username cannot be empty.";
        if (username.length() < 4) return "Username is too short (min 4 chars).";
        if (username.length() > 15) return "Username is too long (max 15 chars).";
        if (!username.matches("^[a-zA-Z0-9_]+$")) return "Use only letters, numbers, and underscores.";
        return "OK";
    }

    /**
     * Checks the database to see if this username is already in use.
     * Directly supports Success Criterion #3 (Unique Accounts).
     */
    public static boolean isUsernameTaken(String username) {
        String sql = "SELECT COUNT(*) FROM Patients WHERE username = ?";

        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (Exception e) {
            System.err.println("Username Check Error: " + e.getMessage());
        }

        return false;
    }
}