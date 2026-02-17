import javax.swing.*;
import java.awt.*;

public class APPOINTMENT extends JFrame {
    private JPanel APPPanel;
    private JComboBox<String> ProcedureCombo;
    private JComboBox<String> timeComboBox;
    private JButton CheckButton, ConfirmButton, BackButton;
    private JLabel welcomeLabel;
    private String userEmail;

    public APPOINTMENT(String email) {
        this.userEmail = email;

        setContentPane(APPPanel);
        setTitle("Duperly & Lanner - Individual Booking");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(800, 700);
        setLocationRelativeTo(null);

        // Setup Procedures
        ProcedureCombo.removeAllItems();
        ProcedureCombo.addItem("Diagnostic (15 min)");
        ProcedureCombo.addItem("Oral Rehabilitation (30 min)");
        ProcedureCombo.addItem("Surgery: Wisdom Teeth (120 min)");
        ProcedureCombo.addItem("Surgery: Implant (120 min)");
        ProcedureCombo.addItem("Surgery: Periodontal (90 min)");

        // Setup Time
        String[] hours = {"08:00", "09:00", "10:00", "11:00", "13:00", "14:00", "15:00", "16:00"};
        for (String hour : hours) timeComboBox.addItem(hour);

        CheckButton.addActionListener(e -> {
            String selectedTime = (String) timeComboBox.getSelectedItem();
            String procedure = (String) ProcedureCombo.getSelectedItem();
            int duration = getDurationInMinutes(procedure);
            String endTime = calculateEndTime(selectedTime, duration);

            if (DatabaseHelper.isSlotAvailable(selectedTime, endTime)) {
                JOptionPane.showMessageDialog(this, "Slot available! Finishes at: " + endTime);
            } else {
                JOptionPane.showMessageDialog(this, "This slot overlaps with another appointment.");
            }
        });

        ConfirmButton.addActionListener(e -> {
            String patientName = DatabaseHelper.getFirstName(userEmail);
            String procedure = (String) ProcedureCombo.getSelectedItem();
            String time = (String) timeComboBox.getSelectedItem();

            if (DatabaseHelper.bookAppointment(userEmail, patientName, procedure, time)) {
                JOptionPane.showMessageDialog(this, "Success! Appointment booked.");
                // Ensure EmailHelper is in its own file before calling this
                EmailHelper.sendEmail(userEmail, patientName, procedure, time);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Error: Could not save appointment.");
            }
        });

        BackButton.addActionListener(e -> {
            // Passing BOTH arguments to keep the Account dashboard functional
            String name = DatabaseHelper.getFirstName(userEmail);
            new Account(userEmail, name);
            dispose();
        });

        setVisible(true);
    }

    private int getDurationInMinutes(String procedure) {
        if (procedure.contains("Wisdom Teeth") || procedure.contains("Implant")) return 120;
        if (procedure.contains("Periodontal")) return 90;
        if (procedure.contains("Oral Rehabilitation")) return 30;
        return 15;
    }

    private String calculateEndTime(String start, int duration) {
        String[] parts = start.split(":");
        int total = (Integer.parseInt(parts[0]) * 60) + Integer.parseInt(parts[1]) + duration;
        return String.format("%02d:%02d", total / 60, total % 60);
    }
}




