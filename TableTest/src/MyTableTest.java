import java.awt.Dimension;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.WindowConstants;
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
        _tableModel.addRow(new Object[] { "A5", "B5", "C5", "D5" });
        _tableModel.addRow(new Object[] { "A6", "B6", "C6", "D6" });
        _tableModel.addRow(new Object[] { "A7", "B7", "C7", "D7" });

        _table = new JTable(_tableModel);
        JPanel panel = new JPanel();
        panel.add(_table);
        JButton upButton = new JButton("UP");
        upButton.addActionListener(ev -> move(-1));
        JButton downButton = new JButton("DOWN");
        downButton.addActionListener(ev -> move(1));
        JButton printButton = new JButton("PRINT");
        printButton.addActionListener(ev -> print());
        JButton deleteButton = new JButton("DELETE");
        deleteButton.addActionListener(ev -> delete());
        JButton duplicateButton = new JButton("DUPLICATE");
        duplicateButton.addActionListener(ev -> duplicate());
        panel.add(upButton);
        panel.add(downButton);
        panel.add(printButton);
        panel.add(deleteButton);
        panel.add(duplicateButton);
        frame.add(panel);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(new Dimension(400, 300));
        frame.setVisible(true);
    }

    private static void duplicate() {
        Arrays.stream(_table.getSelectedRows()).boxed().sorted(Collections.reverseOrder())
                .forEach(i -> _tableModel.insertRow(i, _tableModel.getDataVector().get(i)));
    }

    private static void delete() {
        Arrays.stream(_table.getSelectedRows()).boxed().sorted(Collections.reverseOrder())
                .forEach(i -> _tableModel.removeRow(i));
    }

    private static void print() {
        Arrays.stream(_table.getSelectedRows()).forEach(i -> {
            System.out.println(i + " : " + _tableModel.getDataVector().get(i));
        });

    }

    private static void move(int step) {
        List<Integer> selectedRows = Arrays.stream(_table.getSelectedRows()).boxed().collect(Collectors.toList());
        if (step > 0) {
            Collections.reverse(selectedRows);
        }
        selectedRows.forEach(i -> {
            int endRow = Math.min(Math.max(i + step, 0), _tableModel.getRowCount() - 1);
            _tableModel.moveRow(i, i, endRow);
            _table.removeRowSelectionInterval(i, i);
            _table.addRowSelectionInterval(endRow, endRow);
        });
    }

}
