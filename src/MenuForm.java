import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuForm extends JFrame
{
    private JPanel MenuPrincipal;
    private JButton button1Button;
    private JButton button2Button;
    private JButton button_3;
    private JButton button_4;
    private JButton Button_exit;

    //this is our constructor!
    public MenuForm()
    {
        setContentPane(MenuPrincipal);
        setTitle( "Bienvenido/a!");
        setDefaultCloseOperation( EXIT_ON_CLOSE );
        setSize( 300, 600 );
        setLocationRelativeTo(null);
        setVisible( true );


        Button_exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                System.exit (0); //Closed the applciation

            }
        });


        button1Button.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                //JOptionPane.showMessageDialog( null, " You Pressed Button 1 :-) ");
                new FormOne();

            }


        });


        button2Button.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
              JOptionPane.showMessageDialog( null, " You Pressed Button 2 :-) ");


            }
        });


        button_3.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                JOptionPane.showMessageDialog( null, " You Pressed Button 3 :-) ");

            }
        });


        button_4.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                JOptionPane.showMessageDialog( null, " You Pressed Button 4 :-) ");

            }
        });
    }

    public static void main(String[]args)
    {
        //this is our entry point
        new MenuForm();
    }
}
