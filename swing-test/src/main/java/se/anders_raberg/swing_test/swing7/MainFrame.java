package se.anders_raberg.swing_test.swing7;

import java.awt.BorderLayout;

import javax.swing.JFrame;

public class MainFrame extends JFrame {
    private static final long serialVersionUID = 1L;
    private final TextPanel textPanel;
    private final Toolbar toolbar;
    private final ButtonPanel buttonPanel;

    public MainFrame() {
        super("Hello World");
        setLayout(new BorderLayout());
        textPanel = new TextPanel();
        toolbar = new Toolbar(textPanel::appendText);
        buttonPanel = new ButtonPanel();
        add(toolbar, BorderLayout.NORTH);
        add(textPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

}
