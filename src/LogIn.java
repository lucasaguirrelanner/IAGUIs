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

        //Users will be able to reestablish their password by verifying that they are the actual user with their email address:
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
        LoginButton.addActionListener(e -> {
            String user = txt_user.getText().trim();
            String pass = new String(txt_password.getPassword());
//Users can only log in as long as their credentials are fully written, and these coincide with what is stored in the DB:
            if (user.isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter both username and password.");
                return;
            }

            if (DatabaseHelper.validateLogin(user, pass)) {

                String email = DatabaseHelper.getEmailByUsername(user);

                if (email == null) {
                    JOptionPane.showMessageDialog(this, "Error: Profile data missing.");
                    return;
                }


                String name = DatabaseHelper.getFirstName(email);

                JOptionPane.showMessageDialog(this, "Welcome back, " + name + "!");

                //If login is successful, users will be taken to their account's dashboard:

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
