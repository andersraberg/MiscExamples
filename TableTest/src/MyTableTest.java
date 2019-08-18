import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;

public class MyTableTest {
    private static final Logger LOGGER = Logger.getLogger(MyTableTest.class.getName());

    private static DefaultTableModel _tableModel;
    private static JTable _table;
    private static File _lastFile;

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
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(createButton("UP", ev -> move(-1)));
        buttonPanel.add(createButton("DOWN", ev -> move(1)));
        buttonPanel.add(createButton("PRINT", ev -> print()));
        buttonPanel.add(createButton("DELETE", ev -> delete()));
        buttonPanel.add(createButton("DUPLICATE", ev -> duplicate()));
        buttonPanel.add(createButton("OPEN FILE", ev -> openFile()));
        buttonPanel.add(createButton("SAVE FILE", ev -> saveFile()));

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.add(_table);
        topPanel.add(buttonPanel);

        System.out.println(topPanel.getLayout().getClass().getName());

        frame.add(topPanel);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(new Dimension(400, 300));
        frame.setVisible(true);
    }

    private static JButton createButton(String name, ActionListener al) {
        JButton button = new JButton(name);
        button.addActionListener(al);
        return button;
    }

    private static void openFile() {
        JFileChooser fc = new JFileChooser(_lastFile);
        int returnVal = fc.showOpenDialog(_table);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            _lastFile = fc.getSelectedFile();
            try {
                List<List<String>> collect = Files.readAllLines(_lastFile.toPath()).stream()
                        .map(s -> Arrays.asList(s.split(" "))).collect(Collectors.toList());
                _tableModel.setRowCount(0);
                collect.forEach(r -> _tableModel.addRow(r.toArray(new String[0])));
            } catch (IOException e) {
                LOGGER.log(Level.WARNING, "", e);
            }
        }
    }

    private static void saveFile() {
        JFileChooser fc = new JFileChooser(_lastFile);
        int returnVal = fc.showSaveDialog(_table);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            _lastFile = fc.getSelectedFile();

            try (PrintWriter pw = new PrintWriter(new FileWriter(_lastFile, false))) {
                for (int r = 0; r < _tableModel.getRowCount(); r++) {
                    for (int c = 0; c < _tableModel.getColumnCount(); c++) {
                        pw.print(_tableModel.getValueAt(r, c) + " ");
                    }
                    pw.println();
                }
            } catch (IOException e) {
                LOGGER.log(Level.WARNING, "", e);
            }
        }

    }

    private static void duplicate() {
        Arrays.stream(_table.getSelectedRows()).boxed().sorted(Collections.reverseOrder())
                .forEach(i -> _tableModel.insertRow(i, (Vector<?>) _tableModel.getDataVector().get(i).clone()));
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
