import com.aspose.email.MailAddressCollection;
import com.aspose.email.MailMessage;
import com.aspose.email.SecurityOptions;
import com.aspose.email.SmtpClient;


public class EmailHelper {
    public static void sendEmail(String to, String patient, String procedure, String time) {
        MailMessage message = new MailMessage();
        message.setTo(MailAddressCollection.to_MailAddressCollection(to));
        message.setSubject("Appointment Confirmed - Duperly & Lanner");

        String body = "<html><body style='font-family: Arial; color: #333;'>" +
                "<div style='background: #1E1E3C; padding: 20px; text-align: center;'>" +
                "<h1 style='color: #E47A32;'>Duperly & Lanner</h1>" +
                "</div>" +
                "<h3>Appointment Details</h3>" +
                "<p><b>Patient:</b> " + patient + "</p>" +
                "<p><b>Procedure:</b> " + procedure + "</p>" +
                "<p><b>Time:</b> " + time + "</p>" +
                "<p style='color: #777;'>Please arrive 10 minutes early. If you need to cancel, do so via the app.</p>" +
                "</body></html>";

        message.setHtmlBody(body);

        // Update with your actual credentials for testing
        SmtpClient client = new SmtpClient("smtp.gmail.com", 587, "your-email@gmail.com", "your-app-password");
        client.setSecurityOptions(SecurityOptions.Auto);

        try {
            client.send(message);
        } catch (Exception e) {
            System.err.println("Email Error: " + e.getMessage());

        }

    }

}
