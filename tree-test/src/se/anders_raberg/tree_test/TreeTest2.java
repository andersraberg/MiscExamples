package se.anders_raberg.tree_test;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;

public class TreeTest2 extends JFrame {
    private static final long serialVersionUID = 1L;
    private JTree tree;

    public TreeTest2() {
        // create the root node
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Root");

        Map<String, Object> apa = new HashMap<>();
        Map<String, Object> bepa = new HashMap<>();

        apa.put("eee", 42);
        apa.put("Bepa", bepa);
        bepa.put("rrr", 123);

        addContent(root, "Apa", apa);
        tree = new JTree(root);
        add(new JScrollPane(tree));
        
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        tree.removeAll();

        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setTitle("JTree Example");
        this.pack();
        this.setVisible(true);
    }

    private static void addContent(DefaultMutableTreeNode node, String nodeName, Object content) {
        if (content instanceof Map) {
            
           
            DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(nodeName);
            node.add(newNode);
            ((Map<String, Object>) content).entrySet()
                    .forEach(e -> addContent(newNode, e.getKey(), e.getValue()));

        } else {
            node.add(new DefaultMutableTreeNode(String.format("%s = %s", nodeName, content)));
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TreeTest2::new);
    }
}
