import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class ReminderService {

    //Constantly checking for the status of appointments every hour:
    private static final long CHECK_INTERVAL_MS = 60L * 60L * 1000L;


    public static void start() {
        Timer timer = new Timer("ReminderService", true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                sendReminders();
            }
        }, 0, CHECK_INTERVAL_MS);

        System.out.println("ReminderService started – checks hourly for tomorrow's appointments.");
    }
    //Retreiving the appointments that are happening exactly on the next day:
    private static void sendReminders() {
        System.out.println("ReminderService: Running check at " + java.time.LocalDateTime.now());

        List<String[]> appointments = DatabaseHelper.getAppointmentsTomorrow();

        System.out.println("ReminderService: Found " + appointments.size() + " appointment(s) for tomorrow.");

        if (appointments.isEmpty()) return;
//Identifying the elements required to send the reminder:
        for (String[] appt : appointments) {
            String contactEmail  = appt[0];
            String patientName   = appt[1];
            String dateStr       = appt[2];
            String procedureType = appt[3];

            System.out.println("ReminderService: Processing reminder for " + patientName
                    + " | date string from DB: [" + dateStr + "]");

            try {

                java.time.LocalDateTime appointmentDate;
                try {
                    appointmentDate = java.time.LocalDateTime.parse(
                            dateStr, java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                } catch (Exception e1) {

                    appointmentDate = java.time.LocalDateTime.parse(
                            dateStr.contains(".")
                                    ? dateStr.substring(0, dateStr.indexOf("."))
                                    : dateStr,
                            java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                }

                System.out.println("ReminderService: Parsed date OK — " + appointmentDate);
                System.out.println("ReminderService: Sending email to " + contactEmail + "...");
                //Confirming that the reminder was successfully sent to the patient:
                boolean sent = EmailService.sendAppointmentReminder(
                        contactEmail, patientName, appointmentDate, procedureType);

                if (sent) {
                    System.out.println("ReminderService: ✅ Reminder sent to " + contactEmail);
                } else {
                    System.err.println("ReminderService: ❌ Email method returned false for " + contactEmail);
                }

            } catch (Exception e) {
                System.err.println("ReminderService: ❌ Error processing reminder for "
                        + patientName + ": " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}



