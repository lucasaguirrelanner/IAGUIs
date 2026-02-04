import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuForm extends JFrame {
    private JPanel MenuPrincipal;
    private JButton accederAMiCuentaButton;
    private JButton Crearunacuentabutton;
    private JButton Button_salir;

    //this is our constructor!
    public MenuForm() {
        setContentPane(MenuPrincipal);
        setTitle("Bienvenido/a!");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(300, 600);
        setLocationRelativeTo(null);
        setVisible(true);


        Button_salir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0); //Closed the applciation

            }
        });


        accederAMiCuentaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //JOptionPane.showMessageDialog( null, " You Pressed Button 1 :-) ");
                new FormOne();

            }


        });

        Crearunacuentabutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new CrearCuenta1();
                
            }
        });
    }

        public static void main (String[]args)
        {
            //this is our entry point
            new MenuForm();
        }


        }

