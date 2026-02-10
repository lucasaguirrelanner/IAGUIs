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

public class CrearCuenta1 extends JFrame

{
    private JPanel MainPanel;
    private JButton Continue;
    private JButton ResetButton;
    private JButton GoBackButton;
    private JComboBox combo_famacc;
    private JTextField firstnameField1;
    private JTextField lastnameField1;
    private JTextField mailField1;
    private JLabel firstLabel;
    private JLabel lastLabel;
    private JLabel mailLabel;
    private JLabel famaccLabel;



    public CrearCuenta1 ()
{
    setContentPane(MainPanel);
    setTitle("Crea una cuenta");
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    setSize(800, 700);
    setLocation(50, 500);
    setVisible(true);

    combo_famacc.addItem("Yes");
    combo_famacc.addItem("No");
    combo_famacc.setSelectedItem("Yes");

        ResetButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {

               firstnameField1.setText("");
               mailField1.setText("");
                lastnameField1.setText("");

                combo_famacc.setSelectedItem("Yes");

            }
        });
        Continue.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                new CrearCuenta2();
                dispose();
            }
        });
    Continue.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            new CrearCuenta2();
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
