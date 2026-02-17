import javax.swing.*;
import java.awt.*;

public class ModifyAppointmentForm extends JFrame {
    private JPanel MainPanel;
    private JComboBox<String> patientCombo;
    private JComboBox<String> procedureCombo;
    private JComboBox<String> timeCombo;
    private JButton confirmBtn;
    private JButton cancelBtn;

    private String userEmail;
    private String originalTime;

    public ModifyAppointmentForm(String email, String oldTime, String oldPatient, String oldProc) {
        this.userEmail = email;
        this.originalTime = oldTime;

        // --- UI Setup ---
        setContentPane(MainPanel);
        setTitle("Modify Appointment - Duperly & Lanner");
        setSize(600, 500);
        setLocationRelativeTo(null);
        MainPanel.setBackground(new Color(30, 30, 60)); // Navy Blue

        // Style the button
        confirmBtn.setBackground(new Color(228, 122, 50)); // Orange
        confirmBtn.setForeground(Color.WHITE);

        // --- Data Population ---
        // Pre-selecting the current appointment details
        patientCombo.setSelectedItem(oldPatient);
        procedureCombo.setSelectedItem(oldProc);
        timeCombo.setSelectedItem(oldTime);

        // --- Logic ---
        confirmBtn.addActionListener(e -> {
            String newTime = (String) timeCombo.getSelectedItem();
            String newProc = (String) procedureCombo.getSelectedItem();
            String newPatient = (String) patientCombo.getSelectedItem();

            // 1. Validation: Check if anything actually changed
            if (newTime.equals(oldTime) && newProc.equals(oldProc) && newPatient.equals(oldPatient)) {
                JOptionPane.showMessageDialog(this, "No changes were made.");
                return;
            }

            // 2. Database Update
            // This method in DatabaseHelper (which we refined earlier)
            // will check for overlaps before returning true/false
            boolean success = DatabaseHelper.updateAppointmentDetails(
                    userEmail, originalTime, newTime, newProc, newPatient
            );

            if (success) {
                JOptionPane.showMessageDialog(this,
                        "Appointment updated successfully!\nA confirmation email has been sent.",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Error: The selected time slot is already taken or invalid.",
                        "Overlap Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelBtn.addActionListener(e -> dispose());

        setVisible(true);
    }
}