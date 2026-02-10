import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class Cuenta extends JFrame
{
    private JPanel MainPanel;
    private JButton bookappointment;
    private JButton viewYourAppointmentsButton;
    private JButton modifyMyAccountButton;
    private JButton gobackbutton;
    private JButton exitButton;

    //this is our constructor!
    public Cuenta()
    {
        setContentPane(MainPanel);
        setTitle("Cuenta");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(600, 600);
        setLocation(200, 500);
        setVisible(true);


        exitButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                JFrame Cuenta = (JFrame) SwingUtilities.getWindowAncestor(exitButton);
                Cuenta.dispose();
            }
        });
    }
}
