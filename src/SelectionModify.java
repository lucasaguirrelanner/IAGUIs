import javax.swing.*;


public class SelectionModify extends JFrame {

    public SelectionModify(String email) {

        new HistoryView(email);
        dispose();
    }
}
