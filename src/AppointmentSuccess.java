import javax.swing.*;
import java.awt.*;
import javax.swing.border.EmptyBorder;

//Success window: displays a success message to the user, allowing them to return to their dashboard afterward:

public class AppointmentSuccess extends JFrame {
    private String sessionEmail;
    private String sessionName;
    public AppointmentSuccess(String patient, String procedure, String time, String email, String name) {
        this.sessionEmail = email;
        this.sessionName = name;

        //How the confirmation is displayed in the platform:
        setTitle("Booking Confirmed - Duperly & Lanner");
        setSize(450, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(new Color(30, 30, 60));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));


        JLabel successHeader = new JLabel("BOOKING SUCCESSFUL!");
        successHeader.setForeground(new Color(228, 122, 50));
        successHeader.setFont(new Font("Arial", Font.BOLD, 22));
        successHeader.setAlignmentX(Component.CENTER_ALIGNMENT);


        JLabel detailsLabel = new JLabel("<html><div style='text-align: center; color: white; font-size: 12px;'>" +
                "<br>Your appointment has been added to our schedule.<br><br>" +
                "<b>Patient:</b> " + patient + "<br>" +
                "<b>Procedure:</b> " + procedure + "<br>" +
                "<b>Scheduled Time:</b> " + time + "<br><br>" +
                "<i style='color: #AAAAAA;'>A confirmation email has been sent to your inbox.</i>" +
                "</div></html>");
        detailsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        //Return to dashboard button:
        JButton btnReturn = new JButton("Return to Dashboard");
        btnReturn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnReturn.setBackground(new Color(228, 122, 50));
        btnReturn.setForeground(new Color (43, 43, 92));
        btnReturn.setFocusPainted(false);
        btnReturn.setFont(new Font("Futura", Font.BOLD, 14));
        btnReturn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnReturn.addActionListener(e -> {
            new Account(sessionEmail, sessionName);
            dispose();

        });


        mainPanel.add(successHeader);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainPanel.add(detailsLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        mainPanel.add(btnReturn);

        add(mainPanel);


        setVisible(true);
    }
}
