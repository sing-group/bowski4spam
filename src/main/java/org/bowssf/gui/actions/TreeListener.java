package org.bowssf.gui.actions;

import org.bowssf.util.TreeUtils;
import org.scijava.swing.checkboxtree.CheckBoxNodeData;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultMutableTreeNode;



/**
 * This class is for the tree listener
 *
 *
 * @author Alejandro Borrajo Viéitez
 */
public class TreeListener implements TreeModelListener {


    /**
     * See if node changed
     * @param e the node event
     */
    @Override
    public void treeNodesChanged(final TreeModelEvent e) {

        // recursively toggle the subtree to match
        Object[] children = e.getChildren();

        for (final Object child : children) {
            final DefaultMutableTreeNode node = (DefaultMutableTreeNode) child;
            final CheckBoxNodeData data = TreeUtils.getData(node);
            assert data != null;
            TreeUtils.toggleDown(node, data.isChecked());
        }
        if(e.getTreePath().getLastPathComponent() != null) {
            DefaultMutableTreeNode parent = (DefaultMutableTreeNode) e.getTreePath().getLastPathComponent();
            if(!parent.toString().equals("Root")) {
                CheckBoxNodeData parentData = TreeUtils.getData(parent);
                DefaultMutableTreeNode changed = (DefaultMutableTreeNode) e.getChildren()[0];
                CheckBoxNodeData changedData = TreeUtils.getData(changed);

                assert parentData != null;
                assert changedData != null;
                if (parentData.isChecked() && !changedData.isChecked()) {
                    TreeUtils.toggleUpFalse(parent);
                } else {
                    TreeUtils.toggleUpTrue(parent);
                }
            }
        }


    }


    /**
     * See if a node is added
     * @param e the event
     */
    @Override
    public void treeNodesInserted(final TreeModelEvent e) {
        System.out.println(System.currentTimeMillis() + ": nodes inserted");
    }

    /**
     * See if a node is removed
     * @param e the node event
     */
    @Override
    public void treeNodesRemoved(final TreeModelEvent e) {
        System.out.println(System.currentTimeMillis() + ": nodes removed");
    }

    /**
     * See if the structure is changed
     * @param e the event
     */
    @Override
    public void treeStructureChanged(final TreeModelEvent e) {
        System.out.println(System.currentTimeMillis() + ": structure changed");
    }

}
