import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

public class APPOINTMENT extends JFrame {
    //Panel:
    private JPanel APPPanel;
    private JLabel ProcedureLabel;
    private final String userEmail;
    private final String userName;
    private final boolean isChild;
    private JComboBox<String> doctorCombo;
    private JComboBox<String> procedureCombo;
    private JComboBox<String> timeCombo;
    private JSpinner dateSpinner;
    private JButton checkButton, confirmButton, backButton;
    private JLabel statusLabel;


    public APPOINTMENT(String email, String name) {
        this(email, name, false);
    }


    public APPOINTMENT(String email, String name, boolean isChild) {
        this.userEmail = email;
        this.userName  = name;
        this.isChild   = isChild;

        setTitle("Duperly & Lanner Grupo Dental – Book Appointment");
        setSize(820, 600);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        buildUI();
        loadDoctors();
        setVisible(true);
    }

    private void buildUI() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(new Color(30, 30, 60));

        JLabel header = new JLabel("Schedule an Appointment", SwingConstants.CENTER);
        header.setFont(new Font("Arial", Font.BOLD, 22));
        header.setForeground(new Color(228, 122, 50));
        header.setBorder(new EmptyBorder(20, 0, 10, 0));
        root.add(header, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(new Color(30, 30, 60));
        form.setBorder(new EmptyBorder(10, 60, 10, 60));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(3, 8, 3, 8);
        g.fill = GridBagConstraints.HORIZONTAL;
        g.anchor = GridBagConstraints.WEST;

        // Doctor
        addLabel(form, "Select Doctor:", g, 0, 0);
        doctorCombo = new JComboBox<>();
        doctorCombo.setFont(new Font("Arial", Font.PLAIN, 13));
        doctorCombo.addActionListener(e -> loadProcedures());
        styleCombo(doctorCombo);
        g.gridx = 1; g.gridy = 0; g.weightx = 1;
        form.add(doctorCombo, g);

        // Procedure
        addLabel(form, "Select Procedure:", g, 0, 1);
        procedureCombo = new JComboBox<>();
        styleCombo(procedureCombo);
        g.gridx = 1; g.gridy = 1;
        form.add(procedureCombo, g);

        // Date
        addLabel(form, "Select Date:", g, 0, 2);
        SpinnerDateModel dateModel = new SpinnerDateModel();
        dateSpinner = new JSpinner(dateModel);
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinner, "dd/MM/yyyy");
        dateSpinner.setEditor(dateEditor);
        dateSpinner.setValue(new Date(System.currentTimeMillis() + 86400000L));
        g.gridx = 1; g.gridy = 2;
        form.add(dateSpinner, g);

        // Time
        addLabel(form, "Select Time:", g, 0, 3);
        timeCombo = new JComboBox<>(new String[]{
                "08:00","09:00","10:00","11:00","13:00","14:00","15:00","16:00"
        });
        styleCombo(timeCombo);
        g.gridx = 1; g.gridy = 3;
        form.add(timeCombo, g);

