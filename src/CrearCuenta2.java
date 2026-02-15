import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CrearCuenta2 extends JFrame

{
    private JPanel AccSetUp2;
    private JTextField txt_username;
    private JPasswordField password;
    private JPasswordField passwordVF;
    private JButton resetButton;
    private JButton createAccountButton;
    private JButton goBackButton;

    public CrearCuenta2()
    {
        if (AccSetUp2 == null) {
            AccSetUp2 = new JPanel();
        }
        setContentPane(AccSetUp2);
        setTitle("You're almost finished creating your account! ");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(800, 700);
        setLocationRelativeTo(null);
        setVisible(true);


        createAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                txt_username.setText("");
                password.setText("");
                passwordVF.setText("");

                if (password != passwordVF)
                {

                }
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

