import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

public class FAMAPPOINTMENT extends JFrame {

    private static final String MYSELF_LABEL = "Myself (account holder)";
    private JPanel FAMACCPanel;
    private final String contactEmail;
    private final String parentName;
    private final String userName;
    private JComboBox<String> memberCombo;
    private JComboBox<String> doctorCombo;
    private JComboBox<String> procedureCombo;
    private JComboBox<String> timeCombo;
    private JSpinner dateSpinner;
    private JButton checkButton, confirmButton, backButton;
    private JLabel statusLabel;
    private List<String[]> memberDetails;

    public FAMAPPOINTMENT(String email, String name) {
        this.contactEmail = email;
        this.userName = name;
        this.parentName = name;
//Panel:
        setTitle("Duperly & Lanner Grupo Dental – Family Appointment");
        setSize(820, 650);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        buildUI();
        loadMembers();
        loadDoctors();

        setVisible(true);
    }

    private void buildUI() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(new Color(30, 30, 60));

        JLabel header = new JLabel("Family Member Appointment", SwingConstants.CENTER);
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

        // Booking For
        addLabel(form, "Booking For:", g, 0, 0);
        memberCombo = new JComboBox<>();
        memberCombo.addActionListener(e -> loadDoctors());
        styleCombo(memberCombo);
        g.gridx = 1; g.gridy = 0; g.weightx = 1;
        form.add(memberCombo, g);

        // Doctor
        addLabel(form, "Select Doctor:", g, 0, 1);
        doctorCombo = new JComboBox<>();
        doctorCombo.addActionListener(e -> loadProcedures());
        styleCombo(doctorCombo);
        g.gridx = 1; g.gridy = 1;
        form.add(doctorCombo, g);

        // Procedure
        addLabel(form, "Select Procedure:", g, 0, 2);
        procedureCombo = new JComboBox<>();
        styleCombo(procedureCombo);
        g.gridx = 1; g.gridy = 2;
        form.add(procedureCombo, g);

        // Date
        addLabel(form, "Select Date:", g, 0, 3);
        dateSpinner = new JSpinner(new SpinnerDateModel());
        dateSpinner.setEditor(new JSpinner.DateEditor(dateSpinner, "dd/MM/yyyy"));
        dateSpinner.setValue(new Date(System.currentTimeMillis() + 86400000L));
        g.gridx = 1; g.gridy = 3;
        form.add(dateSpinner, g);

        // Time
        addLabel(form, "Select Time:", g, 0, 4);
        timeCombo = new JComboBox<>(new String[]{
                "08:00","09:00","10:00","11:00","13:00","14:00","15:00","16:00"
        });
        styleCombo(timeCombo);
        g.gridx = 1; g.gridy = 4;
        form.add(timeCombo, g);

        // Status
        statusLabel = new JLabel(" ", SwingConstants.CENTER);
        statusLabel.setForeground(Color.YELLOW);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 12));
        g.gridx = 0; g.gridy = 5; g.gridwidth = 2;
        form.add(statusLabel, g);

        root.add(form, BorderLayout.CENTER);


        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));

//Checking dentist availability:
        btnPanel.setBackground(new Color(30, 30, 60));
        checkButton = styledButton("Check Availability", new Color(80, 140, 200));

