import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;

public class FamilyAccount extends JFrame {

    private JPanel FamPanel;
    private final String fName, lName, email;
    private JComboBox<Integer> combomember;
    private JPanel dynamicFieldsPanel;

    public FamilyAccount(String fName, String lName, String email) {
        this.fName = fName;
        this.lName = lName;
        this.email = email;

        setTitle("Duperly & Lanner Grupo Dental – Set Up Family Members");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(900, 700);
        setLocationRelativeTo(null);

        buildUI();
        setVisible(true);
    }

    private void buildUI() {
        JPanel root = new JPanel(new BorderLayout(0, 0));
        root.setBackground(new Color(237, 248, 250));

        // Header
        JLabel title = new JLabel("SET UP YOUR FAMILY ACCOUNT", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setForeground(new Color(228, 122, 50));
        title.setBorder(new EmptyBorder(20, 10, 15, 10));

        // Sub-header
        JPanel topBar = new JPanel(new GridBagLayout());
        topBar.setBackground(new Color(30, 30, 60));
        topBar.setBorder(new EmptyBorder(10, 20, 15, 20));

        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(5, 8, 5, 8);
        g.fill = GridBagConstraints.HORIZONTAL;

        JLabel subtitle = new JLabel(
                "Fill out the information of the family members you would like to add to your account");
        subtitle.setFont(new Font("Arial", Font.PLAIN, 16));
        subtitle.setForeground(Color.WHITE);
        g.gridx = 0; g.gridy = 0; g.gridwidth = 2; g.weightx = 1;
        topBar.add(subtitle, g);

        JLabel countLabel = new JLabel("How many members would you like to add?");
        countLabel.setForeground(Color.WHITE);
        countLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        g.gridy = 1; g.gridwidth = 1; g.weightx = 0;
        g.gridx = 0;
        topBar.add(countLabel, g);

        combomember = new JComboBox<>();
        for (int i = 1; i <= 5; i++) combomember.addItem(i);
        combomember.setFont(new Font("Arial", Font.PLAIN, 13));
        combomember.setBackground(Color.WHITE);
        combomember.setForeground(new Color(30, 30, 60));
        combomember.addActionListener(e -> generateFields((int) combomember.getSelectedItem()));
        g.gridx = 1; g.weightx = 1;
        topBar.add(combomember, g);

        JPanel northWrapper = new JPanel(new BorderLayout());
        northWrapper.setBackground(new Color(30, 30, 60));
        northWrapper.add(title, BorderLayout.NORTH);
        northWrapper.add(topBar, BorderLayout.CENTER);
        root.add(northWrapper, BorderLayout.NORTH);

        // Dynamic fields panel
        dynamicFieldsPanel = new JPanel();
        dynamicFieldsPanel.setBackground(new Color(30, 30, 60));

        JScrollPane scroll = new JScrollPane(dynamicFieldsPanel);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(new Color(30, 30, 60));
        root.add(scroll, BorderLayout.CENTER);

        setContentPane(root);
        generateFields(1);
    }

    private void generateFields(int count) {
        dynamicFieldsPanel.removeAll();
        dynamicFieldsPanel.setLayout(new GridBagLayout());

        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(2, 10, 2, 10);
        g.fill = GridBagConstraints.HORIZONTAL;
        g.anchor = GridBagConstraints.WEST;

        int row = 0;
        for (int i = 1; i <= count; i++) {
            // Member header
            g.gridy = row++; g.gridx = 0; g.gridwidth = 3; g.weightx = 1;
            JLabel header = new JLabel("MEMBER " + i);
            header.setForeground(new Color(228, 122, 50));
            header.setFont(new Font("Arial", Font.BOLD, 15));
            header.setBorder(new EmptyBorder(i == 1 ? 5 : 15, 0, 3, 0));
            dynamicFieldsPanel.add(header, g);

            // Column labels
            g.gridy = row++; g.gridwidth = 1;
            g.gridx = 0; dynamicFieldsPanel.add(whiteLabel("Full Name"), g);
            g.gridx = 1; dynamicFieldsPanel.add(whiteLabel("Age"), g);
            g.gridx = 2; dynamicFieldsPanel.add(whiteLabel("DOB (YYYY-MM-DD)"), g);

            // Input fields
            g.gridy = row++;
            g.gridx = 0; dynamicFieldsPanel.add(styledField(15), g);
            g.gridx = 1; dynamicFieldsPanel.add(styledField(5), g);
            g.gridx = 2; dynamicFieldsPanel.add(styledField(10), g);
        }

        // FIXED: Button with white background and dark text
        g.gridy = row; g.gridx = 0; g.gridwidth = 3;
        g.insets = new Insets(25, 10, 20, 10);
        JButton saveBtn = new JButton("SAVE FAMILY MEMBERS AND CONTINUE");
        saveBtn.setBackground(Color.WHITE);
        saveBtn.setForeground(new Color(228, 122, 50));  // Orange text
        saveBtn.setFont(new Font("Arial", Font.BOLD, 13));
        saveBtn.setFocusPainted(false);
        saveBtn.setBorderPainted(true);
        saveBtn.setBorder(BorderFactory.createLineBorder(new Color(228, 122, 50), 2));
        saveBtn.setOpaque(true);
        saveBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        saveBtn.addActionListener(e -> saveFamilyToDatabase());
        dynamicFieldsPanel.add(saveBtn, g);

        dynamicFieldsPanel.revalidate();
        dynamicFieldsPanel.repaint();
    }

    private void saveFamilyToDatabase() {
        Component[] components = dynamicFieldsPanel.getComponents();
        ArrayList<JTextField> fields = new ArrayList<>();
        for (Component c : components)
            if (c instanceof JTextField) fields.add((JTextField) c);

        for (int i = 0; i < fields.size(); i += 3) {
            String name = fields.get(i).getText().trim();
            String ageStr = fields.get(i + 1).getText().trim();
            String dob = fields.get(i + 2).getText().trim();

            if (name.isEmpty() || ageStr.isEmpty() || dob.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields for every member.");
                return;
            }

            try {
                int age = Integer.parseInt(ageStr);
                if (age < 7) {
                    JOptionPane.showMessageDialog(this,
                            "The clinic accepts patients from age 7 and up.\n" +
                                    name + " does not meet the minimum age requirement.");
                    return;
                }
                if (age >= 18) {
                    JOptionPane.showMessageDialog(this,
                            name + " is an adult (18+) and must register their own account.");
                    return;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Age must be a whole number.");
                return;
            }
        }

        for (int i = 0; i < fields.size(); i += 3) {
            String name = fields.get(i).getText().trim();
            int age = Integer.parseInt(fields.get(i + 1).getText().trim());
            String dob = fields.get(i + 2).getText().trim();
            DatabaseHelper.saveFamilyMember(email, name, age, dob);
        }

        JOptionPane.showMessageDialog(this,
                "Family members registered successfully!\nYou can now log in.");

        new LogIn();
        dispose();
    }

    private JLabel whiteLabel(String text) {
        JLabel l = new JLabel(text);
        l.setForeground(Color.WHITE);
        l.setFont(new Font("Arial", Font.BOLD, 12));
        return l;
    }

    private JTextField styledField(int columns) {
        JTextField f = new JTextField(columns);
        f.setBorder(BorderFactory.createLineBorder(new Color(228, 122, 50), 1));
        f.setBackground(Color.WHITE);
        f.setForeground(new Color(30, 30, 60));
        return f;
    }
}