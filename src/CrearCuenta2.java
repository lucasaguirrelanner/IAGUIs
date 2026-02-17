import javax.swing.*;
import java.awt.*;

public class CrearCuenta2 (String fName, String lName,String email, boolean isFamilyAccount)extends JFrame {
    private JPanel AccSetUp2;
    private JTextField txt_username;
    private JPasswordField password, passwordVF;
    private JButton resetButton, createAccountButton, goBackButton;
    private String fName, lName, email;
    private boolean isFamily; // Added to store the choice from Step 1

    // Updated constructor to accept 4 arguments
    public CrearCuenta2(String fName, String lName, String email, boolean isFamilyAccount) {
        this.fName = fName;
        this.lName = lName;
        this.email = email;
        this.isFamily = isFamilyAccount;

        setContentPane(AccSetUp2);
        setTitle("Duperly & Lanner - Set Credentials");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(800, 700);
        setLocationRelativeTo(null);

        createAccountButton.addActionListener(e -> {
            String user = txt_username.getText().trim();
            String pw = new String(password.getPassword());
            String vpw = new String(passwordVF.getPassword());

            if (user.isEmpty() || pw.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields.");
                return;
            }

            // Validation Helpers (Ensures these external classes are present in your project)
            if (!USERNAME_VALIDATION_HELPER.isUsernameValid(user)) {
                JOptionPane.showMessageDialog(this, USERNAME_VALIDATION_HELPER.getUsernameRequirements(user));
                return;
            }
            if (USERNAME_VALIDATION_HELPER.isUsernameTaken(user)) {
                JOptionPane.showMessageDialog(this, "Username already taken.");
                return;
            }

            if (!pw.equals(vpw)) {
                JOptionPane.showMessageDialog(this, "Passwords do not match.");
                return;
            }
            if (!PASSWORDHELPER.isPasswordComplex(pw)) {
                JOptionPane.showMessageDialog(this, PASSWORDHELPER.getPasswordRequirements(pw));
                return;
            }

            // 4. Finalize Account Update
            if (DatabaseHelper.RegisterFullAccount(email, user, pw)) {
                JOptionPane.showMessageDialog(this, "ACCOUNT SUCCESSFULLY CREATED!");

                // RESTORING PROGRESS: Check if they wanted a family account
                if (isFamilyAccount) {
                    // This leads to the family member addition form we worked on
                    new FamilyAccount(email);
                } else {
                    new LogIn();
                }
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Error saving credentials to database.");
            }
        });

        resetButton.addActionListener(e -> {
            txt_username.setText("");
            password.setText("");
            passwordVF.setText("");
        });

        goBackButton.addActionListener(e -> {
            new CrearCuenta1();
            dispose();
        });

        setVisible(true);
    }
}