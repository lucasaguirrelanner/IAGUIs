import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Properties;
import javax.swing.JOptionPane;

public class APPOINTMENT extends JFrame

{
    private JPanel MainPanel;
    private JPanel panel1;
    private JComboBox comboProcedure;
    private JButton CHECKAVAILABILITYButton;
    private JLabel ProcedureLabel;


public APPOINTMENT()
{
    setContentPane(MainPanel);
    setTitle("CREATE AN APPOINTMENT");
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    setSize(600, 600);
    setLocationRelativeTo(null);
    setVisible(true);

    comboProcedure.addItem(" I need a diagnostic ");
    comboProcedure.addItem(" Oral Rehabilitation ");
    comboProcedure.addItem(" Ortodontics ");
    comboProcedure.addItem(" Periodontics ");
    comboProcedure.addItem(" Implantology ");
    comboProcedure.addItem(" Edodontics ");

}


}