//Confirm booking button:
        confirmButton = styledButton("Confirm Booking", new Color(228, 122, 50));
        backButton = styledButton("Back", new Color(100, 100, 100));
        btnPanel.add(checkButton);
        btnPanel.add(confirmButton);
        btnPanel.add(backButton);
        root.add(btnPanel, BorderLayout.SOUTH);
        setContentPane(root);

        checkButton.addActionListener(e -> checkAvailability());
        confirmButton.addActionListener(e -> bookAppointment());
        backButton.addActionListener(e -> {
            new Account(contactEmail, userName);
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

    //All family members are loaded so they are options that the user can choose to make appointments for:
    private void loadMembers() {
        memberDetails = DatabaseHelper.getFamilyMembersDetailed(contactEmail);
        memberCombo.removeAllItems();
        memberCombo.addItem(MYSELF_LABEL);
        for (String[] m : memberDetails) memberCombo.addItem(m[1]);
    }
    //Retreiving all available doctors:
    private void loadDoctors() {
        doctorCombo.removeAllItems();


        List<String> names = isBookingForSelf()
                ? DatabaseHelper.getDentistNames()
                : DatabaseHelper.getDentistNamesForChildren();

        if (names.isEmpty()) {
            if (isBookingForSelf()) {
                doctorCombo.addItem("Dr. Smith");
            } else {
                JOptionPane.showMessageDialog(this,
                        "No paediatric dentists found in the database.\n"
                        ,
                        "No Paediatric Dentist", JOptionPane.WARNING_MESSAGE);
            }
        } else {
            for (String d : names) doctorCombo.addItem(d);
        }
        loadProcedures();
    }
    //All  procedures accessible to a specific dentist are taken into account and displayed here:
    private void loadProcedures() {
        String doctor = (String) doctorCombo.getSelectedItem();
        if (doctor == null) return;
        procedureCombo.removeAllItems();
        for (String p : DatabaseHelper.getProceduresForDentist(doctor)) procedureCombo.addItem(p);
        refreshBlockedSlots();
    }

//Preventing overlapping:

    private void refreshBlockedSlots() {
        String doctor = (String) doctorCombo.getSelectedItem();
        if (doctor == null) return;
        LocalDate date = getSelectedDate();


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
//Removing unavailable slots as options:
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
    //Checking if there is availability:
    private void checkAvailability() {
        String doctor = (String) doctorCombo.getSelectedItem();
        String procedure = (String) procedureCombo.getSelectedItem();
        String time = (String) timeCombo.getSelectedItem();
        if (doctor == null || procedure == null || time == null) {
            statusLabel.setText("Please select all fields.");
            return;
        } //Slot is available:
        int dur = DatabaseHelper.getProcedureDuration(procedure);
        LocalDateTime start = buildDateTime(time);
        if (DatabaseHelper.isSlotAvailable(doctor, start, dur)) {
            statusLabel.setText("✔ Available! Ends at: " +
                    start.plusMinutes(dur).format(DateTimeFormatter.ofPattern("HH:mm")));
            statusLabel.setForeground(Color.GREEN);
        } else { //Slot is unavailable:
            statusLabel.setText("✘ Slot unavailable. Choose another time.");
            statusLabel.setForeground(Color.RED);
        }
    }

//For officially booking an appointment:

    private void bookAppointment() {
        String doctor    = (String) doctorCombo.getSelectedItem();
        String procedure = (String) procedureCombo.getSelectedItem();
        String time      = (String) timeCombo.getSelectedItem();

        if (doctor == null || procedure == null || time == null) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.");
            return;
        }

        LocalDateTime start = buildDateTime(time);
        int           dur   = DatabaseHelper.getProcedureDuration(procedure);

        if (!DatabaseHelper.isSlotAvailable(doctor, start, dur)) {
            JOptionPane.showMessageDialog(this, "Slot is taken. Choose another time.",
                    "Unavailable", JOptionPane.WARNING_MESSAGE);
            return;
        }

        boolean self = isBookingForSelf();
        String  patientName;
        Integer familyId    = null;
        boolean isFamilyMbr = false;

        if (self) {
            patientName = parentName != null
                    ? parentName
                    : DatabaseHelper.getFirstName(contactEmail);

            // One appointment per day enforcement— parent
            if (DatabaseHelper.patientHasAppointmentOnDate(contactEmail, start.toLocalDate())) {
                JOptionPane.showMessageDialog(this,
                        "You already have an appointment on this date.\n" +
                                "Only one appointment per day is allowed.",
                        "Booking Not Allowed", JOptionPane.WARNING_MESSAGE);
                return;
            }

        } else {
            int idx = memberCombo.getSelectedIndex() - 1;
            if (idx < 0 || idx >= memberDetails.size()) {
                JOptionPane.showMessageDialog(this, "Please select a valid patient.");
                return;
            }
            String[] member = memberDetails.get(idx);
            patientName = member[1];
            familyId    = Integer.parseInt(member[0]);
            isFamilyMbr = true;

            // One appointment per day enforcement — family member
            if (DatabaseHelper.familyMemberHasAppointmentOnDate(familyId, start.toLocalDate())) {
                JOptionPane.showMessageDialog(this,
                        patientName + " already has an appointment on this date.\n" +
                                "Only one appointment per day is allowed.",
                        "Booking Not Allowed", JOptionPane.WARNING_MESSAGE);
                return;
            }
        }

//For calculating the end time of an appointment:
        String endTime = start.plusMinutes(dur).format(DateTimeFormatter.ofPattern("HH:mm"));
        String msg = String.format(
                "<html><b>Confirm appointment%s:</b><br><br>" +
                        "Patient:   <b>%s</b><br>" +
                        "Doctor:    <b>Dr. %s</b><br>" +
                        "Procedure: <b>%s</b><br>" +
                        "Date:      <b>%s</b><br>" +
                        "Time:      <b>%s – %s</b><br><br>" +
                        "Confirmation sent to: <i>%s</i></html>",
                self ? "" : " for family member",
                patientName, doctor, procedure,
                start.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                time, endTime, contactEmail);

        int choice = JOptionPane.showConfirmDialog(this, msg,
                "Confirm Appointment", JOptionPane.YES_NO_OPTION);
        if (choice != JOptionPane.YES_OPTION) return;

//the boolean establishes that a new appointment has been officially created:

        boolean ok = DatabaseHelper.createAppointment(
                contactEmail, patientName, start, procedure, doctor, isFamilyMbr, familyId);

        if (ok) {
            new AppointmentSuccess(
                    patientName, procedure,
                    start.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")),
                    contactEmail, userName);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Error saving appointment. Please try again.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    //Identifying if the booking is for the account holder, not a family member:
    private boolean isBookingForSelf() {
        return MYSELF_LABEL.equals(memberCombo.getSelectedItem());
    }

    private LocalDate getSelectedDate() {
        Date d = (Date) dateSpinner.getValue();
        return d.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
    }

    private LocalDateTime buildDateTime(String time) {
        String[] p = time.split(":");
        return getSelectedDate().atTime(Integer.parseInt(p[0]), Integer.parseInt(p[1]));
    }
}
