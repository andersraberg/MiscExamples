package se.anders_raberg.swing_test.swing7;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

public class ButtonPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    public ButtonPanel() {
        setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.fill = GridBagConstraints.BOTH;
        gc.gridx = 0;
        gc.gridy = 0;
        gc.gridwidth = 1;
        gc.gridheight = 6;
        add(new JButton("Alpha"), gc);

        gc.gridx = 1;
        gc.gridy = 0;
        gc.gridwidth = 1;
        gc.gridheight = 3;
        add(new JButton("Bravo"), gc);

        gc.gridy = 3;
        gc.gridwidth = 1;
        gc.gridheight = 3;
        add(new JButton("Charlie"), gc);

        gc.gridx = 2;
        gc.gridy = 0;
        gc.gridwidth = 1;
        gc.gridheight = 2;
        add(new JButton("Delta"), gc);

        gc.gridy = 2;
        gc.gridwidth = 1;
        gc.gridheight = 2;
        add(new JButton("Echo"), gc);

        gc.gridy = 4;
        gc.gridwidth = 1;
        gc.gridheight = 2;
        add(new JButton("Foxtrot"), gc);
    }

}
