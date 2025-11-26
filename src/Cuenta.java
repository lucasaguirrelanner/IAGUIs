import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class Cuenta extends JFrame
{
    private JPanel MainPanel;
    private JButton AgendarunacitaButton;
    private JButton verMisCitasButton;
    private JButton cambiarMiCuentaButton;
    private JButton salirButton;

    //this is our constructor!
    public Cuenta()
    {
        setContentPane(MainPanel);
        setTitle("Cuenta");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(300, 600);
        setLocation(200, 500);
        setVisible(true);


        salirButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                JFrame Cuenta = (JFrame) SwingUtilities.getWindowAncestor(salirButton);
                Cuenta.dispose();
            }
        });
    }
}
