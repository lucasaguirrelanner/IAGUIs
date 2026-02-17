import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * FamilyEditor – lets a patient view, add, or remove family members.
 * FIX: updated to handle the 4-column result {id, name, age, dob} from
 *      DatabaseHelper.getFamilyMembersDetailed.
 * SUCCESS CRITERIA #2 – children from age 7 and up.
 */
public class FamilyEditor extends JFrame {
    private JTable            familyTable;
    private DefaultTableModel model;
    private final String      parentEmail;

    private static final String[] COLS = {"Name", "Age", "Date of Birth"};

    public FamilyEditor(String email) {
        this.parentEmail = email;

        setTitle("Duperly & Lanner Grupo Dental – Family Management");
        setSize(700, 450);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(30, 30, 60));

        // Table – show name / age / dob (hide internal id)
        model = new DefaultTableModel(COLS, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        familyTable = new JTable(model);
        familyTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        styleTable(familyTable);
        refreshTable();

        // Buttons
        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(new Color(30, 30, 60));

        JButton addBtn    = new JButton("Add Member");
        JButton deleteBtn = new JButton("Remove Member");
        JButton backBtn   = new JButton("Back");

        styleButton(addBtn); styleButton(deleteBtn); styleButton(backBtn);

        // ADD
        addBtn.addActionListener(e -> {
            String name   = JOptionPane.showInputDialog(this, "Member's Full Name:");
            if (name == null || name.trim().isEmpty()) return;

            String ageStr = JOptionPane.showInputDialog(this, "Age:");
            String dob    = JOptionPane.showInputDialog(this, "Date of Birth (YYYY-MM-DD):");

            try {
                int age = Integer.parseInt(ageStr.trim());
                // Criterion #2 – minimum age is 7
                if (age < 7) {
                    JOptionPane.showMessageDialog(this,
                            "The clinic accepts patients from age 7 and up.");
                    return;
                }
                if (age >= 18) {
                    JOptionPane.showMessageDialog(this,
                            name + " is an adult (18+) and must create their own account.");
                    return;
                }
                if (DatabaseHelper.saveFamilyMember(parentEmail, name.trim(), age, dob.trim())) {
                    refreshTable();
                    JOptionPane.showMessageDialog(this, name + " added successfully.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Age must be a whole number.");
            }
        });

        // DELETE
        deleteBtn.addActionListener(e -> {
            int row = familyTable.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Select a member first."); return;
            }
            String name = model.getValueAt(row, 0).toString();
            int ok = JOptionPane.showConfirmDialog(this,
                    "Remove " + name + "?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (ok == JOptionPane.YES_OPTION) {
                DatabaseHelper.deleteFamilyMember(parentEmail, name);
                refreshTable();
            }
        });

        backBtn.addActionListener(e -> dispose());

        btnPanel.add(addBtn); btnPanel.add(deleteBtn); btnPanel.add(backBtn);
        add(new JScrollPane(familyTable), BorderLayout.CENTER);
        add(btnPanel, BorderLayout.SOUTH);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void refreshTable() {
        model.setRowCount(0);
        // getFamilyMembersDetailed returns {id, name, age, dob}
        List<String[]> members = DatabaseHelper.getFamilyMembersDetailed(parentEmail);
        for (String[] m : members) {
            model.addRow(new Object[]{m[1], m[2], m[3]}); // skip m[0]=id
        }
    }

    private void styleButton(JButton btn) {
        btn.setBackground(new Color(228, 122, 50));
        btn.setForeground(Color.WHITE);
        btn.setOpaque(true);              // ← this line was missing
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }
    private void styleTable(JTable t) {
        t.getTableHeader().setBackground(new Color(228, 122, 50));
        t.getTableHeader().setForeground(Color.WHITE);
        t.setRowHeight(25);
    }
}
