import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FormTwo extends JFrame
{
    private JPanel MainPanel;
    private JPasswordField passwordField1;
    private JPasswordField passwordField2;
    private JButton Regresar;

    //this is our constructor!
    public FormTwo()
    {
        setContentPane(MainPanel);
        setTitle("Se me olvidó mi contraseña");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(300, 600);
        setLocationRelativeTo(null);
        setVisible(true);


        Regresar.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                JFrame FormTwo = (JFrame) SwingUtilities.getWindowAncestor(Regresar);
                FormTwo.dispose();
            }
        });
    }
}
