import javax.swing.*;
import java.sql.Connection;

public class MenuForm extends JFrame {
    private JPanel MenuPrincipal;
    private JButton SigninButton;
    private JButton Createaccountbutton;
    private JButton Button_exit;

    public MenuForm() {
        setContentPane(MenuPrincipal);
        // Criterion #10 – official name in title
        setTitle("Duperly & Lanner Grupo Dental – Welcome");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1100, 700);
        setLocationRelativeTo(null);

        // Verify DB connection on startup
        try (Connection conn = DatabaseHelper.getConnection()) {
            if (conn != null) System.out.println("Database connection verified.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Cannot connect to the database.\nCheck config.properties.",
                    "Connection Error", JOptionPane.ERROR_MESSAGE);
        }

        Button_exit.addActionListener(e -> System.exit(0));

        SigninButton.addActionListener(e -> {
            new LogIn();
            dispose();
        });

        Createaccountbutton.addActionListener(e -> {
            new CreateAccount1();
            dispose();
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        // Criterion #9 – start reminder service when app launches
        ReminderService.start();

        SwingUtilities.invokeLater(MenuForm::new);
    }
}
