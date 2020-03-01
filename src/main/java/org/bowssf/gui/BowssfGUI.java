package org.bowssf.gui;

import org.bowssf.gui.actions.SearchAction;
import org.bowssf.util.JedisSynsetCache;
import org.bowssf.util.JedisTree;
import org.scijava.swing.checkboxtree.CheckBoxNodeData;
import org.scijava.swing.checkboxtree.CheckBoxNodeEditor;
import org.scijava.swing.checkboxtree.CheckBoxNodePanel;
import org.scijava.swing.checkboxtree.CheckBoxNodeRenderer;

import javax.swing.*;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Set;

public class BowssfGUI {
    private JButton button1;
    private JTextField textField1;
    private JPanel searchPanel;
    private JPanel mainPanel;
    private JScrollPane jscroll;

    public BowssfGUI() {
        button1.addActionListener(new SearchAction(textField1.getText()));


    }

   /* public static void main(String[] args) {
        JFrame frame = new JFrame("App");
        frame.setContentPane(new BowssfGUI().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
    */
   public static void main(final String args[]) {

       // show the tree onscreen
       final JFrame frame = new JFrame("app");
       BowssfGUI b = new BowssfGUI();
       frame.setContentPane(b.mainPanel);
       frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       frame.setSize(300, 150);
       frame.setVisible(true);
   }

    private static DefaultMutableTreeNode add(
            final DefaultMutableTreeNode parent, final String text,
            final boolean checked)
    {
        final CheckBoxNodeData data = new CheckBoxNodeData(text, checked);
        final DefaultMutableTreeNode node = new DefaultMutableTreeNode(data);
        parent.add(node);
        return node;
    }

    private static void getSynsets(String root, DefaultMutableTreeNode rootTree){
       Set<String> synsets = JedisSynsetCache.getChildNodes(root);
        for (String synset: synsets) {
            DefaultMutableTreeNode synsetTree = add(rootTree,JedisSynsetCache.getWordFromSynset(synset),false);
            getSynsets(synset,synsetTree);
        }
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        // TODO: place custom component creation code here


        final DefaultMutableTreeNode root = new DefaultMutableTreeNode("Root");
        String startSynset = JedisTree.getStartSynset();
        String startSynsetWord = JedisSynsetCache.getWordFromSynset(startSynset);

        final DefaultMutableTreeNode entity =
                add(root, startSynsetWord, false);

       // add(accessibility, "Move system caret with focus/selection changes", false);
       // add(accessibility, "Always expand alt text for images", true);
        root.add(entity);
        getSynsets(startSynset,entity);

        /*final DefaultMutableTreeNode browsing =
                new DefaultMutableTreeNode("Browsing");
        add(browsing, "Notify when downloads complete", true);
        add(browsing, "Disable script debugging", true);
        add(browsing, "Use AutoComplete", true);
        add(browsing, "Browse in a new process", false);
        root.add(browsing);*/

        final DefaultTreeModel treeModel = new DefaultTreeModel(root);
        JTree tree = new JTree(treeModel);

        final CheckBoxNodeRenderer renderer = new CheckBoxNodeRenderer();
        tree.setCellRenderer(renderer);

        final CheckBoxNodeEditor editor = new CheckBoxNodeEditor(tree);
        tree.setCellEditor(editor);
        tree.setEditable(true);

        // listen for changes in the selection
        tree.addTreeSelectionListener(new TreeSelectionListener() {

            @Override
            public void valueChanged(final TreeSelectionEvent e) {
                System.out.println(System.currentTimeMillis() + ": selection changed");
            }
        });

        // listen for changes in the model (including check box toggles)
        treeModel.addTreeModelListener(new TreeModelListener() {

            @Override
            public void treeNodesChanged(final TreeModelEvent e) {
                System.out.println(System.currentTimeMillis() + ": nodes changed");
            }

            @Override
            public void treeNodesInserted(final TreeModelEvent e) {
                System.out.println(System.currentTimeMillis() + ": nodes inserted");
            }

            @Override
            public void treeNodesRemoved(final TreeModelEvent e) {
                System.out.println(System.currentTimeMillis() + ": nodes removed");
            }

            @Override
            public void treeStructureChanged(final TreeModelEvent e) {
                System.out.println(System.currentTimeMillis() + ": structure changed");
            }
        });
        jscroll = new JScrollPane(tree);
    }
}


