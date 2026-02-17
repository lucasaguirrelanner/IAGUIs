import java.io.FileInputStream;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.time.LocalTime;

public class DatabaseHelper {
    private static String URL;
    private static String USER;
    private static String PASS;

    static {
        try (FileInputStream fis = new FileInputStream("config.properties")) {
            Properties prop = new Properties();
            prop.load(fis);
            URL  = prop.getProperty("db.url");
            USER = prop.getProperty("db.user");
            PASS = prop.getProperty("db.password");
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (Exception e) {
            System.err.println("CRITICAL: config.properties not found!");
            e.printStackTrace();
        }
    }

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
    public static Connection getConnection() throws SQLException { return connect(); }

    // =========================================================
    // REGISTRATION & AUTH
    // =========================================================

    public static boolean emailExists(String email) {
        String sql = "SELECT COUNT(*) FROM Patients WHERE email = ?";
        try (Connection c = connect(); PreparedStatement p = c.prepareStatement(sql)) {
            p.setString(1, email);
            ResultSet rs = p.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public static boolean saveInitialPatient(String fName, String lName, String email) {
        // Use INSERT IGNORE to avoid duplicate-key errors if called twice
        String sql = "INSERT IGNORE INTO Patients (first_name, last_name, email, encrypted_password, username) " +
                "VALUES (?, ?, ?, 'PENDING', 'PENDING')";
        try (Connection c = connect(); PreparedStatement p = c.prepareStatement(sql)) {
            p.setString(1, fName); p.setString(2, lName); p.setString(3, email);
            return p.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    // FIX: column is `encrypted_password`, not `password`
    public static boolean RegisterFullAccount(String email, String user, String pass) {
        String sql = "UPDATE Patients SET username = ?, encrypted_password = ? WHERE email = ?";
        try (Connection c = connect(); PreparedStatement p = c.prepareStatement(sql)) {
            p.setString(1, user); p.setString(2, pass); p.setString(3, email);
            return p.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    // FIX: use encrypted_password
    public static boolean validateLogin(String user, String pass) {
        String sql = "SELECT * FROM Patients WHERE username = ? AND encrypted_password = ?";
        try (Connection c = connect(); PreparedStatement p = c.prepareStatement(sql)) {
            p.setString(1, user); p.setString(2, pass);
            return p.executeQuery().next();
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    // =========================================================
    // USER DATA
    // =========================================================

    public static String getEmailByUsername(String user) {
        String sql = "SELECT email FROM Patients WHERE username = ?";
        try (Connection c = connect(); PreparedStatement p = c.prepareStatement(sql)) {
            p.setString(1, user);
            ResultSet rs = p.executeQuery();
            return rs.next() ? rs.getString("email") : null;
        } catch (SQLException e) { return null; }
    }

    public static String getFirstName(String email) {
        String sql = "SELECT first_name FROM Patients WHERE email = ?";
        try (Connection c = connect(); PreparedStatement p = c.prepareStatement(sql)) {
            p.setString(1, email);
            ResultSet rs = p.executeQuery();
            return rs.next() ? rs.getString("first_name") : "User";
        } catch (SQLException e) { return "User"; }
    }

    public static String getPatientNameByEmail(String email) {
        String sql = "SELECT CONCAT(first_name,' ',last_name) AS full_name FROM Patients WHERE email = ?";
        try (Connection c = getConnection(); PreparedStatement p = c.prepareStatement(sql)) {
            p.setString(1, email);
            ResultSet rs = p.executeQuery();
            return rs.next() ? rs.getString("full_name") : null;
        } catch (Exception e) { e.printStackTrace(); return null; }
    }

    // FIX: use encrypted_password
    public static boolean updatePassword(String email, String newPass) {
        String sql = "UPDATE Patients SET encrypted_password = ? WHERE email = ?";
        try (Connection c = connect(); PreparedStatement p = c.prepareStatement(sql)) {
            p.setString(1, newPass); p.setString(2, email);
            return p.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public static boolean updateUsername(String email, String newName) {
        String sql = "UPDATE Patients SET first_name = ? WHERE email = ?";
        try (Connection c = connect(); PreparedStatement p = c.prepareStatement(sql)) {
            p.setString(1, newName); p.setString(2, email);
            return p.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    // =========================================================
    // FAMILY MANAGEMENT
    // =========================================================

    public static boolean saveFamilyMember(String parentEmail, String name, int age, String dob) {
        String sql = "INSERT INTO FamilyMembers (parent_email, member_name, age, dob) VALUES (?, ?, ?, ?)";
        try (Connection c = connect(); PreparedStatement p = c.prepareStatement(sql)) {
            p.setString(1, parentEmail); p.setString(2, name);
            p.setInt(3, age); p.setString(4, dob);
            return p.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public static List<String> getFamilyMembers(String parentEmail) {
        List<String> members = new ArrayList<>();
        String sql = "SELECT member_name FROM FamilyMembers WHERE parent_email = ?";
        try (Connection c = connect(); PreparedStatement p = c.prepareStatement(sql)) {
            p.setString(1, parentEmail);
            ResultSet rs = p.executeQuery();
            while (rs.next()) members.add(rs.getString("member_name"));
        } catch (SQLException e) { e.printStackTrace(); }
        return members;
    }

    public static List<String[]> getFamilyMembersDetailed(String parentEmail) {
        List<String[]> members = new ArrayList<>();
        String sql = "SELECT member_id, member_name, age, dob FROM FamilyMembers WHERE parent_email = ?";
        try (Connection c = connect(); PreparedStatement p = c.prepareStatement(sql)) {
            p.setString(1, parentEmail);
            ResultSet rs = p.executeQuery();
            while (rs.next()) members.add(new String[]{
                    rs.getString("member_id"),
                    rs.getString("member_name"),
                    String.valueOf(rs.getInt("age")),
                    rs.getString("dob")
            });
        } catch (SQLException e) { e.printStackTrace(); }
        return members;
    }

    public static boolean deleteFamilyMember(String parentEmail, String memberName) {
        String sql = "DELETE FROM FamilyMembers WHERE parent_email = ? AND member_name = ?";
        try (Connection c = connect(); PreparedStatement p = c.prepareStatement(sql)) {
            p.setString(1, parentEmail); p.setString(2, memberName);
            return p.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public static boolean hasFamilyMembers(String email) {
        return !getFamilyMembers(email).isEmpty();
    }

    // =========================================================
    // DENTISTS & PROCEDURES
    // =========================================================

    /** Returns list of "First Last" strings for all dentists. */
    public static List<String> getDentistNames() {
        List<String> names = new ArrayList<>();
        String sql = "SELECT DISTINCT CONCAT(first_name, IF(last_name IS NOT NULL," +
                " CONCAT(' ', last_name), '')) AS full_name " +
                "FROM Dentist ORDER BY full_name";
        try (Connection c = connect(); Statement st = c.createStatement()) {
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) names.add(rs.getString("full_name"));
        } catch (SQLException e) { e.printStackTrace(); }
        return names;
    }

    public static List<String> getDentistNamesForChildren() {
        List<String> names = new ArrayList<>();
        String sql = "SELECT CONCAT(first_name, IF(last_name IS NOT NULL," +
                " CONCAT(' ', last_name), '')) AS full_name " +
                "FROM Dentist WHERE sees_children = TRUE ORDER BY first_name";
        try (Connection c = connect(); Statement st = c.createStatement()) {
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) names.add(rs.getString("full_name"));
        } catch (SQLException e) { e.printStackTrace(); }
        return names;
    }
    public static int getFamilyMemberAge(int memberId) {
        String sql = "SELECT age FROM FamilyMembers WHERE member_id = ?";
        try (Connection c = connect(); PreparedStatement p = c.prepareStatement(sql)) {
            p.setInt(1, memberId);
            ResultSet rs = p.executeQuery();
            if (rs.next()) return rs.getInt("age");
        } catch (SQLException e) { e.printStackTrace(); }
        return 99; // safe fallback — treated as adult
    }

    /**
     * Returns procedure names for a given doctor's full name.
     * Format returned: "Procedure Name (X min)"
     */
    public static List<String> getProceduresForDentist(String doctorFullName) {
        List<String> procs = new ArrayList<>();
        String sql = "SELECT pt.procedure_name, pt.base_duration " +
                "FROM procedure_type pt " +
                "JOIN Dentist d ON pt.dentist_id = d.dentist_id " +
                "WHERE CONCAT(d.first_name, IF(d.last_name IS NOT NULL, CONCAT(' ', d.last_name), '')) = ?";
        try (Connection c = connect(); PreparedStatement p = c.prepareStatement(sql)) {
            p.setString(1, doctorFullName);
            ResultSet rs = p.executeQuery();
            while (rs.next())
                procs.add(rs.getString("procedure_name") + " (" + rs.getInt("base_duration") + " min)");
        } catch (SQLException e) { e.printStackTrace(); }
        // Fallback if table is empty
        if (procs.isEmpty()) {
            procs.add("Diagnostic (15 min)");
            procs.add("Oral Rehabilitation (30 min)");
            procs.add("Surgery: Wisdom Teeth (120 min)");
            procs.add("Surgery: Implant (120 min)");
            procs.add("Surgery: Periodontal (90 min)");
        }
        return procs;
    }

    public static List<String> getWorkingSlotsForDoctorOnDate(String doctorFullName, LocalDate date) {
        String[] allSlots = {"08:00","09:00","10:00","11:00","13:00","14:00","15:00","16:00"};

        String dayName = date.getDayOfWeek()
                .getDisplayName(java.time.format.TextStyle.FULL, java.util.Locale.ENGLISH);

        String sql = "SELECT open_time, close_time FROM Dentist_Schedules ds " +
                "JOIN Dentist d ON ds.dentist_id = d.dentist_id " +
                "WHERE CONCAT(d.first_name, IF(d.last_name IS NOT NULL," +
                " CONCAT(' ', d.last_name), '')) = ? " +
                "AND ds.day_of_week = ?";

        List<LocalTime> opens  = new ArrayList<>();
        List<LocalTime> closes = new ArrayList<>();

        try (Connection c = connect(); PreparedStatement p = c.prepareStatement(sql)) {
            p.setString(1, doctorFullName);
            p.setString(2, dayName);
            ResultSet rs = p.executeQuery();
            while (rs.next()) {
                opens.add(rs.getTime("open_time").toLocalTime());
                closes.add(rs.getTime("close_time").toLocalTime());
            }
        } catch (SQLException e) { e.printStackTrace(); }

        if (opens.isEmpty()) return new ArrayList<>(); // doctor not working this day

        List<String> valid = new ArrayList<>();
        for (String slot : allSlots) {
            String[]  parts    = slot.split(":");
            LocalTime slotTime = LocalTime.of(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
            for (int i = 0; i < opens.size(); i++) {
                if (!slotTime.isBefore(opens.get(i)) && slotTime.isBefore(closes.get(i))) {
                    valid.add(slot);
                    break;
                }
            }
        }
        return valid;
    }

    /** Returns duration in minutes for a procedure name (with or without the " (X min)" suffix). */
    public static int getProcedureDuration(String procedureDisplay) {
        // Try to parse from display string first
        if (procedureDisplay.contains("(") && procedureDisplay.contains(" min)")) {
            try {
                String num = procedureDisplay.substring(
                        procedureDisplay.lastIndexOf('(') + 1,
                        procedureDisplay.lastIndexOf(" min)")
                );
                return Integer.parseInt(num.trim());
            } catch (Exception ignored) {}
        }
        // Fallback query
        String sql = "SELECT base_duration FROM procedure_type WHERE procedure_name = ?";
        String name = procedureDisplay.contains("(")
                ? procedureDisplay.substring(0, procedureDisplay.lastIndexOf("(")).trim()
                : procedureDisplay;
        try (Connection c = connect(); PreparedStatement p = c.prepareStatement(sql)) {
            p.setString(1, name);
            ResultSet rs = p.executeQuery();
            if (rs.next()) return rs.getInt("base_duration");
        } catch (SQLException e) { e.printStackTrace(); }
        return 30; // default
    }

    // =========================================================
    // APPOINTMENT LOGIC
    // =========================================================

    /**
     * Checks if a doctor is available at the given start time for the given duration.
     * Blocks the slot if any existing appointment overlaps.
     */
    public static boolean isSlotAvailable(String doctorName, LocalDateTime newStart, int durationMinutes) {
        // We need to check: no existing CONFIRMED/COMPLETED appointment for this doctor
        // where the new slot [newStart, newStart+dur) overlaps with existing [existStart, existStart+existDur)
        // Overlap: newStart < existEnd AND newEnd > existStart
        // Since we don't store end_time, we derive existEnd by joining procedure_type
        String sql =
                "SELECT a.appointment_date, " +
                        "  COALESCE(pt.base_duration, 30) AS base_duration " +
                        "FROM Appointments a " +
                        "LEFT JOIN procedure_type pt ON a.procedure_type = pt.procedure_name " +
                        "WHERE a.doctor_name = ? " +
                        "  AND a.status NOT IN ('CANCELLED') " +
                        "  AND DATE(a.appointment_date) = DATE(?)";
        LocalDateTime newEnd = newStart.plusMinutes(durationMinutes);
        try (Connection c = connect(); PreparedStatement p = c.prepareStatement(sql)) {
            p.setString(1, doctorName);
            p.setTimestamp(2, Timestamp.valueOf(newStart));
            ResultSet rs = p.executeQuery();
            while (rs.next()) {
                LocalDateTime existStart = rs.getTimestamp("appointment_date").toLocalDateTime();
                int existDur = rs.getInt("base_duration");
                LocalDateTime existEnd = existStart.plusMinutes(existDur);
                // Check overlap
                if (newStart.isBefore(existEnd) && newEnd.isAfter(existStart)) {
                    return false; // Overlap found
                }
            }
            return true; // No overlap
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    /**
     * Returns blocked start-times (as HH:mm strings) for a doctor on a given date.
     * A time is "blocked" if it falls within an existing appointment's window.
     */
    public static List<String> getBlockedSlotsForDoctorOnDate(String doctorName, LocalDate date) {
        List<String> blocked = new ArrayList<>();
        String[] allSlots = {"08:00","09:00","10:00","11:00","13:00","14:00","15:00","16:00"};
        for (String slot : allSlots) {
            String[] parts = slot.split(":");
            LocalDateTime slotDT = date.atTime(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
            // We just mark a slot as blocked if ANY appointment occupies it
            String sql =
                    "SELECT a.appointment_date, COALESCE(pt.base_duration,30) AS base_duration " +
                            "FROM Appointments a " +
                            "LEFT JOIN procedure_type pt ON a.procedure_type = pt.procedure_name " +
                            "WHERE a.doctor_name = ? AND DATE(a.appointment_date) = ? AND a.status != 'CANCELLED'";
            try (Connection c = connect(); PreparedStatement p = c.prepareStatement(sql)) {
                p.setString(1, doctorName);
                p.setDate(2, Date.valueOf(date));
                ResultSet rs = p.executeQuery();
                boolean slotBlocked = false;
                while (rs.next() && !slotBlocked) {
                    LocalDateTime existStart = rs.getTimestamp("appointment_date").toLocalDateTime();
                    int existDur = rs.getInt("base_duration");
                    LocalDateTime existEnd = existStart.plusMinutes(existDur);
                    if (!slotDT.isBefore(existStart) && slotDT.isBefore(existEnd)) {
                        slotBlocked = true;
                    }
                }
                if (slotBlocked) blocked.add(slot);
            } catch (SQLException e) { e.printStackTrace(); }
        }
        return blocked;
    }

    /**
     * Creates appointment and automatically sends confirmation email.
     */
    public static boolean createAppointment(
            String contactEmail, String patientName,
            LocalDateTime appointmentDate, String procedureType,
            String doctorName) {
        return createAppointment(contactEmail, patientName, appointmentDate,
                procedureType, doctorName, false, null);
    }

    public static boolean createAppointment(
            String contactEmail, String patientName,
            LocalDateTime appointmentDate, String procedureType,
            String doctorName, boolean isFamilyMember, Integer familyMemberId) {

        String sql = "INSERT INTO Appointments " +
                "(patient_name, patient_email, contact_email, contact_name, is_family_member, " +
                "family_member_id, appointment_date, procedure_type, doctor_name, status) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, 'CONFIRMED')";
        try (Connection c = getConnection(); PreparedStatement p = c.prepareStatement(sql)) {
            p.setString(1, patientName);
            if (isFamilyMember) p.setNull(2, Types.VARCHAR);
            else p.setString(2, contactEmail);
            p.setString(3, contactEmail);
            p.setString(4, getPatientNameByEmail(contactEmail));
            p.setBoolean(5, isFamilyMember);
            if (familyMemberId != null) p.setInt(6, familyMemberId);
            else p.setNull(6, Types.INTEGER);
            p.setTimestamp(7, Timestamp.valueOf(appointmentDate));
            p.setString(8, procedureType);
            p.setString(9, doctorName);

            int rows = p.executeUpdate();
            if (rows > 0) {
                // FIX: Send email on a background thread so that:
                //  1. A missing JAR (NoClassDefFoundError) cannot crash the UI thread
                //  2. Email latency cannot delay showing the AppointmentSuccess screen
                final String emailTo   = contactEmail;
                final String pName     = patientName;
                final LocalDateTime dt = appointmentDate;
                final String proc      = procedureType;
                final String doc       = doctorName;
                new Thread(() -> {
                    try {
                        boolean sent = EmailService.sendAppointmentConfirmation(
                                emailTo, pName, dt, proc, doc);
                        if (!sent) System.err.println("Warning: Appointment saved but email failed.");
                    } catch (Throwable t) {
                        // Catches both Exception and Error (e.g. NoClassDefFoundError)
                        System.err.println("Email error (appointment still saved): " + t.getMessage());
                    }
                }, "EmailSender").start();
                return true;  // always return true if DB insert succeeded
            }
            return false;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }

    public static boolean familyMemberHasAppointmentOnDate(int familyMemberId, LocalDate date) {
        String sql = "SELECT COUNT(*) FROM Appointments " +
                "WHERE family_member_id = ? AND DATE(appointment_date) = ? " +
                "AND status != 'CANCELLED'";
        try (Connection c = connect(); PreparedStatement p = c.prepareStatement(sql)) {
            p.setInt(1, familyMemberId);
            p.setDate(2, Date.valueOf(date));
            ResultSet rs = p.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public static boolean patientHasAppointmentOnDate(String contactEmail, LocalDate date) {
        String sql = "SELECT COUNT(*) FROM Appointments " +
                "WHERE contact_email = ? AND DATE(appointment_date) = ? " +
                "AND status != 'CANCELLED'";
        try (Connection c = connect(); PreparedStatement p = c.prepareStatement(sql)) {
            p.setString(1, contactEmail);
            p.setDate(2, Date.valueOf(date));
            ResultSet rs = p.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    /**
     * Returns all appointments for a contact email, ordered by date descending.
     * Returns: {id, patient_name, appointment_date, procedure_type, doctor_name, status}
     */
    public static List<String[]> getAppointmentsByEmail(String contactEmail) {
        List<String[]> list = new ArrayList<>();
        String sql = "SELECT id, patient_name, appointment_date, procedure_type, doctor_name, status " +
                "FROM Appointments WHERE contact_email = ? ORDER BY appointment_date DESC";
        try (Connection c = connect(); PreparedStatement p = c.prepareStatement(sql)) {
            p.setString(1, contactEmail);
            ResultSet rs = p.executeQuery();
            while (rs.next()) list.add(new String[]{
                    String.valueOf(rs.getInt("id")),
                    rs.getString("patient_name"),
                    rs.getString("appointment_date"),
                    rs.getString("procedure_type"),
                    rs.getString("doctor_name"),
                    rs.getString("status")
            });
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    /**
     * Cancels appointment and sends cancellation email.
     */
    public static boolean cancelAppointment(int appointmentId, String contactEmail, String patientName) {
        String sql = "UPDATE Appointments SET status = 'CANCELLED' WHERE id = ? AND contact_email = ?";
        try (Connection c = getConnection(); PreparedStatement p = c.prepareStatement(sql)) {
            p.setInt(1, appointmentId); p.setString(2, contactEmail);
            int rows = p.executeUpdate();
            if (rows > 0) {
                String dateSql = "SELECT appointment_date FROM Appointments WHERE id = ?";
                try (PreparedStatement p2 = c.prepareStatement(dateSql)) {
                    p2.setInt(1, appointmentId);
                    ResultSet rs = p2.executeQuery();
                    if (rs.next()) {
                        LocalDateTime dt = rs.getTimestamp("appointment_date").toLocalDateTime();
                        // Send on background thread so email errors never crash the UI
                        new Thread(() -> {
                            try {
                                EmailService.sendCancellationEmail(contactEmail, patientName, dt);
                            } catch (Throwable t) {
                                System.err.println("Cancellation email error: " + t.getMessage());
                            }
                        }, "EmailSender").start();
                    }
                }
                return true;
            }
            return false;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }

    /**
     * Reschedules an appointment to a new date/time.
     * FIX: now uses correct column `appointment_date`.
     */
    public static boolean rescheduleAppointment(int appointmentId, LocalDateTime newDate) {
        String sql = "UPDATE Appointments SET appointment_date = ?, status = 'CONFIRMED' WHERE id = ?";
        try (Connection c = connect(); PreparedStatement p = c.prepareStatement(sql)) {
            p.setTimestamp(1, Timestamp.valueOf(newDate));
            p.setInt(2, appointmentId);
            return p.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    /**
     * Gets appointments scheduled for tomorrow (for reminder emails).
     * Returns: {contact_email, patient_name, appointment_date, procedure_type, doctor_name}
     */
    public static List<String[]> getAppointmentsTomorrow() {
        List<String[]> list = new ArrayList<>();
        String sql = "SELECT contact_email, patient_name, appointment_date, " +
                "procedure_type, doctor_name " +
                "FROM Appointments " +
                "WHERE DATE(appointment_date) = DATE(NOW() + INTERVAL 1 DAY) " +
                "  AND status = 'CONFIRMED'";
        try (Connection c = connect(); Statement st = c.createStatement()) {
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                // Read as Timestamp, then convert — avoids format ambiguity entirely
                LocalDateTime dt = rs.getTimestamp("appointment_date").toLocalDateTime();
                list.add(new String[]{
                        rs.getString("contact_email"),
                        rs.getString("patient_name"),
                        dt.format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                        rs.getString("procedure_type"),
                        rs.getString("doctor_name")
                });
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }
    /**
     * Returns true if the given doctor has ANY schedule entries at all.
     * Used as a fallback check — if the Dentist_Schedules table is empty,
     * we skip the working-day restriction and show all time slots.
     */
    public static boolean doctorHasAnySchedule(String doctorFullName) {
        String sql =
                "SELECT COUNT(*) FROM Dentist_Schedules ds " +
                        "JOIN Dentist d ON ds.dentist_id = d.dentist_id " +
                        "WHERE CONCAT(d.first_name, IF(d.last_name IS NOT NULL, CONCAT(' ', d.last_name), '')) = ?";
        try (Connection c = connect(); PreparedStatement p = c.prepareStatement(sql)) {
            p.setString(1, doctorFullName);
            ResultSet rs = p.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    /**
     * Returns true if the given doctor has a schedule entry for the day of the week
     * that corresponds to the provided date.
     */
    public static boolean isDoctorWorkingOnDate(String doctorFullName, LocalDate date) {
        String dayName = date.getDayOfWeek()
                .getDisplayName(java.time.format.TextStyle.FULL, java.util.Locale.ENGLISH); // e.g. "Monday"

        String sql =
                "SELECT COUNT(*) FROM Dentist_Schedules ds " +
                        "JOIN Dentist d ON ds.dentist_id = d.dentist_id " +
                        "WHERE CONCAT(d.first_name, IF(d.last_name IS NOT NULL, CONCAT(' ', d.last_name), '')) = ? " +
                        "  AND ds.day_of_week = ?";

        try (Connection c = connect(); PreparedStatement p = c.prepareStatement(sql)) {
            p.setString(1, doctorFullName);
            p.setString(2, dayName);
            ResultSet rs = p.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    /**
     * Updates procedure, patient name, and appointment time for a given appointment.
     * Used by ModifyAppointmentForm.
     * Parameters match the old signature so existing call sites compile unchanged:
     *   email       – contact_email of the account holder
     *   oldTime     – original appointment_date string (yyyy-MM-dd HH:mm:ss)
     *   newTime     – new time as "HH:mm" (date is kept the same)
     *   newProc     – new procedure_type value
     *   newPatient  – new patient_name value
     */
    public static boolean updateAppointmentDetails(
            String email, String oldTime, String newTime, String newProc, String newPatient) {

        // Build the new appointment_date by combining the date part of oldTime with newTime
        String newDatetime;
        try {
            String datePart = oldTime.length() >= 10 ? oldTime.substring(0, 10) : oldTime;
            newDatetime = datePart + " " + newTime + ":00";
        } catch (Exception e) {
            System.err.println("updateAppointmentDetails: could not parse oldTime: " + oldTime);
            return false;
        }

        // Check the new slot is actually available before updating
        try {
            java.time.LocalDateTime newDT = java.time.LocalDateTime.parse(
                    newDatetime, java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            int dur = getProcedureDuration(newProc);

            // Fetch doctor name for the original appointment so we can check availability
            String doctorSql = "SELECT doctor_name FROM Appointments " +
                    "WHERE contact_email = ? AND appointment_date = ?";
            String doctor = null;
            try (Connection c = connect(); PreparedStatement p = c.prepareStatement(doctorSql)) {
                p.setString(1, email);
                p.setString(2, oldTime);
                ResultSet rs = p.executeQuery();
                if (rs.next()) doctor = rs.getString("doctor_name");
            }

            if (doctor != null && !isSlotAvailable(doctor, newDT, dur)) return false;

        } catch (Exception e) {
            // If parsing fails just attempt the update anyway
            System.err.println("Slot check skipped: " + e.getMessage());
        }

        String sql = "UPDATE Appointments " +
                "SET appointment_date = ?, procedure_type = ?, patient_name = ? " +
                "WHERE contact_email = ? AND appointment_date = ?";
        try (Connection c = connect(); PreparedStatement p = c.prepareStatement(sql)) {
            p.setString(1, newDatetime);
            p.setString(2, newProc);
            p.setString(3, newPatient);
            p.setString(4, email);
            p.setString(5, oldTime);
            return p.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }


}