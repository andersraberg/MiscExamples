package se.anders_raberg.swing_test.swing7;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class TextPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private final JTextArea _textArea;

    public TextPanel() {
        _textArea = new JTextArea();
        setLayout(new BorderLayout());
        add(new JScrollPane(_textArea), BorderLayout.CENTER);
    }

    public void appendText(String text) {
        _textArea.append(text);
    }
}
