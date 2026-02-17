import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class Account extends JFrame {

    private final String userEmail;
    private final String userName;

    // Exact field names preserved from the original .form file
    private JPanel  MainPanel;
    private JButton gobackbutton;
    private JButton bookappointment;
    private JButton changeAnAppointmentButton;
    private JButton viewYourAppointmentsButton;
    private JButton modifyMyAccountButton;
    private JLabel  welcomeLabel;

    public Account(String email, String name) {
        this.userEmail = email;
        this.userName  = name;

        buildUI();

        setTitle("Duperly & Lanner Grupo Dental");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 700);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void buildUI() {
        // ── Root ──────────────────────────────────────────────────────────
        MainPanel = new JPanel(new BorderLayout(0, 0));
        MainPanel.setBackground(new Color(30, 30, 60));

        // ── Welcome label (top-left, matching screenshot) ─────────────────
        welcomeLabel = new JLabel("WELCOME, " + userName.toUpperCase());
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 32));
        welcomeLabel.setForeground(new Color(228, 122, 50));
        welcomeLabel.setBorder(new EmptyBorder(40, 30, 30, 30));
        MainPanel.add(welcomeLabel, BorderLayout.NORTH);

        // ── 2x2 button grid (centre) ──────────────────────────────────────
        JPanel grid = new JPanel(new GridLayout(2, 2, 30, 30));
        grid.setBackground(new Color(30, 30, 60));
        grid.setBorder(new EmptyBorder(20, 30, 20, 30));

        bookappointment            = outlineButton("Book an Appointment");
        changeAnAppointmentButton  = outlineButton("Change an Appointment");
        viewYourAppointmentsButton = outlineButton("View your appointments");
        modifyMyAccountButton      = outlineButton("Modify my account");

        grid.add(bookappointment);
        grid.add(changeAnAppointmentButton);
        grid.add(viewYourAppointmentsButton);
        grid.add(modifyMyAccountButton);

        MainPanel.add(grid, BorderLayout.CENTER);

        // ── Go Back button (full-width, bottom) ───────────────────────────
        gobackbutton = outlineButton("Go Back");
        JPanel south = new JPanel(new BorderLayout());
        south.setBackground(new Color(30, 30, 60));
        south.setBorder(new EmptyBorder(0, 30, 40, 30));
        south.add(gobackbutton, BorderLayout.CENTER);
        MainPanel.add(south, BorderLayout.SOUTH);

        setContentPane(MainPanel);

        // ── Action listeners ──────────────────────────────────────────────
        bookappointment.addActionListener(e -> {
            if (DatabaseHelper.hasFamilyMembers(userEmail)) {
                new FAMAPPOINTMENT(userEmail, userName); // Add userName here!
            } else {
                new APPOINTMENT(userEmail, userName);
            }
            dispose();
        });

        changeAnAppointmentButton.addActionListener(e -> new SelectionModify(userEmail));

        viewYourAppointmentsButton.addActionListener(e -> new HistoryView(userEmail));

        modifyMyAccountButton.addActionListener(e -> new ModifyAccount(userEmail));

        gobackbutton.addActionListener(e -> {
            new MenuForm();
            dispose();
        });
    }

    /** White-bordered button with orange text — matches the screenshot style exactly. */
    private JButton outlineButton(String text) {
        JButton btn = new JButton(text);
        btn.setBackground(new Color(30, 30, 60));
        btn.setForeground(new Color(228, 122, 50));
        btn.setFont(new Font("Arial", Font.BOLD, 16));
        btn.setFocusPainted(false);
        btn.setOpaque(true);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.WHITE, 2, true),
                new EmptyBorder(14, 20, 14, 20)
        ));
        // Subtle hover highlight
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(new Color(50, 50, 90));
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(new Color(30, 30, 60));
            }
        });
        return btn;
    }
}