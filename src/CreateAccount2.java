import javax.swing.*;

public class CreateAccount2 (String fName, String lName,String email, boolean isFamilyAccount)extends JFrame {
    private JPanel AccSetUp2;
    private JTextField txt_username;
    private JPasswordField password, passwordVF;
    private JButton resetButton, createAccountButton, goBackButton;
    private String fName, lName, email;
    private boolean isFamily;

    //Second step towards creating an account, by crrating a new username and a password:
    public CreateAccount2(String fName, String lName, String email, boolean isFamilyAccount) {
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
//Usernames must be complete:
            if (user.isEmpty() || pw.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields.");
                return;
            }

            //No invsalid username, must follow formatting requirements:
            if (!USERNAME_VALIDATION_HELPER.isUsernameValid(user)) {
                JOptionPane.showMessageDialog(this, USERNAME_VALIDATION_HELPER.getUsernameRequirements(user));
                return;
            }
//No duplicate usernames:
            if (USERNAME_VALIDATION_HELPER.isUsernameTaken(user)) {
                JOptionPane.showMessageDialog(this, "Username already taken.");
                return;
            }
//the confirmed password must match the first password:
            if (!pw.equals(vpw)) {
                JOptionPane.showMessageDialog(this, "Passwords do not match.");
                return;
            }

//Password must follow complexity requirements:

            if (!PASSWORDHELPER.isPasswordComplex(pw)) {
                JOptionPane.showMessageDialog(this, PASSWORDHELPER.getPasswordRequirements(pw));
                return;
            }

            //if all credentials are valid, the account is officially registered:
            if (DatabaseHelper.RegisterFullAccount(email, user, pw)) {
                JOptionPane.showMessageDialog(this, "ACCOUNT SUCCESSFULLY CREATED!");

                //Users that have chosen to create a family account will be sent to a menu in which they can add information about their family members:

                if (isFamilyAccount) {

                    new FamilyAccount(fName, lName, email);
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
            new CreateAccount1();
            dispose();
        });

        setVisible(true);
    }
}
