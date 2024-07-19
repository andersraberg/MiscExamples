package se.anders_raberg.java2d_test;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Java2DTest extends JFrame {
    private static final long serialVersionUID = 1L;

    public Java2DTest() {
        super("Lines Drawing Demo");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    @Override
    public void paint(Graphics g) {
        System.out.println("X");
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawLine(120, 50, 360, 50);
        g2d.draw(new Line2D.Double(59.2d, 99.8d, 419.1d, 99.8d));
        g2d.draw(new Line2D.Float(21.50f, 132.50f, 459.50f, 132.50f));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Java2DTest java2dTest = new Java2DTest();
            java2dTest.setEnabled(true);
            java2dTest.setVisible(true);
        });
    }
}
