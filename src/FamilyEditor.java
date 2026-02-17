import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class FamilyEditor extends JFrame {
    private JTable familyTable;
    private DefaultTableModel model;
    private String parentEmail;

    public FamilyEditor(String email) {
        this.parentEmail = email;

        // --- Branding & UI Setup (Criterion #10) ---
        setTitle("Duperly & Lanner - Family Management");
        setSize(700, 450);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(30, 30, 60)); // Navy Blue

        // Table Setup - Columns match DatabaseHelper.saveFamilyMember
        String[] cols = {"Member Name", "Age", "DOB"};
        model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        familyTable = new JTable(model);
        familyTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        styleTable(familyTable);

        refreshTable();

        // Buttons Panel
        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(new Color(30, 30, 60));

        JButton addBtn = new JButton("Add Member");
        JButton deleteBtn = new JButton("Remove Member");
        JButton backBtn = new JButton("Back to Account");

        styleButton(addBtn);
        styleButton(deleteBtn);
        styleButton(backBtn);

        // ADD MEMBER LOGIC
        addBtn.addActionListener(e -> {
            String name = JOptionPane.showInputDialog(this, "Enter Member Name:");
            if (name == null || name.trim().isEmpty()) return;

            String ageStr = JOptionPane.showInputDialog(this, "Enter Age:");
            String dob = JOptionPane.showInputDialog(this, "Enter Date of Birth (YYYY-MM-DD):");

            try {
                int age = Integer.parseInt(ageStr);
                if (DatabaseHelper.saveFamilyMember(parentEmail, name, age, dob)) {
                    refreshTable();
                    JOptionPane.showMessageDialog(this, name + " added successfully.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid age. Please enter a number.");
            }
        });

        // DELETE MEMBER LOGIC
        deleteBtn.addActionListener(e -> {
            int row = familyTable.getSelectedRow();
            if (row != -1) {
                String name = model.getValueAt(row, 0).toString();
                int confirm = JOptionPane.showConfirmDialog(this, "Remove " + name + "?", "Confirm", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    DatabaseHelper.deleteFamilyMember(parentEmail, name);
                    refreshTable();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a member to remove.");
            }
        });

        backBtn.addActionListener(e -> dispose());

        btnPanel.add(addBtn);
        btnPanel.add(deleteBtn);
        btnPanel.add(backBtn);

        add(new JScrollPane(familyTable), BorderLayout.CENTER);
        add(btnPanel, BorderLayout.SOUTH);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void refreshTable() {
        model.setRowCount(0);
        // Note: Modified DatabaseHelper to return List<String[]> for this view
        java.util.List<String[]> members = DatabaseHelper.getFamilyMembersDetailed(parentEmail);
        for (String[] row : members) {
            model.addRow(row);
        }
    }

    private void styleButton(JButton btn) {
        btn.setBackground(new Color(228, 122, 50)); // Orange
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
    }

    private void styleTable(JTable table) {
        table.getTableHeader().setBackground(new Color(228, 122, 50));
        table.getTableHeader().setForeground(Color.WHITE);
        table.setRowHeight(25);
    }
}