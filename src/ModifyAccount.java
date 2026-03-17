import javax.swing.*;
import java.awt.*;

public class ModifyAccount extends JFrame {
    private JPanel mainPanel;
    private JTextField nameField;
    private String userEmail;

    public ModifyAccount(String email) {
        this.userEmail = email;


        setTitle("Account Settings - Duperly & Lanner");
        setSize(500, 600);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        //Creation of the appointment modification panel:
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(new Color(30, 30, 60));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        //For users that wish to update their display name:
        JLabel nameLabel = new JLabel("Update Your Display Name:");
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        nameField = new JTextField(DatabaseHelper.getFirstName(email));
        nameField.setMaximumSize(new Dimension(300, 30));

        JButton updateNameBtn = new JButton("Save New Name");
        styleButton(updateNameBtn, new Color(228, 122, 50));

        updateNameBtn.addActionListener(e -> {
            String newName = nameField.getText().trim();
            if (!newName.isEmpty()) {
                if (DatabaseHelper.updateUsername(email, newName)) {
                    JOptionPane.showMessageDialog(this, "Name updated successfully!");
                }
            }
        });

//In case they want to change their password:
        JButton passBtn = new JButton("Change Password");
        styleButton(passBtn, new Color(100, 100, 100)); // Grey

        passBtn.addActionListener(e -> new Restablishpassword(email));

        //For family accounts, they can manage their members through this button:
        JButton familyBtn = new JButton("Manage Family Members");
        styleButton(familyBtn, new Color(100, 100, 100)); // Grey

        familyBtn.addActionListener(e -> new FamilyEditor(email));


        addSpace(20);
        mainPanel.add(nameLabel);
        addSpace(10);
        mainPanel.add(nameField);
        addSpace(10);
        mainPanel.add(updateNameBtn);
        addSpace(40);
        mainPanel.add(new JSeparator());
        addSpace(40);
        mainPanel.add(passBtn);
        addSpace(20);
        mainPanel.add(familyBtn);

        add(mainPanel);
        setVisible(true);
    }


    private void addSpace(int height) {
        mainPanel.add(Box.createRigidArea(new Dimension(0, height)));
    }


    private void styleButton(JButton btn, Color bgColor) {
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setOpaque(true);
        btn.setBorderPainted(false);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(250, 40));
        btn.setFocusPainted(false);
        btn.setFont(new Font("Arial", Font.BOLD, 13));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }
}
