import javax.swing.*;
import java.awt.*;

public class Restablishpassword extends JFrame {
    private JPanel MainPanel;
    private JPasswordField passwordField1;
    private JPasswordField passwordField2;
    private JButton Regresar;
    private JButton ConfirmButton;
    private JButton Resetbutton;

    public Restablishpassword(String emailToReset) {


        setContentPane(MainPanel);
        setTitle("Duperly & Lanner - Reset Password");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(800, 700);
        setLocationRelativeTo(null);

        // RESET LOGIC
        Resetbutton.addActionListener(e -> {
            passwordField1.setText("");
            passwordField2.setText("");
        });

        // BACK LOGIC
        Regresar.addActionListener(e -> dispose());

        // CONFIRM/UPDATE LOGIC
        ConfirmButton.addActionListener(e -> {
            String pw = new String(passwordField1.getPassword());
            String vpw = new String(passwordField2.getPassword());

            // 1. Check for empty fields
            if (pw.isEmpty() || vpw.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in both fields.");
                return;
            }

            // 2. Matching Check
            if (!pw.equals(vpw)) {
                JOptionPane.showMessageDialog(this, "Passwords do not match.");
                passwordField1.setText("");
                passwordField2.setText("");
                return;
            }

            // 3. Complexity Check (Using Helper for detailed feedback)
            if (!PASSWORDHELPER.isPasswordComplex(pw)) {
                JOptionPane.showMessageDialog(this, "ERROR: " + PASSWORDHELPER.getPasswordRequirements(pw));
                return;
            }

            // 4. Database Update
            if (DatabaseHelper.updatePassword(emailToReset, pw)) {
                JOptionPane.showMessageDialog(this, "Success! Your password has been updated.");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "System Error: Could not update password at this time.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        setVisible(true);
    }
}