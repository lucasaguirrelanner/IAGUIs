import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CrearCuenta2 extends JFrame

{
    private JPanel MainPanel;
    private JTextField txt_username;
    private JPasswordField password;
    private JPasswordField passwordVF;
    private JButton resetButton;
    private JButton createAccountButton;
    private JButton goBackButton;

    public CrearCuenta2()
    {
        setContentPane(MainPanel);
        setTitle("You're almost finished creating your account! ");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(600, 600);
        setLocation(50, 500);
        setVisible(true);


        createAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Account_Created();
            }
        });
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                txt_username.setText("");
                password.setText("");
                passwordVF.setText("");
            }
        });
        goBackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new CrearCuenta1();
                dispose();
            }
        });
    }
}

