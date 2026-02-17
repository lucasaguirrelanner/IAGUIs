import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.io.FileInputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

/**
 * EmailService – FIXED to load credentials from config.properties
 * instead of environment variables, which are often not set in IntelliJ.
 *
 * Add these two lines to your config.properties file:
 *   clinic.email=youraddress@gmail.com
 *   clinic.password=xxxx xxxx xxxx xxxx   (16-char Gmail App Password)
 *
 * IMPORTANT – Gmail requires an App Password, NOT your normal Gmail password.
 * Steps to get one:
 *   1. Go to myaccount.google.com → Security
 *   2. Enable 2-Step Verification (required first)
 *   3. Search "App Passwords" in the Google Account search bar
 *   4. Create one → App = Mail, Device = Windows Computer
 *   5. Copy the 16-character code into config.properties (no spaces needed)
 */
public class EmailService {

    private static final String SMTP_HOST   = "smtp.gmail.com";
    private static final String SMTP_PORT   = "587";
    private static final String CLINIC_NAME = "Duperly & Lanner Grupo Dental";

    // Loaded once from config.properties
    private static final String SENDER_EMAIL;
    private static final String SENDER_PASSWORD;

    static {
        String email = null, password = null;
        try (FileInputStream fis = new FileInputStream("config.properties")) {
            Properties prop = new Properties();
            prop.load(fis);
            email    = prop.getProperty("clinic.email");
            password = prop.getProperty("clinic.password");
        } catch (Exception e) {
            System.err.println("EmailService: could not load config.properties – " + e.getMessage());
        }
        SENDER_EMAIL    = email;
        SENDER_PASSWORD = password;
    }

    // =========================================================
    // PUBLIC API
    // =========================================================

    public static boolean sendAppointmentConfirmation(
            String patientEmail, String patientName,
            LocalDateTime appointmentDate, String procedureType, String doctorName) {
        try {
            String formatted = appointmentDate.format(
                    DateTimeFormatter.ofPattern("EEEE, MMMM dd, yyyy 'at' hh:mm a"));
            return sendEmail(patientEmail,
                    "Appointment Confirmed – " + CLINIC_NAME,
                    buildConfirmationEmail(patientName, formatted, procedureType, doctorName));
        } catch (Exception e) {
            System.err.println("Error sending confirmation: " + e.getMessage());
            return false;
        }
    }

    public static boolean sendAppointmentReminder(
            String patientEmail, String patientName,
            LocalDateTime appointmentDate, String procedureType) {
        try {
            String formatted = appointmentDate.format(
                    DateTimeFormatter.ofPattern("EEEE, MMMM dd 'at' hh:mm a"));
            return sendEmail(patientEmail,
                    "Reminder: Your Appointment Tomorrow – " + CLINIC_NAME,
                    buildReminderEmail(patientName, formatted, procedureType));
        } catch (Exception e) {
            System.err.println("Error sending reminder: " + e.getMessage());
            return false;
        }
    }

    public static boolean sendCancellationEmail(
            String patientEmail, String patientName, LocalDateTime appointmentDate) {
        try {
            String formatted = appointmentDate.format(
                    DateTimeFormatter.ofPattern("EEEE, MMMM dd, yyyy 'at' hh:mm a"));
            String body = String.format("""
                <html><body style="font-family:Arial,sans-serif;">
                  <div style="max-width:600px;margin:0 auto;padding:20px;">
                    <h2 style="color:#e74c3c;">Appointment Cancelled</h2>
                    <p>Hello %s,</p>
                    <p>Your appointment on <strong>%s</strong> has been cancelled.</p>
                    <p>Please contact us to reschedule at your convenience.</p>
                    <p>Thank you,<br>%s</p>
                  </div>
                </body></html>""", patientName, formatted, CLINIC_NAME);
            return sendEmail(patientEmail, "Appointment Cancelled – " + CLINIC_NAME, body);
        } catch (Exception e) {
            System.err.println("Error sending cancellation: " + e.getMessage());
            return false;
        }
    }

