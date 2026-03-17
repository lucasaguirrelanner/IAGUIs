import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

public class MenuForm extends JFrame{
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

        //Establishing connection with DB:

        try (Connection conn = DatabaseHelper.getConnection()){
            if (conn != null) {
                System.out.println("Database Connection Verified.");
            }
        }catch (Exception e) {
            JOptionPane.showMessageDialog (this,
                    "System Error: Cannot reach the database. \nPlease check config.properties.",
                    "Connection Error", JOptionPane.ERROR_MESSAGE);
        }

        Button_exit.addActionListener(e -> System.exit(0));

        //taking user to the sign in dashboard
        SigninButton.addActionListener(e -> {
            new LogIn();
            dispose();

        });

        //taking user to create a new account
        Createaccountbutton.addActionListener(e -> {
            new CreateAccount1();
            dispose();
        });

        setVisible(true);


    }

    public static void main (String[]args){

        SwingUtilities.invokeLater(MenuForm::new);

    }

}
