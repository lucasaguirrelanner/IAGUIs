import javax.swing.*;
import java.awt.*;

public class ModifyAccount extends JFrame {
    private JPanel mainPanel;
    private JTextField nameField;
    private String userEmail;

    public ModifyAccount(String email) {
        this.userEmail = email;

        // --- Window Setup ---
        setTitle("Account Settings - Duperly & Lanner");
        setSize(500, 600);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // --- Main Panel with Branding (Criterion #10) ---
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(new Color(30, 30, 60)); // Navy Blue
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        // --- Section 1: Update Name ---
        JLabel nameLabel = new JLabel("Update Your Display Name:");
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        nameField = new JTextField(DatabaseHelper.getFirstName(email));
        nameField.setMaximumSize(new Dimension(300, 30));

        JButton updateNameBtn = new JButton("Save New Name");
        styleButton(updateNameBtn, new Color(228, 122, 50)); // Orange

        updateNameBtn.addActionListener(e -> {
            String newName = nameField.getText().trim();
            if (!newName.isEmpty()) {
                if (DatabaseHelper.updateUsername(email, newName)) {
                    JOptionPane.showMessageDialog(this, "Name updated successfully!");
                }
            }
        });

        // --- Section 2: Password Security ---
        JButton passBtn = new JButton("Change Password");
        styleButton(passBtn, new Color(100, 100, 100)); // Grey

        passBtn.addActionListener(e -> new Restablishpassword(email));

        // --- Section 3: Family Management (Criterion #1) ---
        JButton familyBtn = new JButton("Manage Family Members");
        styleButton(familyBtn, new Color(100, 100, 100)); // Grey

        familyBtn.addActionListener(e -> new FamilyEditor(email));

        // --- Layout Assembly ---
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

    // UI Helper: Adds vertical gaps
    private void addSpace(int height) {
        mainPanel.add(Box.createRigidArea(new Dimension(0, height)));
    }

    // UI Helper: Standardizes button looks
    private void styleButton(JButton btn, Color bgColor) {
        btn.setBackground(bgColor);
        btn.setForeground(new Color(228, 122, 50));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(250, 40));
        btn.setFocusPainted(false);
        btn.setFont(new Font("Arial", Font.BOLD, 13));
    }
}