    // =========================================================
    // PRIVATE HELPERS
    // =========================================================

    private static boolean sendEmail(String recipientEmail, String subject, String htmlBody) {
        if (SENDER_EMAIL == null || SENDER_PASSWORD == null) {
            System.err.println("EmailService ERROR: clinic.email or clinic.password " +
                    "missing from config.properties");
            return false;
        }

        Properties props = new Properties();
        props.put("mail.smtp.auth",            "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host",            SMTP_HOST);
        props.put("mail.smtp.port",            SMTP_PORT);
        props.put("mail.smtp.ssl.trust",       SMTP_HOST);
        props.put("mail.smtp.ssl.protocols",   "TLSv1.2");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SENDER_EMAIL, SENDER_PASSWORD);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(SENDER_EMAIL, CLINIC_NAME));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject(subject);
            message.setContent(htmlBody, "text/html; charset=utf-8");
            Transport.send(message);
            System.out.println("✅ Email sent to: " + recipientEmail);
            return true;
        } catch (Exception e) {
            System.err.println("❌ Failed to send email to " + recipientEmail + ": " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private static String buildConfirmationEmail(
            String name, String dateTime, String procedure, String doctor) {
        return String.format("""
            <!DOCTYPE html><html><head><style>
              body{font-family:Arial,sans-serif;color:#333;}
              .container{max-width:600px;margin:0 auto;padding:20px;}
              .header{background:linear-gradient(135deg,#1e1e3c,#e47a32);color:white;
                      padding:30px;text-align:center;border-radius:10px 10px 0 0;}
              .content{background:#f9f9f9;padding:30px;border:1px solid #ddd;}
              .details{background:white;padding:20px;border-left:4px solid #e47a32;margin:20px 0;}
              .footer{background:#1e1e3c;color:white;padding:20px;
                      text-align:center;border-radius:0 0 10px 10px;font-size:.9em;}
              .label{font-weight:bold;color:#666;}
            </style></head><body>
            <div class="container">
              <div class="header"><h1>🦷 %s</h1><p>Appointment Confirmed</p></div>
              <div class="content">
                <h2>Hello %s,</h2>
                <p>Your appointment has been successfully scheduled!</p>
                <div class="details">
                  <p><span class="label">📅 Date &amp; Time:</span> %s</p>
                  <p><span class="label">🔬 Procedure:</span> %s</p>
                  <p><span class="label">👨‍⚕️ Doctor:</span> Dr. %s</p>
                </div>
                <p><strong>⚠️ Reminders:</strong></p>
                <ul>
                  <li>Please arrive 15 minutes early</li>
                  <li>Bring your ID and insurance card</li>
                  <li>To reschedule, please contact us at least 24 hours in advance</li>
                </ul>
              </div>
              <div class="footer">
                <p><strong>%s</strong></p>
                <p>📞 (555) 123-4567 | 📧 clinic@duperlyandlanner.com</p>
              </div>
            </div></body></html>""",
                CLINIC_NAME, name, dateTime, procedure, doctor, CLINIC_NAME);
    }

    private static String buildReminderEmail(String name, String dateTime, String procedure) {
        return String.format("""
            <html><body style="font-family:Arial,sans-serif;">
            <div style="max-width:600px;margin:0 auto;padding:20px;">
              <h2 style="color:#e47a32;">⏰ Appointment Reminder</h2>
              <p>Hello %s,</p>
              <p>This is a friendly reminder about your appointment <strong>tomorrow</strong>:</p>
              <div style="background:#f0f0f0;padding:15px;border-left:4px solid #e47a32;margin:20px 0;">
                <strong>When:</strong> %s<br>
                <strong>Procedure:</strong> %s
              </div>
              <p>We look forward to seeing you!</p>
              <p style="color:#666;font-size:.9em;">
                If you need to reschedule, please call us as soon as possible.</p>
            </div></body></html>""", name, dateTime, procedure);
    }
}