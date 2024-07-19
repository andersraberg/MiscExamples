package se.anders_raberg.swing_test.swing7;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.function.Consumer;

import javax.swing.JButton;
import javax.swing.JPanel;

public class Toolbar extends JPanel implements ActionListener {
    private static final long serialVersionUID = 1L;
    private final JButton helloButton;
    private final JButton goodbyeButton;
    private final Consumer<String> _textListener;

    public Toolbar(Consumer<String> textListener) {
        _textListener = textListener;
        helloButton = new JButton("Hello");
        goodbyeButton = new JButton("Goodbye");

        helloButton.addActionListener(this);
        goodbyeButton.addActionListener(this);

        setLayout(new FlowLayout(FlowLayout.LEFT));

        add(helloButton);
        add(goodbyeButton);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton clicked = (JButton) e.getSource();

        if (clicked == helloButton) {
            _textListener.accept("Hello\n");
        } else if (clicked == goodbyeButton) {
            _textListener.accept("Goodbye\n");
        }

    }
}
