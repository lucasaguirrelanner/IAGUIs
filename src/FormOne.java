import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FormOne extends JFrame
{
    private JPanel MainPanel;
    private JButton Salir;
    private JButton accederButton;
    private JButton seMeOlvidóMiButton;
    private JTextField textField1;
    private JPasswordField passwordField1;

    //this is our constructor!
    public FormOne()
    {
        setContentPane(MainPanel);
        setTitle("Accede a tu cuenta");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(300, 600);
        setLocation(200, 500 );
        setVisible(true);

        seMeOlvidóMiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
            new FormTwo ();
            }
        });
        accederButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                new Cuenta();

            }
        });
    }
}





