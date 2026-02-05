import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CrearCuenta1 extends JFrame

{
    private JPanel MainPanel;
    private JButton Continue;
    private JButton ResetButton;
    private JButton GoBackButton;
    private JLabel txt_lastname;
    private JLabel txt_firstname;
    private JLabel txt_mail;
    private JComboBox combo_famacc;


    public CrearCuenta1 ()
{
    setContentPane(MainPanel);
    setTitle("Crea una cuenta");
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    setSize(600, 600);
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

                txt_firstname.setText("");
               txt_lastname.setText("");
                txt_mail.setText("");

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