        // Status label
        statusLabel = new JLabel(" ", SwingConstants.CENTER);
        statusLabel.setForeground(Color.YELLOW);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 12));
        g.gridx = 0; g.gridy = 4; g.gridwidth = 2;
        form.add(statusLabel, g);

        root.add(form, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        btnPanel.setBackground(new Color(30, 30, 60));
        checkButton   = styledButton("Check Availability", new Color(80, 140, 200));
        confirmButton = styledButton("Confirm Booking",    new Color(228, 122, 50));
        backButton    = styledButton("Back",               new Color(100, 100, 100));
        btnPanel.add(checkButton);
        btnPanel.add(confirmButton);
        btnPanel.add(backButton);
        root.add(btnPanel, BorderLayout.SOUTH);

        setContentPane(root);

        checkButton.addActionListener(e   -> checkAvailability());
        confirmButton.addActionListener(e -> bookAppointment());
        backButton.addActionListener(e -> {
            new Account(userEmail, DatabaseHelper.getFirstName(userEmail));
            dispose();
        });
    }

    private void addLabel(JPanel p, String text, GridBagConstraints g, int x, int y) {
        g.gridx = x; g.gridy = y; g.weightx = 0; g.gridwidth = 1;
        JLabel l = new JLabel(text);
        l.setForeground(Color.WHITE);
        l.setFont(new Font("Arial", Font.BOLD, 13));
        p.add(l, g);
    }

    private JButton styledButton(String text, Color accentColor) {
        JButton btn = new JButton(text);
        btn.setBackground(Color.WHITE);
        btn.setForeground(accentColor);
        btn.setFont(new Font("Arial", Font.BOLD, 13));
        btn.setFocusPainted(false);
        btn.setBorderPainted(true);
        btn.setBorder(BorderFactory.createLineBorder(accentColor, 2));
        btn.setOpaque(true);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void styleCombo(JComboBox<String> combo) {
        combo.setBackground(Color.WHITE);
        combo.setForeground(new Color(30, 30, 60));
        combo.setFont(new Font("Arial", Font.PLAIN, 13));
    }
    //Retreiving the available doctors, if patient is a child, only the 2 doctors who also attend children will be displayed as options:
    private void loadDoctors() {
        doctorCombo.removeAllItems();
        List<String> names = isChild
                ? DatabaseHelper.getDentistNamesForChildren()
                : DatabaseHelper.getDentistNames();
        if (names.isEmpty()) {
            if (isChild) {
                doctorCombo.addItem("Agnes Lanner");
                doctorCombo.addItem("Vanessa Jarra");
            } else {
                doctorCombo.addItem("Dr. Smith");
                doctorCombo.addItem("Dr. Johnson");
            }
        } else {
            for (String n : names) doctorCombo.addItem(n);
        }
        loadProcedures();
    }
    //Retreiving all of the procedures per dentist:
    private void loadProcedures() {
        String doctor = (String) doctorCombo.getSelectedItem();
        if (doctor == null) return;
        procedureCombo.removeAllItems();
        List<String> procs = DatabaseHelper.getProceduresForDentist(doctor);
        for (String proc : procs) procedureCombo.addItem(proc);
        refreshBlockedSlots();
    }
    //Refreshing booked slots to prevent overlapping bookings:
    private void refreshBlockedSlots() {
        String doctor = (String) doctorCombo.getSelectedItem();
        if (doctor == null) return;
        LocalDate date = getSelectedDate();

//Ensuring the doctor is working for the selected date:
        boolean scheduleExists = DatabaseHelper.doctorHasAnySchedule(doctor);
        if (scheduleExists && !DatabaseHelper.isDoctorWorkingOnDate(doctor, date)) {
            timeCombo.removeAllItems();
            String dayName = date.getDayOfWeek()
                    .getDisplayName(java.time.format.TextStyle.FULL, java.util.Locale.ENGLISH);
            statusLabel.setText("Dr. " + doctor + " does not work on " + dayName + "s. Please pick another date.");
            statusLabel.setForeground(Color.RED);
            return;
        }


        List<String> workingSlots = DatabaseHelper.getWorkingSlotsForDoctorOnDate(doctor, date);


        List<String> blocked = DatabaseHelper.getBlockedSlotsForDoctorOnDate(doctor, date);

        timeCombo.removeAllItems();
        for (String slot : workingSlots) {
            if (!blocked.contains(slot)) timeCombo.addItem(slot);
        }

        if (timeCombo.getItemCount() == 0) {
            statusLabel.setText("No available slots for this doctor on the selected date.");
            statusLabel.setForeground(Color.RED);
        } else {
            statusLabel.setText(" ");
        }
    }
    //Checking iof a dentist has availability for the selected time/date:
    private void checkAvailability() {
        String doctor    = (String) doctorCombo.getSelectedItem();
        String procedure = (String) procedureCombo.getSelectedItem();
        String time      = (String) timeCombo.getSelectedItem();
        if (doctor == null || procedure == null || time == null) {
            statusLabel.setText("Please select all fields first.");
            statusLabel.setForeground(Color.YELLOW);
            return;
        }
        LocalDateTime start    = buildDateTime(time);
        int           duration = DatabaseHelper.getProcedureDuration(procedure);
        if (DatabaseHelper.isSlotAvailable(doctor, start, duration)) {
            statusLabel.setText("✔ Slot available! Appointment would end at: "
                    + start.plusMinutes(duration).format(DateTimeFormatter.ofPattern("HH:mm")));
            statusLabel.setForeground(Color.GREEN);
        } else {
            statusLabel.setText("✘ This slot overlaps with an existing appointment. Choose another time.");
            statusLabel.setForeground(Color.RED);
        }
    }

    //Booking the appointment logic:
    private void bookAppointment() {
        String doctor    = (String) doctorCombo.getSelectedItem();
        String procedure = (String) procedureCombo.getSelectedItem();
        String time      = (String) timeCombo.getSelectedItem();

        if (doctor == null || procedure == null || time == null) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.");
            return;
        }

        LocalDateTime start    = buildDateTime(time);
        int           duration = DatabaseHelper.getProcedureDuration(procedure);

        if (!DatabaseHelper.isSlotAvailable(doctor, start, duration)) {
            JOptionPane.showMessageDialog(this,
                    "This time slot is already taken. Please choose another.",
                    "Slot Unavailable", JOptionPane.WARNING_MESSAGE);
            return;
        }
//Patient must exist, coincide with the DB info:
        String patientName = DatabaseHelper.getPatientNameByEmail(userEmail);
        if (patientName == null) {
            JOptionPane.showMessageDialog(this, "Error: Could not retrieve patient info.");
            return;
        }
//Enforcement of the 1 appointment per patient a day rule:
        if (DatabaseHelper.patientHasAppointmentOnDate(userEmail, start.toLocalDate())) {
            JOptionPane.showMessageDialog(this,
                    "You already have an appointment on this date.\n" +
                            "Only one appointment per day is allowed.",
                    "Booking Not Allowed", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String endTime = start.plusMinutes(duration).format(DateTimeFormatter.ofPattern("HH:mm"));
        String confirm = String.format(
                "<html><b>Please confirm your appointment:</b><br><br>" +
                        "Patient:    <b>%s</b><br>" +
                        "Doctor:     <b>Dr. %s</b><br>" +
                        "Procedure:  <b>%s</b><br>" +
                        "Date:       <b>%s</b><br>" +
                        "Time:       <b>%s – %s</b><br><br>" +
                        "A confirmation email will be sent to:<br><i>%s</i></html>",
                patientName, doctor, procedure,
                start.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                time, endTime, userEmail);
//Confirming a booking:
        int choice = JOptionPane.showConfirmDialog(this, confirm,
                "Confirm Appointment", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (choice != JOptionPane.YES_OPTION) return;

        boolean ok = DatabaseHelper.createAppointment(
                userEmail, patientName, start, procedure, doctor);
        if (ok) {
            new AppointmentSuccess(patientName, procedure,
                    start.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")),
                    userEmail, userName);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Error saving appointment. Please try again.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private LocalDate getSelectedDate() {
        Date d = (Date) dateSpinner.getValue();
        return d.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
    }

    private LocalDateTime buildDateTime(String timeStr) {
        LocalDate date  = getSelectedDate();
        String[]  parts = timeStr.split(":");
        return date.atTime(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
    }
}
