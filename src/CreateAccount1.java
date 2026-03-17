import javax.swing.*;

public class CreateAccount1 extends JFrame {
    private JPanel MainPanel;
    private JButton Continue, ResetButton, GoBackButton;
    private JComboBox<String> combo_famacc;
    private JTextField firstnameField1, lastnameField1, mailField1;
    private JLabel firstLabel;
    private JLabel lastLabel;
    private JLabel mailLabel;
    private JLabel famaccLabel;


    public CreateAccount1() {

//Display menu for the user to select options wheb creating their account:
        setContentPane(MainPanel);
        setTitle("Duperly & Lanner - Step 1");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(800, 700);
        setLocationRelativeTo(null);

//These combo boxes allow the user to create a family or personal account:
        combo_famacc.addItem("Yes");
        combo_famacc.addItem("No");

        ResetButton.addActionListener(e -> {
            firstnameField1.setText("");
            lastnameField1.setText("");
            mailField1.setText("");
            combo_famacc.setSelectedItem("Yes");
        });

        Continue.addActionListener(e -> {
            String fName = firstnameField1.getText().trim();
            String lName = lastnameField1.getText().trim();
            String email = mailField1.getText().trim();


            boolean isFamily = combo_famacc.getSelectedItem().equals("Yes");
            new CreateAccount2(fName, lName, email, isFamily);

            if (fName.isEmpty() || lName.isEmpty() || email.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields.");
                return;
            }

//No duplicate emails:
            if (DatabaseHelper.emailExists(email)) {
                JOptionPane.showMessageDialog(this, "This email is already registered.");
            } else {

                boolean saved = DatabaseHelper.saveInitialPatient(fName, lName, email);

                if (saved) {

                    new CreateAccount2(fName, lName, email, isFamily);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Error saving data to database.");
                }
            }
        });

        GoBackButton.addActionListener(e -> {
            new MenuForm();
            dispose();
        });

        setVisible(true);
    }

}
