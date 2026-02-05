import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Account_Created1 extends JFrame{
    private JPanel panel1;
    private JButton Gobackbutton;

    public Account_Created1(){
        setContentPane(panel1);
        setTitle("Your account has been successfully created!! ");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(600, 600);
        setLocation(50, 500);
        setVisible(true);
        Gobackbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new MenuForm();
                dispose();
            }
        });
    }

}
