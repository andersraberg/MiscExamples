import java.awt.Dimension;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class MyTableTest {

    private static DefaultTableModel _tableModel;
    private static JTable _table;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Table fun");
        _tableModel = new DefaultTableModel();
        _tableModel.setColumnIdentifiers(new String[] { "A", "B", "C", "D" });
        _tableModel.addRow(new Object[] { "A1", "B1", "C1", "D1" });
        _tableModel.addRow(new Object[] { "A2", "B2", "C2", "D2" });
        _tableModel.addRow(new Object[] { "A3", "B3", "C3", "D3" });
        _tableModel.addRow(new Object[] { "A4", "B4", "C4", "D4" });

        _table = new JTable(_tableModel);
        JPanel panel = new JPanel();
        panel.add(_table);
        JButton upButton = new JButton("UP");
        upButton.addActionListener(ev -> move(-1));
        JButton downButton = new JButton("DOWN");
        downButton.addActionListener(ev -> move(1));
        JButton printButton = new JButton("PRINT");
        printButton.addActionListener(ev -> print());
        panel.add(upButton);
        panel.add(downButton);
        panel.add(printButton);
        frame.add(panel);
        frame.pack();
        frame.setSize(new Dimension(400, 300));
        frame.setVisible(true);
    }

    private static void print() {
        Arrays.stream(_table.getSelectedRows()).forEach(i -> {
            System.out.println(_tableModel.getValueAt(i, 0));
        });

    }

    private static void move(int step) {
        Arrays.stream(_table.getSelectedRows()).forEach(i -> {
            int endRow = Math.min(Math.max(i + step, 0), _tableModel.getRowCount());
            _tableModel.moveRow(i, i, endRow);
            _table.removeRowSelectionInterval(i, i);
            _table.addRowSelectionInterval(endRow, endRow);
        });
    }

}
