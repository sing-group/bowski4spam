package org.bowssf.gui.actions;

import org.bowssf.util.JsonUtils;
import org.bowssf.util.TreeUtils;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;

/**
 * This class is for the save action button
 *
 *
 * @author Alejandro Borrajo Viéitez
 */
public class SaveActionListener implements ActionListener {
    private final DefaultMutableTreeNode root;

    /**
     * Constructor for the class
     * @param root the node of the tree
     */
    public SaveActionListener(TreeNode root) {
        ;
        this.root = (DefaultMutableTreeNode) root;
    }

    /**
     * The action performed
     * @param e the action selected
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println(this.root.getFirstChild());
        Map<String, List<String>> interestMap = TreeUtils.getInterest((DefaultMutableTreeNode)this.root.getFirstChild());
        System.out.println(interestMap);
        JsonUtils.save(interestMap);

    }
}
