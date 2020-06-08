package org.bowssf.gui.actions;


import org.bowssf.util.TreeUtils;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/**
 * This class is for searching nodes
 *
 *
 * @author Alejandro Borrajo Viéitez
 */
public class SearchActionListener implements ActionListener {
    private final JTextField Jfield;
    private final DefaultMutableTreeNode root;
    private final JTree tree;

    /**
     * Constructor of the class
     * @param Jfield the field to search
     * @param root the node of the tree
     * @param tree the entire tree
     */
    public SearchActionListener(JTextField Jfield, TreeNode root, JTree tree) {
        this.Jfield = Jfield;
        this.root = (DefaultMutableTreeNode) root;
        this.tree = tree;
    }

    /**
     * The action performed
     * @param e the selected action
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        DefaultMutableTreeNode node = TreeUtils.find(root,Jfield.getText());
        if(node!=null) {
            TreePath path = new TreePath(node.getPath());
            for (int i = 0; i < tree.getRowCount(); i++) {
                tree.collapseRow(i);
            }

            tree.scrollPathToVisible(path);
            tree.setSelectionPath(path);
        }
    }
}
