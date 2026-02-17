import javax.swing.*;

/**
 * Step 1 of account creation: name, email, and family account choice.
 * FIX: Removed the duplicate `new CreateAccount2(...)` call that appeared
 *      before the validation checks.
 */
public class CreateAccount1 extends JFrame {
    private JPanel MainPanel;
    private JButton Continue, ResetButton, GoBackButton;
    private JComboBox<String> combo_famacc;
    private JTextField firstnameField1, lastnameField1, mailField1;
    private JLabel firstLabel, lastLabel, mailLabel, famaccLabel;

    public CreateAccount1() {
        setContentPane(MainPanel);
        setTitle("Duperly & Lanner Grupo Dental – Step 1: Your Details");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(800, 700);
        setLocationRelativeTo(null);

        combo_famacc.addItem("Yes");
        combo_famacc.addItem("No");

        ResetButton.addActionListener(e -> {
            firstnameField1.setText("");
            lastnameField1.setText("");
            mailField1.setText("");
            combo_famacc.setSelectedItem("Yes");
        });

        Continue.addActionListener(e -> {
            String fName   = firstnameField1.getText().trim();
            String lName   = lastnameField1.getText().trim();
            String email   = mailField1.getText().trim();
            boolean isFamily = "Yes".equals(combo_famacc.getSelectedItem());

            // --- Validation FIRST ---
            if (fName.isEmpty() || lName.isEmpty() || email.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields.");
                return;
            }

            if (!email.contains("@") || !email.contains(".")) {
                JOptionPane.showMessageDialog(this, "Please enter a valid email address.");
                return;
            }

            if (DatabaseHelper.emailExists(email)) {
                JOptionPane.showMessageDialog(this, "This email is already registered.");
                return;
            }

            // --- Save to DB ---
            boolean saved = DatabaseHelper.saveInitialPatient(fName, lName, email);
            if (saved) {
                // FIX: Only one call to CreateAccount2
                new CreateAccount2(fName, lName, email, isFamily);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Error saving data. Please try again.");
            }
        });

        GoBackButton.addActionListener(e -> {
            new MenuForm();
            dispose();
        });

        setVisible(true);
    }
}
