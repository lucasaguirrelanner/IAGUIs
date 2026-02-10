import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

//sql imports(database

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

//file imports
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class FormOne extends JFrame
{
    private JPanel MainPanel;
    private JButton GoBackButton;
    private JButton LoginButton;
    private JButton ForgotMyButton;
    private JPasswordField txt_password;
    private JLabel txt_login;
    private JLabel txt_username;
    private JLabel txt_pw;
    private JTextField txt_user;

    //this is our constructor!
    public FormOne()
    {
        setContentPane(MainPanel);
        setTitle("Accede a tu cuenta");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(600, 600);
        setLocation(200, 500 );
        setVisible(true);

        ForgotMyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
            new FormTwo ();
            }
        });
        LoginButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                new Cuenta();
                dispose();

            }
        });
        GoBackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new MenuForm();
                dispose();
            }
        });
    }
}





