package gui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.text.DefaultCaret;

public class LogPanel extends JPanel {

    private static final int MAX_CHAR_COUNT = 30_000;
    private final JTextArea area = new JTextArea();

    public LogPanel() {
        super(new BorderLayout());

        area.setEditable(false);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setMargin(new Insets(8, 8, 8, 8));
        area.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        ((DefaultCaret) area.getCaret()).setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        JScrollPane scroll = new JScrollPane(area,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setBorder(BorderFactory.createTitledBorder("Combat Log"));
        add(scroll, BorderLayout.CENTER);
    }

    /** Safe to call from any thread. */
    public void append(String msg) {
        if (msg == null) return;
        SwingUtilities.invokeLater(() -> {
            area.append(msg + "\n");
            if (area.getDocument().getLength() > MAX_CHAR_COUNT) {
                try {
                    area.replaceRange("", 0, area.getLineEndOffset(100));
                } catch (Exception ignored) {
                    // not enough lines yet
                }
            }
        });
    }

    public void clear() {
        if (SwingUtilities.isEventDispatchThread()) {
            area.setText("");
        } else {
            SwingUtilities.invokeLater(() -> area.setText(""));
        }
    }
}
