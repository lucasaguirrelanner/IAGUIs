//GUI imports

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;




public class MenuForm extends JFrame {
    private JPanel MenuPrincipal;
    private JButton SigninButton;
    private JButton Createaccountbutton;
    private JButton Button_exit;

    //this is our constructor!
    public MenuForm() {
        setContentPane(MenuPrincipal);
        setTitle("Welcome!!");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(950, 700);
        setLocationRelativeTo(null);
        setVisible(true);

try (Connection conn=DatabaseHelper.getConnection()) {
    if (conn != null)
    {
        System.out.println(" Connection Successful! ");
    }

}catch(Exception e) {
    javax.swing.JOptionPane.showMessageDialog(this, "Database Error:" + e.getMessage());
    e.printStackTrace();
}

        Button_exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0); //Closed the applciation

            }
        });


        SigninButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //JOptionPane.showMessageDialog( null, " You Pressed Button 1 :-) ");
                new FormOne();
                dispose();

            }


        });

        Createaccountbutton.addActionListener(new ActionListener() {
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

