package org.bowssf.util;

import org.scijava.swing.checkboxtree.CheckBoxNodeData;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import java.util.*;

/**
 * This class with the tree funcionalities
 *
 * @author Alejandro Borrajo Viéitez
 */
public class TreeUtils {

    /**
     * Gets the data from a node
     * @param node the node from the tree
     * @return the data with true of false and the title of the node
     */
    public static CheckBoxNodeData getData(final DefaultMutableTreeNode node) {
        final Object userObject = node.getUserObject();
        if (!(userObject instanceof CheckBoxNodeData)) return null;
        return (CheckBoxNodeData) userObject;
    }

    /**
     * Recursively toggles the check box state of the given subtree.
     */
    public static boolean toggleDown(final DefaultMutableTreeNode node,
                                     final boolean checked) {
        final CheckBoxNodeData data = TreeUtils.getData(node);
        boolean anyChanged = false;
        if (data != null) {
            if (data.isChecked() != checked) {
                data.setChecked(checked);
                anyChanged = true;
            }
        }
        Iterator<TreeNode> childIterator = node.children().asIterator();
        while (childIterator.hasNext()) {
            DefaultMutableTreeNode child = (DefaultMutableTreeNode) childIterator.next();
            final boolean changed = toggleDown(child, checked);
            if (changed) anyChanged = true;
        }
        return anyChanged;
    }

    /**
     * Toggles all tree down to false
     * @param node the start node
     * @return a boolean with the last checked
     */
    public static boolean toggleUpFalse(final DefaultMutableTreeNode node) {
        final CheckBoxNodeData data = getData(node);
        boolean anyChanged = false;
        if (data != null) {
            if (data.isChecked()) {
                data.setChecked(false);
                anyChanged = true;
            }
        }
        if (node.getParent() != null) {
            final boolean changed = toggleUpFalse((DefaultMutableTreeNode) node.getParent());
            if (changed) anyChanged = true;
        }
        return anyChanged;
    }

    /**
     * Toggles all tree down to true
     * @param node the start node
     * @return a boolean with the last checked
     */
    public static boolean toggleUpTrue(final DefaultMutableTreeNode node) {

        Iterator<TreeNode> childs = node.children().asIterator();
        boolean stop = false;
        boolean anyChanged = false;
        while (childs.hasNext() && !stop) {
            if (!Objects.requireNonNull(TreeUtils.getData((DefaultMutableTreeNode) childs.next())).isChecked()) {
                stop = true;
            }
        }
        if (!stop) {
            final CheckBoxNodeData data = getData(node);

            if (data != null) {
                if (!data.isChecked()) {
                    data.setChecked(true);
                    anyChanged = true;
                }
            }
            if (node.getParent() != null) {
                final boolean changed = toggleUpTrue((DefaultMutableTreeNode) node.getParent());
                if (changed) anyChanged = true;
            }
        }

        return anyChanged;
    }

    /**
     * Gets the checkbox value from a node
     * @param node desired node
     * @return a Map with all the data
     */
    public static Map<String, List<String>> getInterest(final DefaultMutableTreeNode node) {
        Map<String, List<String>> interestMap = new HashMap<>();
        List<String> interested = new ArrayList<>();
        ArrayList<String> notInterested = new ArrayList<>();
        JedisSynsetCache jedisSynsetCache = JedisSynsetCache.getInstance();
        if (node != null) {
            final CheckBoxNodeData data = TreeUtils.getData(node);
            assert data != null;
            String[] split = data.getText().replaceAll("\\<.*?\\>", "").split("\\|");
            String word = split[0].trim();
            String gloss = split[1].trim();
            String synset = jedisSynsetCache.getSynsetFromWord(word, gloss);

            if (data.isChecked()) {
                interested.add(synset);
            } else {
                notInterested.add(synset);
            }
            interestMap.put("interested", interested);
            interestMap.put("notInterested", notInterested);
            Iterator<TreeNode> childIterator = node.children().asIterator();
            while (childIterator.hasNext()) {
                DefaultMutableTreeNode child = (DefaultMutableTreeNode) childIterator.next();
                Map<String, List<String>> nextInterestMap = getInterest(child);
                interestMap.get("interested").addAll(nextInterestMap.get("interested"));
                interestMap.get("notInterested").addAll(nextInterestMap.get("notInterested"));
            }

        }

        return interestMap;
    }

    /**
     * Adds a node
     * @param parent the parent o the node
     * @param text the text of the node
     * @param checked the boolean value of the node
     * @return the node added
     */
    public static DefaultMutableTreeNode add(
            final DefaultMutableTreeNode parent, final String text,
            final boolean checked) {
        final CheckBoxNodeData data = new CheckBoxNodeData(text, checked);
        final DefaultMutableTreeNode node = new DefaultMutableTreeNode(data);
        parent.add(node);
        return node;
    }

    /**
     * Finds a node in the tree
     * @param root the parent of the node
     * @param toSearch the node to search
     * @return if it's found, the node.
     */
    public static DefaultMutableTreeNode find(DefaultMutableTreeNode root, String toSearch) {
        DefaultMutableTreeNode node = null;
        String text = Objects.requireNonNull(getData(root)).getText();
        if (text.split("\\|")[0].contains(toSearch)) {
            return root;
        }
        Iterator<TreeNode> childIterator = root.children().asIterator();
        while (childIterator.hasNext()) {
            DefaultMutableTreeNode child = (DefaultMutableTreeNode) childIterator.next();
            node = find(child, toSearch);
            if (node != null) {
                return node;
            }
        }

        return node;


    }
}
