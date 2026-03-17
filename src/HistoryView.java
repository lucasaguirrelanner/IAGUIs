import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class HistoryView extends JFrame {

    private final String contactEmail;
    private DefaultTableModel tableModel;
    private JTable table;

    private static final String[] COLUMNS = {
            "ID", "Patient", "Date & Time", "Procedure", "Doctor", "Status"
    };

    public HistoryView(String email) {
        this.contactEmail = email;

        setTitle("Duperly & Lanner Grupo Dental – Appointment History");
        setSize(900, 500);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        buildUI();
        loadData();
        setVisible(true);
    }

    private void buildUI() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(new Color(30, 30, 60));


        JLabel header = new JLabel("Your Appointments", SwingConstants.CENTER);
        header.setFont(new Font("Arial", Font.BOLD, 20));
        header.setForeground(new Color(228, 122, 50));
        header.setBorder(new EmptyBorder(15, 0, 10, 0));
        root.add(header, BorderLayout.NORTH);

        // Table
        tableModel = new DefaultTableModel(COLUMNS, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(24);
        table.getTableHeader().setBackground(new Color(228, 122, 50));
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        table.setFont(new Font("Arial", Font.PLAIN, 12));

        table.getColumnModel().getColumn(0).setMinWidth(0);
        table.getColumnModel().getColumn(0).setMaxWidth(0);
        table.getColumnModel().getColumn(0).setWidth(0);

        root.add(new JScrollPane(table), BorderLayout.CENTER);

        // Buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 12));
        btnPanel.setBackground(new Color(30, 30, 60));
//Cancelling, rescheduling, refreshing and closing the window:
        JButton cancelBtn     = styledBtn("Cancel Appointment",     Color.RED.darker());
        JButton rescheduleBtn = styledBtn("Reschedule Appointment", new Color(228, 122, 50));
        JButton refreshBtn    = styledBtn("Refresh",                new Color(80, 140, 200));
        JButton closeBtn      = styledBtn("Close",                  new Color(100, 100, 100));

        cancelBtn.addActionListener(e     -> cancelSelected());
        rescheduleBtn.addActionListener(e -> rescheduleSelected());
        refreshBtn.addActionListener(e    -> loadData());
        closeBtn.addActionListener(e      -> dispose());

        btnPanel.add(cancelBtn);
        btnPanel.add(rescheduleBtn);
        btnPanel.add(refreshBtn);
        btnPanel.add(closeBtn);
        root.add(btnPanel, BorderLayout.SOUTH);

        setContentPane(root);
    }

    private JButton styledBtn(String text, Color bg) {
        JButton b = new JButton(text);
        b.setBackground(bg);
        b.setForeground(Color.WHITE);
        b.setOpaque(true);
        b.setFont(new Font("Arial", Font.BOLD, 12));
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    private void loadData() {
        tableModel.setRowCount(0);
        List<String[]> rows = DatabaseHelper.getAppointmentsByEmail(contactEmail);
        for (String[] row : rows) tableModel.addRow(row);
    }

    //An appointment must be selected for it to be cancelled:
    private void cancelSelected() {
        int row = table.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Select an appointment first."); return; }

//Enforcing that an appointment must have been confiremd previously to be cancelled:
        String status = tableModel.getValueAt(row, 5).toString();
        if (!status.equals("CONFIRMED")) {
            JOptionPane.showMessageDialog(this, "Only CONFIRMED appointments can be cancelled."); return;
        }

        int id          = Integer.parseInt(tableModel.getValueAt(row, 0).toString());
        String patient  = tableModel.getValueAt(row, 1).toString();

        int choice = JOptionPane.showConfirmDialog(this,
                "Cancel appointment for " + patient + "?",
                "Confirm Cancellation", JOptionPane.YES_NO_OPTION);

        if (choice == JOptionPane.YES_OPTION) {
            if (DatabaseHelper.cancelAppointment(id, contactEmail, patient)) {
                JOptionPane.showMessageDialog(this,
                        "Appointment cancelled. A cancellation email has been sent.");
                loadData();
            } else {
                JOptionPane.showMessageDialog(this, "Error cancelling. Please try again.");
            }
        }
    }

    //Appointments being rescheduled:
    private void rescheduleSelected() {
        int row = table.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Select an appointment first."); return; }

        String status = tableModel.getValueAt(row, 5).toString();
        if (!status.equals("CONFIRMED")) {
            JOptionPane.showMessageDialog(this, "Only CONFIRMED appointments can be rescheduled."); return;
        }

        int    id      = Integer.parseInt(tableModel.getValueAt(row, 0).toString());
        String patient = tableModel.getValueAt(row, 1).toString();
        String doctor  = tableModel.getValueAt(row, 4).toString();
        String proc    = tableModel.getValueAt(row, 3).toString();

        //Editing the new dates and times for the Appointment:
        JSpinner dateSpinner = new JSpinner(new SpinnerDateModel());
        dateSpinner.setEditor(new JSpinner.DateEditor(dateSpinner, "dd/MM/yyyy"));
        dateSpinner.setValue(new java.util.Date(System.currentTimeMillis() + 86400000L));

        String[] slots = {"08:00","09:00","10:00","11:00","13:00","14:00","15:00","16:00"};
        JComboBox<String> timeBox = new JComboBox<>(slots);

        JPanel panel = new JPanel(new GridLayout(4, 2, 8, 8));
        panel.add(new JLabel("New Date:")); panel.add(dateSpinner);
        panel.add(new JLabel("New Time:")); panel.add(timeBox);
        panel.add(new JLabel("Doctor:"));   panel.add(new JLabel(doctor));
        panel.add(new JLabel("Procedure:")); panel.add(new JLabel(proc));

        int choice = JOptionPane.showConfirmDialog(this, panel,
                "Reschedule Appointment", JOptionPane.OK_CANCEL_OPTION);
        if (choice != JOptionPane.OK_OPTION) return;


        java.util.Date d = (java.util.Date) dateSpinner.getValue();
        java.time.LocalDate ld = d.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
        String time = (String) timeBox.getSelectedItem();
        String[] parts = time.split(":");
        java.time.LocalDateTime newDT = ld.atTime(
                Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));

        //ensuring rescheduled time and dates are available:
        int dur = DatabaseHelper.getProcedureDuration(proc);
        if (!DatabaseHelper.isSlotAvailable(doctor, newDT, dur)) {
            JOptionPane.showMessageDialog(this,
                    "That slot is already taken. Please choose another time.",
                    "Slot Unavailable", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (DatabaseHelper.rescheduleAppointment(id, newDT)) {
            JOptionPane.showMessageDialog(this, "Appointment rescheduled successfully!");
            loadData();
        } else {
            JOptionPane.showMessageDialog(this, "Error rescheduling. Please try again.");
        }
    }
}
