import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CrearCuenta1 extends JFrame

{
    private JPanel MainPanel;
    private JTextField txt_Nombre;
    private JTextField txt_apellido;
    private JTextField txt_correo;
    private JComboBox combo_familiar;
    private JButton Continuar;
    private JButton borrarTodoButton;



public CrearCuenta1 ()
{
    setContentPane(MainPanel);
    setTitle("Crea una cuenta");
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    setSize(300, 600);
    setLocation(200, 500);
    setVisible(true);

        borrarTodoButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {

                txt_apellido.setText("");
                txt_correo.setText("");
                txt_Nombre.setText("");

            }
        });
        Continuar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {

            }
        });
    }
}
