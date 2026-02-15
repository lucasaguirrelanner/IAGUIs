//these are my GUI imports
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

//these are my database imports
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

//these are my file imports
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
    setLocationRelativeTo(null);
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

                String isFamilyAccount = combo_famacc.getSelectedItem().toString();

            }
        });
    Continue.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {

            String fName= firstnameField1.getName().trim();
            String lName = lastnameField1.getName().trim();
            String email = mailField1.getText().trim();


            Properties properties = new Properties();

            try (InputStream input = new FileInputStream("src/config.properties")) {
                properties.load(input);

                // 1. Get info from your config file
                String db_url = properties.getProperty("db.url");
                String db_user = properties.getProperty("db.user"); // Fixed from "db.name"
                String db_pass = properties.getProperty("db.password");

                // 2. Try to connect
                try (Connection conn = DriverManager.getConnection(db_url, db_user, db_pass)) {
                    System.out.println("Success! Connected to the DL Database.");

                    //Directing the user to the Family Account Menu in case they have responded to Yes
                    String isFamilyAccount = combo_famacc.getSelectedItem().toString();

                    if (isFamilyAccount.equals("Yes")) {
                        new FamilyAccount();
                        dispose();
                    } else
                        new CrearCuenta2();
                    {

                    }
                }
            } catch (Exception ex) {
                // This catches BOTH file errors and database errors
                JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
                ex.printStackTrace();
            }

            if(fName.isEmpty()||lName.isEmpty()|| email.isEmpty())
            {
                JOptionPane.showMessageDialog(null, " Please fill in all fields before continuing ");
                return;
            }else {
                saveToDatabase(fName, lName, email);

            }
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


