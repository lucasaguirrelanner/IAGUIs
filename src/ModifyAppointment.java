import javax.swing.*;
import java.awt.*;

public class ModifyAppointment extends JFrame {
    private JPanel MainPanel;
    private JComboBox<String> patientCombo;
    private JComboBox<String> procedureCombo;
    private JComboBox<String> timeCombo;
    private JButton confirmBtn;
    private JButton cancelBtn;

    private String userEmail;
    private String originalTime;

    public ModifyAppointment(String email, String oldTime, String oldPatient, String oldProc) {
        this.userEmail = email;
        this.originalTime = oldTime;

        setContentPane(MainPanel);
        setTitle("Modify Appointment - Duperly & Lanner");
        setSize(600, 500);
        setLocationRelativeTo(null);
        MainPanel.setBackground(new Color(30, 30, 60));
        confirmBtn.setBackground(new Color(228, 122, 50));
        confirmBtn.setForeground(Color.BLACK);

        //Options that appear through combo boxes:
        patientCombo.setSelectedItem(oldPatient);
        procedureCombo.setSelectedItem(oldProc);
        timeCombo.setSelectedItem(oldTime);


        confirmBtn.addActionListener(e -> {
            String newTime = (String) timeCombo.getSelectedItem();
            String newProc = (String) procedureCombo.getSelectedItem();
            String newPatient = (String) patientCombo.getSelectedItem();

            //This ensures that the user actually changes the time of the procedure:
            if (newTime.equals(oldTime) && newProc.equals(oldProc) && newPatient.equals(oldPatient)) {
                JOptionPane.showMessageDialog(this, "No changes were made.");
                return;
            }

            //DB is updated:

            boolean success = DatabaseHelper.updateAppointmentDetails(
                    userEmail, originalTime, newTime, newProc, newPatient
            );

            //Confirmation to the user that their appointment was successfully changed:
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
