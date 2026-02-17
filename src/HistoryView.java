import javax.swing.*;
import java.awt.*;

public class HistoryView extends JFrame {
    public HistoryView(String email) {
        setTitle("Your Appointment History");
        setSize(500, 400);
        setLocationRelativeTo(null);
        JLabel label = new JLabel("History and Notes for: " + email, SwingConstants.CENTER);
        add(label);
        setVisible(true);
    }
}
