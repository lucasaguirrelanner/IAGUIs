import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Account extends JFrame {
    private JPanel MainPanel;
    private JButton bookappointment;
    private JButton viewYourAppointmentsButton;
    private JButton modifyMyAccountButton;
    private JButton gobackbutton;
    private JButton changeAnAppointmentButton;
    private JButton exitButton;
    private JLabel welcomeLabel;

    private String userEmail;
    private String userName;

    public Account(String email, String name) {
        this.userEmail = email;
        this.userName = name;

        // --- Branding (Criterion #10) ---
        MainPanel.setBackground(new Color(30, 30, 60));
        welcomeLabel.setForeground(new Color(228, 122, 50)); // Orange
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        welcomeLabel.setText("WELCOME, " + name.toUpperCase());


        setContentPane(MainPanel);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 700);
        setLocationRelativeTo(null);

        // --- Action Listeners ---

        // Logic for branching: Family vs Individual (Criterion #1)
        bookappointment.addActionListener(e -> {
            if (DatabaseHelper.hasFamilyMembers(userEmail)) {
                new FAMAPPOINTMENT(email);
            } else {
                new APPOINTMENT(userEmail);
            }
            dispose();
        });

        modifyMyAccountButton.addActionListener(e -> new ModifyAccount(userEmail));

        viewYourAppointmentsButton.addActionListener(e -> {
            // Displays the JTable with history and notes (Criterion #8)
            new HistoryView(userEmail);
        });

        changeAnAppointmentButton.addActionListener(e -> {
            // Redirects to the modification selection screen
            new SelectionModify(userEmail);
        });

        gobackbutton.addActionListener(e -> {
            new MenuForm();
            dispose();
        });

        exitButton.addActionListener(e -> System.exit(0));

        setVisible(true);

    }
}