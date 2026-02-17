import javax.swing.*;
import java.awt.*;

public class LogIn extends JFrame {
    private JPanel MainPanel;
    private JButton GoBackButton, LoginButton, ForgotMyButton;
    private JPasswordField txt_password;
    private JTextField txt_user;
    private JLabel txt_login, txt_username, txt_pw;

    public LogIn() {


        setContentPane(MainPanel);
        setTitle("Duperly & Lanner - Login");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(800, 700);
        setLocationRelativeTo(null);

        // FORGOT PASSWORD LOGIC
        ForgotMyButton.addActionListener(e -> {
            String emailToReset = JOptionPane.showInputDialog(this, "Enter your registered email address:");
            if (emailToReset != null && !emailToReset.trim().isEmpty()) {
                if (DatabaseHelper.emailExists(emailToReset)) {
                    new Restablishpassword(emailToReset);
                } else {
                    JOptionPane.showMessageDialog(this, "Email not found in our records.");
                }
            }
        });

        // LOGIN LOGIC
        LoginButton.addActionListener(e -> {
            String user = txt_user.getText().trim();
            String pass = new String(txt_password.getPassword());

            if (user.isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter both username and password.");
                return;
            }

            if (DatabaseHelper.validateLogin(user, pass)) {
                // 1. Convert username to email first (Primary Key for the rest of the app)
                String email = DatabaseHelper.getEmailByUsername(user);

                if (email == null) {
                    JOptionPane.showMessageDialog(this, "Error: Profile data missing.");
                    return;
                }

                // 2. Fetch First Name using the EMAIL
                String name = DatabaseHelper.getFirstName(email);

                JOptionPane.showMessageDialog(this, "Welcome back, " + name + "!");

                // Proceed to Dashboard
                new Account(email, name).setVisible(true);
                dispose();

            } else {
                JOptionPane.showMessageDialog(this, "Invalid Username or Password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
                txt_password.setText("");
            }
        });

        GoBackButton.addActionListener(e -> {
            new MenuForm();
            dispose();
        });

        setVisible(true);


    }
}