import javax.swing.*;

public class SelectionModify extends JFrame {
    public SelectionModify(String email) {
        setTitle("Select Appointment to Modify");
        setSize(400, 300);
        setLocationRelativeTo(null);
        // Add a simple button as a placeholder for now
        JButton btn = new JButton("Modify Next Appointment");
        btn.addActionListener(e -> new ModifyAppointmentForm(email, "10:00", "Patient", "Cleaning"));
        add(btn);
        setVisible(true);
    }
}