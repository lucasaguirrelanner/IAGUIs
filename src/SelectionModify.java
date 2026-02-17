import javax.swing.*;

/**
 * SelectionModify – redirects to HistoryView where
 * cancellation and rescheduling are both handled.
 * SUCCESS CRITERIA #5 – change or cancel with enough time.
 */
public class SelectionModify extends JFrame {

    public SelectionModify(String email) {
        // Delegate directly to the fully-featured HistoryView
        new HistoryView(email);
        dispose();
    }
}
