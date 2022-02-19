package se.anders_raberg.tree_test;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

public class TreeTest3 extends JFrame {
    private static final long serialVersionUID = 1L;
    private JTree tree;

    public TreeTest3() {
        
        class MyTreeModel implements TreeModel {
            
            private Map<String, Object> mapp;

            MyTreeModel(Map<String, Object> mapp) {
                this.mapp = mapp;
            }

            @Override
            public Object getRoot() {
                return "Hello";
            }

            @Override
            public Object getChild(Object parent, int index) {
                return null;
            }

            @Override
            public int getChildCount(Object parent) {
                return 0;
            }

            @Override
            public boolean isLeaf(Object node) {
                return false;
            }

            @Override
            public void valueForPathChanged(TreePath path, Object newValue) {
            }

            @Override
            public int getIndexOfChild(Object parent, Object child) {
                return 0;
            }

            @Override
            public void addTreeModelListener(TreeModelListener l) {
            }

            @Override
            public void removeTreeModelListener(TreeModelListener l) {
            }
            
        }
        
        // create the root node
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Root");
        DefaultTreeModel model = new DefaultTreeModel(root);
        
//        model.

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
    
    
//    private void updateTree1(DefaultMutableTreeNode node) {
//        node.getc
//        
//    }
//
//    private void updateTree2(DefaultMutableTreeNode node) {
//        
//    }
//
//    private void updateTree3(DefaultMutableTreeNode node) {
//        
//    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(TreeTest3::new);
    }
}
