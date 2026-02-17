import javax.swing.*;
import java.awt.*;

public class CrearCuenta1 extends JFrame {
    private JPanel MainPanel;
    private JButton Continue, ResetButton, GoBackButton;
    private JComboBox<String> combo_famacc;
    private JTextField firstnameField1, lastnameField1, mailField1;
    private JLabel firstLabel;
    private JLabel lastLabel;
    private JLabel mailLabel;
    private JLabel famaccLabel;


    public CrearCuenta1() {
        // Branding (Criterion #10)


        setContentPane(MainPanel);
        setTitle("Duperly & Lanner - Step 1");
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
            String fName = firstnameField1.getText().trim();
            String lName = lastnameField1.getText().trim();
            String email = mailField1.getText().trim();
            // Get the family account choice
            // Inside the Continue listener in CrearCuenta1:
            boolean isFamily = combo_famacc.getSelectedItem().equals("Yes");
            new CrearCuenta2(fName, lName, email, isFamily);

            if (fName.isEmpty() || lName.isEmpty() || email.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields.");
                return;
            }

            if (DatabaseHelper.emailExists(email)) {
                JOptionPane.showMessageDialog(this, "This email is already registered.");
            } else {
                // SAVING PARTIAL DATA (Criterion #1)
                boolean saved = DatabaseHelper.saveInitialPatient(fName, lName, email);

                if (saved) {
                    // MATCHING THE NEXT FORM:
                    // Make sure CrearCuenta2 is ready to receive these 4 things!
                    new CrearCuenta2(fName, lName, email, isFamily);
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
