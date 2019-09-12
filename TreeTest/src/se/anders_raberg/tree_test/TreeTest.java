package se.anders_raberg.tree_test;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

public class TreeTest extends JFrame {
    private static final long serialVersionUID = 1L;
    private JTree tree;
    private JLabel selectedLabel;

    public TreeTest() {
        // create the root node
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Root");
        // create the child nodes
        DefaultMutableTreeNode english = new DefaultMutableTreeNode("English");
        english.add(new DefaultMutableTreeNode("Alpha"));
        english.add(new DefaultMutableTreeNode("Bravo"));
        english.add(new DefaultMutableTreeNode("Charlie"));
        english.add(new DefaultMutableTreeNode("Delta"));
        DefaultMutableTreeNode swedish = new DefaultMutableTreeNode("Swedish");
        swedish.add(new DefaultMutableTreeNode("Adam"));
        swedish.add(new DefaultMutableTreeNode("Bertil"));
        swedish.add(new DefaultMutableTreeNode("Ceasar"));
        swedish.add(new DefaultMutableTreeNode("David"));
        root.add(english);
        root.add(swedish);

        tree = new JTree(root);
        add(new JScrollPane(tree));
        selectedLabel = new JLabel();
        JButton button = new JButton("Do it");
        JButton button2 = new JButton("Refresh");
        JPopupMenu pm = new JPopupMenu();
        pm.add(button2);
        add(selectedLabel, BorderLayout.SOUTH);
        add(button, BorderLayout.SOUTH);

        button.addActionListener(l -> {
            DefaultMutableTreeNode swedish2 = new DefaultMutableTreeNode("Swedish2");
            swedish2.add(new DefaultMutableTreeNode("Erik1"));
            swedish2.add(new DefaultMutableTreeNode("Erik2"));
            swedish.add(swedish2);
        });

        button2.addActionListener(l -> {
            DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
            DefaultTreeModel m = (DefaultTreeModel) tree.getModel();
            m.reload(selectedNode);
            // tree.setExpandsSelectedPaths(true);

            TreePath path = new TreePath(selectedNode.getPath());
            tree.expandPath(path);
            pm.setVisible(false);

        });
        tree.getSelectionModel().addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                selectedLabel.setText(e.getPath().toString());
            }
        });

        tree.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    int row = tree.getClosestRowForLocation(e.getX(), e.getY());
                    tree.setSelectionRow(row);
                    pm.show(tree, e.getX(), e.getY());
                }
            }

        });

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("JTree Example");
        this.pack();
        this.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new TreeTest();

            }
        });

    }
}
