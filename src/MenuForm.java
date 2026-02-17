import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

public class MenuForm extends JFrame {
    private JPanel MenuPrincipal;
    private JButton SigninButton;
    private JButton Createaccountbutton;
    private JButton Button_exit;

    public MenuForm() {

        setContentPane(MenuPrincipal);
        setTitle("Duperly & Lanner - Welcome");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1100, 700);
        setLocationRelativeTo(null);

        // --- 2. Connection Health Check ---
        try (Connection conn = DatabaseHelper.getConnection()) {
            if (conn != null) {
                System.out.println("Database Connection Verified.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "System Error: Cannot reach the database.\nPlease check config.properties.",
                    "Connection Error", JOptionPane.ERROR_MESSAGE);
        }

        // --- 3. Action Listeners ---
        Button_exit.addActionListener(e -> System.exit(0));

        SigninButton.addActionListener(e -> {
            new LogIn(); // Assuming LogIn is your next form
            dispose();
        });

        Createaccountbutton.addActionListener(e -> {
            new CrearCuenta1(); // Opens the first step of registration
            dispose(); // Dispose to keep the workspace clean
        });

        setVisible(true);

    }

        public static void main (String[]args){
            // Run on the Event Dispatch Thread for thread safety
            SwingUtilities.invokeLater(MenuForm::new);
        }
    }



