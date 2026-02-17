import javax.swing.*;
import java.awt.*;

public class AppointmentSuccess extends JFrame {

    public AppointmentSuccess(String patient, String procedure, String time) {
        // --- Branding & Setup ---
        setTitle("Booking Confirmed - Duperly & Lanner");
        setSize(450, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(new Color(30, 30, 60)); // Navy Blue
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // --- Success Icon/Header ---
        JLabel successHeader = new JLabel("BOOKING SUCCESSFUL!");
        successHeader.setForeground(new Color(228, 122, 50)); // Orange
        successHeader.setFont(new Font("Arial", Font.BOLD, 22));
        successHeader.setAlignmentX(Component.CENTER_ALIGNMENT);

        // --- Detail Labels ---
        // Using HTML for better formatting within the labels
        JLabel detailsLabel = new JLabel("<html><div style='text-align: center; color: white; font-size: 12px;'>" +
                "<br>Your appointment has been added to our schedule.<br><br>" +
                "<b>Patient:</b> " + patient + "<br>" +
                "<b>Procedure:</b> " + procedure + "<br>" +
                "<b>Scheduled Time:</b> " + time + "<br><br>" +
                "<i style='color: #AAAAAA;'>A confirmation email has been sent to your inbox.</i>" +
                "</div></html>");
        detailsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // --- Action Button ---
        JButton btnReturn = new JButton("Return to Dashboard");
        btnReturn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnReturn.setBackground(new Color(228, 122, 50));
        btnReturn.setForeground(Color.WHITE);
        btnReturn.setFocusPainted(false);
        btnReturn.setFont(new Font("Arial", Font.BOLD, 14));
        btnReturn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnReturn.addActionListener(e -> dispose());

        // --- Assembly ---
        mainPanel.add(successHeader);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainPanel.add(detailsLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        mainPanel.add(btnReturn);

        add(mainPanel);

        // Final polish: ensure the window is visible
        setVisible(true);
    }
}