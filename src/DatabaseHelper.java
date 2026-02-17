import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.Random;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.io.FileInputStream;
import java.util.Properties;



public class DatabaseHelper {
    // Replace with your actual database URL, user, and password
    private static  String URL;
    private static  String USER;
    private static  String PASS;

    static {
        try (FileInputStream fis = new FileInputStream("config.properties")) { // Must be in the root folder!
            Properties prop = new Properties();
            prop.load(fis);
            URL = prop.getProperty("db.url");
            USER = prop.getProperty("db.user");
            PASS = prop.getProperty("db.password");
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (Exception e) {
            System.err.println("CRITICAL: config.properties not found in root!");
            e.printStackTrace();
        }
    }

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }



    // --- REGISTRATION & AUTHENTICATION ---

    public static boolean emailExists(String email) {
        String query = "SELECT COUNT(*) FROM Patients WHERE email = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean saveInitialPatient(String fName, String lName, String email) {
        String sql = "INSERT INTO Patients (first_name, last_name, email) VALUES (?, ?, ?)";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, fName);
            pstmt.setString(2, lName);
            pstmt.setString(3, email);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean RegisterFullAccount(String email, String user, String pass) {
        String sql = "UPDATE Patients SET username = ?, password = ? WHERE email = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user);
            pstmt.setString(2, pass);
            pstmt.setString(3, email);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean validateLogin(String user, String pass) {
        String sql = "SELECT * FROM Patients WHERE username = ? AND password = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user);
            pstmt.setString(2, pass);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // --- USER DATA FETCHING ---

    public static String getEmailByUsername(String user) {
        String sql = "SELECT email FROM Patients WHERE username = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user);
            ResultSet rs = pstmt.executeQuery();
            return rs.next() ? rs.getString("email") : null;
        } catch (SQLException e) { return null; }
    }

    public static String getFirstName(String email) {
        String sql = "SELECT first_name FROM Patients WHERE email = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            return rs.next() ? rs.getString("first_name") : "User";
        } catch (SQLException e) { return "User"; }
    }

    // --- FAMILY MANAGEMENT (Criterion #1 & #2) ---

    public static boolean saveFamilyMember(String parentEmail, String name, int age, String dob) {
        String sql = "INSERT INTO FamilyMembers (parent_email, member_name, age, dob) VALUES (?, ?, ?, ?)";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, parentEmail);
            pstmt.setString(2, name);
            pstmt.setInt(3, age);
            pstmt.setString(4, dob);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) { return false; }
    }

    public static List<String> getFamilyMembers(String parentEmail) {
        List<String> members = new ArrayList<>();
        String sql = "SELECT member_name FROM FamilyMembers WHERE parent_email = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, parentEmail);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) members.add(rs.getString("member_name"));
        } catch (SQLException e) { e.printStackTrace(); }
        return members;
    }

    public static boolean isChild(String name) {
        String sql = "SELECT age FROM FamilyMembers WHERE member_name = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            ResultSet rs = pstmt.executeQuery();
            return rs.next() && rs.getInt("age") < 18;
        } catch (SQLException e) { return false; }
    }

    public static boolean hasFamilyMembers(String email) {
        return !getFamilyMembers(email).isEmpty();
    }

    // --- APPOINTMENT LOGIC (Criterion #6 & #7) ---

    public static boolean isSlotAvailable(String startTime, String endTime) {
        // Checks if any existing appointment's range overlaps with the new range
        String sql = "SELECT COUNT(*) FROM Appointments WHERE " +
                "(appointment_time < ? AND end_time > ?)";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, endTime);
            pstmt.setString(2, startTime);
            ResultSet rs = pstmt.executeQuery();
            return rs.next() && rs.getInt(1) == 0;
        } catch (SQLException e) { return false; }
    }

    public static boolean bookAppointment(String email, String patient, String proc, String time) {
        int duration = getProcedureDuration(proc);
        String endTime = calculateEndTime(time, duration);

        String sql = "INSERT INTO Appointments (parent_email, patient_name, procedure_name, appointment_time, end_time) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            pstmt.setString(2, patient);
            pstmt.setString(3, proc);
            pstmt.setString(4, time);
            pstmt.setString(5, endTime);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) { return false; }
    }

    public static boolean updateAppointmentDetails(String email, String oldTime, String newTime, String newProc, String newPatient) {
        String sql = "UPDATE Appointments SET appointment_time = ?, procedure_name = ?, patient_name = ? " +
                "WHERE parent_email = ? AND appointment_time = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newTime);
            pstmt.setString(2, newProc);
            pstmt.setString(3, newPatient);
            pstmt.setString(4, email);
            pstmt.setString(5, oldTime);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) { return false; }
    }

    // --- HELPERS ---

    public static int getProcedureDuration(String proc) {
        if (proc.contains("Wisdom Teeth") || proc.contains("Implant") || proc.contains("Oral Rehab")) return 120;
        if (proc.contains("Periodontal") || proc.contains("Gum")) return 60;
        if (proc.contains("Rehabilitation") || proc.contains("Cleaning")) return 30;
        return 15; // Diagnostic/Default
    }

    private static String calculateEndTime(String start, int duration) {
        String[] parts = start.split(":");
        int total = (Integer.parseInt(parts[0]) * 60) + Integer.parseInt(parts[1]) + duration;
        return String.format("%02d:%02d", total / 60, total % 60);
    }

    public static boolean updatePassword(String email, String newPass) {
        String sql = "UPDATE Patients SET password = ? WHERE email = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newPass);
            pstmt.setString(2, email);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) { return false; }
    }

    // Fix for updateUsername error
    public static boolean updateUsername(String email, String newName) {
        String sql = "UPDATE Patients SET first_name = ? WHERE email = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newName);
            pstmt.setString(2, email);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) { return false; }
    }

    // Fix for getConnection() error
// This allows other classes to use your connection settings safely
    public static Connection getConnection() throws SQLException {
        return connect();
    }

    // Fix for getFamilyMembersDetailed error (Used for editing family)
    public static List<String[]> getFamilyMembersDetailed(String email) {
        List<String[]> list = new ArrayList<>();
        String sql = "SELECT member_name, age, dob FROM FamilyMembers WHERE parent_email = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(new String[]{rs.getString(1), rs.getString(2), rs.getString(3)});
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    // Fix for deleteFamilyMember error
    public static boolean deleteFamilyMember(String parentEmail, String memberName) {
        String sql = "DELETE FROM FamilyMembers WHERE parent_email = ? AND member_name = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, parentEmail);
            pstmt.setString(2, memberName);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) { return false; }
    }


}