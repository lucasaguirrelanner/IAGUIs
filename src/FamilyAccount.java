import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class FamilyAccount extends JFrame {
    private JComboBox<Integer> combomember = new JComboBox<>();
    private JPanel FamPanel;
    private JPanel dynamicFieldsPanel;
    private String fName, lName, email;
    private boolean isFamilyAccount = true; // They are on this page, so this is true

    public FamilyAccount(String fName, String lName, String email) {
        this.fName = fName;
        this.lName = lName;
        this.email = email;

        // Ensure these panels are initialized if not done by GUI Designer
        if (FamPanel == null) FamPanel = new JPanel(new BorderLayout());
        if (dynamicFieldsPanel == null) dynamicFieldsPanel = new JPanel();

        setContentPane(FamPanel);
        setTitle("Duperly & Lanner - Set Up Family Members");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(900, 800);
        setLocationRelativeTo(null);

        // Populate ComboBox
        for (int i = 1; i <= 5; i++) combomember.addItem(i);

        combomember.addActionListener(e -> {
            int count = (int) combomember.getSelectedItem();
            generateFields(count);
        });

        // Initialize with 1 field
        generateFields(1);

        setVisible(true);
    }

    private void generateFields(int count) {
        dynamicFieldsPanel.removeAll();
        dynamicFieldsPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Font labelFont = new Font("Arial", Font.BOLD, 14);
        int currentRow = 0;

        for (int i = 1; i <= count; i++) {
            gbc.gridy = currentRow++;
            gbc.gridx = 0; gbc.gridwidth = 3;
            JLabel header = new JLabel("MEMBER " + i);
            header.setForeground(new Color(228, 122, 50));
            header.setFont(new Font("Arial", Font.BOLD, 18));
            dynamicFieldsPanel.add(header, gbc);

            gbc.gridy = currentRow++;
            gbc.gridwidth = 1;
            gbc.gridx = 0; dynamicFieldsPanel.add(new JLabel("Full Name"), gbc);
            gbc.gridx = 1; dynamicFieldsPanel.add(new JLabel("Age"), gbc);
            gbc.gridx = 2; dynamicFieldsPanel.add(new JLabel("DOB (YYYY-MM-DD)"), gbc);

            gbc.gridy = currentRow++;
            gbc.gridx = 0; dynamicFieldsPanel.add(createStyledField(15), gbc);
            gbc.gridx = 1; dynamicFieldsPanel.add(createStyledField(5), gbc);
            gbc.gridx = 2; dynamicFieldsPanel.add(createStyledField(10), gbc);

            currentRow++;
        }

        gbc.gridy = currentRow;
        gbc.gridx = 0; gbc.gridwidth = 3;
        JButton saveBtn = new JButton("SAVE AND PROCEED TO CREDENTIALS");
        saveBtn.addActionListener(ev -> saveFamilyToDatabase());
        dynamicFieldsPanel.add(saveBtn, gbc);

        dynamicFieldsPanel.revalidate();
        dynamicFieldsPanel.repaint();
    }

    private void saveFamilyToDatabase() {
        Component[] components = dynamicFieldsPanel.getComponents();
        ArrayList<JTextField> fields = new ArrayList<>();

        for (Component c : components) {
            if (c instanceof JTextField) fields.add((JTextField) c);
        }

        for (int i = 0; i < fields.size(); i += 3) {
            String name = fields.get(i).getText().trim();
            String ageStr = fields.get(i+1).getText().trim();
            String dob = fields.get(i+2).getText().trim();

            if (name.isEmpty() || ageStr.isEmpty() || dob.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields for all members.");
                return;
            }

            try {
                int age = Integer.parseInt(ageStr);
                if (age >= 18) {
                    JOptionPane.showMessageDialog(this, name + " is an adult. Adults need separate accounts.");
                    return;
                }

                // Save to Database
                DatabaseHelper.saveFamilyMember(this.email, name, age, dob);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Age must be a number.");
                return;
            }
        }

        JOptionPane.showMessageDialog(this, "Family members registered!");
        // FIXED: Passing 4 arguments to match the updated CrearCuenta2
        new CrearCuenta2(this.fName, this.lName, this.email, true);
        dispose();
    }

    private JTextField createStyledField(int columns) {
        JTextField field = new JTextField(columns);
        field.setBorder(BorderFactory.createLineBorder(new Color(228, 122, 50), 1));
        return field;
    }
}