package org.bowssf.gui;


import org.bowssf.gui.actions.ConfigurationActionListener;
import org.bowssf.gui.actions.SaveActionListener;
import org.bowssf.gui.actions.SearchActionListener;
import org.bowssf.gui.actions.TreeListener;
import org.bowssf.util.*;
import org.scijava.swing.checkboxtree.CheckBoxNodeEditor;
import org.scijava.swing.checkboxtree.CheckBoxNodeRenderer;
import redis.clients.jedis.exceptions.JedisConnectionException;
import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This class is the interface for the treeFunctions
 *
 * @author Alejandro Borrajo Viéitez
 */
public class PreferenceManagerGUI extends JFrame {
    private final String file = "interest.json";
    private JButton search;
    private JTextField searchInput;
    private JPanel searchPanel;
    private JPanel mainPanel;
    private JScrollPane jscroll;
    private JButton conf;
    private JButton saveButton;
    private static JedisSynsetCache jedisSynsetCache = JedisSynsetCache.getInstance();
    private static IniConfig iniConfig = IniConfig.getInstance();
    private static DefaultTreeModel treeModel;
    public static DefaultMutableTreeNode root;
    private static JTree tree;
    private Map<String, List<String>> interest;

    /**
     * Gets the JPanel
     *
     * @return the main JPanel
     */
    public JPanel getMainPanel() {
        return mainPanel;
    }

    /**
     * Constructor fot the class
     */
    public PreferenceManagerGUI() {
    }

    /**
     * For deploy the app
     *
     * @throws JedisConnectionException if jedis is not connected
     */
    public static void deploy() throws JedisConnectionException {

        final JFrame frame = new JFrame("app");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setResizable(false);
        frame.setSize(300, 150);
        PreferenceManagerGUI preferenceManagerGUI = new PreferenceManagerGUI();
        frame.setContentPane(preferenceManagerGUI.mainPanel);
        frame.setVisible(true);
    }


    /**
     * Constructs the tree in the Jframe
     *
     * @param root      the parent of the node
     * @param rootTree  the entire tree
     * @param levels    the level desired to construct
     * @param childTrue the boolean if the parent is checked
     */
    private void getSynsets(String root, DefaultMutableTreeNode rootTree, int levels, boolean childTrue) {
        if (levels != 0) {

            boolean check = false;
            if (!interest.isEmpty()) {
                if (interest.get("interested").contains(root)) {
                    childTrue = true;
                }
            }
            DefaultMutableTreeNode synsetTree = TreeUtils.add(rootTree, "<html><body><b>" + jedisSynsetCache.getWordFromSynset(root) + "</b> | " + "<small>" + jedisSynsetCache.getGlossFromSynset(root) + "</small></body></html>", childTrue);
            Set<String> synsets = jedisSynsetCache.getChildNodes(root);

            for (String synset : synsets) {
                getSynsets(synset, synsetTree, levels - 1, childTrue);
            }
        }
    }

    /**
     * Pregenerate the node for optimization
     */
    private void preload() {
        iniConfig.load();
        //jedisSynsetCache.preloadingLevels(iniConfig.getPreloadingLevels());
        BabelUtils.setLang(iniConfig.getLanguage());
        jedisSynsetCache.setLanguage(iniConfig.getLanguage());
        jedisSynsetCache.setForceBabelnet(iniConfig.isForceBabelNet());
        jedisSynsetCache.setForceJedis(iniConfig.isForceJedis());
        interest = JsonUtils.read(file);


    }

    /**
     * For intellij purpose
     */
    private void createUIComponents() {

        mainPanel = new JPanel();
        preload();

        root = new DefaultMutableTreeNode("Root");
        String startSynset = jedisSynsetCache.START_SYNSET;
        this.getSynsets(startSynset, root, iniConfig.getPreloadingLevels(), false);

        treeModel = new DefaultTreeModel(root);
        tree = new JTree(treeModel);

        final CheckBoxNodeRenderer renderer = new CheckBoxNodeRenderer();
        tree.setCellRenderer(renderer);

        final CheckBoxNodeEditor editor = new CheckBoxNodeEditor(tree);
        tree.setCellEditor(editor);
        tree.setEditable(true);
        saveButton = new JButton();
        saveButton.setBackground(new Color(-15000805));
        saveButton.setHideActionText(true);
        saveButton.setIcon(new ImageIcon(getClass().getResource("/icons/save.png")));
        saveButton.setText("");
        saveButton.addActionListener(new SaveActionListener(root));

        searchInput = new JTextField();

        search = new JButton();
        search.setBackground(new Color(-15000805));
        search.setHideActionText(true);
        search.setIcon(new ImageIcon(getClass().getResource("/icons/research.png")));
        search.setText("");
        search.addActionListener(new SearchActionListener(searchInput, root.getFirstChild(), tree));

        conf = new JButton();
        conf.setBackground(new Color(-15000805));
        conf.setHideActionText(true);
        conf.setIcon(new ImageIcon(getClass().getResource("/icons/settings.png")));
        conf.setText("");
        conf.addActionListener(new ConfigurationActionListener(this));

        // listen for changes in the selection
        tree.addTreeSelectionListener(new TreeSelectionListener() {

            @Override
            public void valueChanged(final TreeSelectionEvent e) {
                System.out.println(System.currentTimeMillis() + ": selection changed");
            }
        });

        // listen for changes in the model (including check box toggles)
        treeModel.addTreeModelListener(new TreeListener());
        jscroll = new JScrollPane(tree);


    }


    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        createUIComponents();
        mainPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(3, 1, new Insets(0, 0, 0, 0), -1, -1));
        searchPanel = new JPanel();
        searchPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        mainPanel.add(searchPanel, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_NORTH, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        search.setBackground(new Color(-15000805));
        search.setEnabled(true);
        search.setForeground(new Color(-15000805));
        search.setHideActionText(false);
        search.setIcon(new ImageIcon(getClass().getResource("/icons/research.png")));
        search.setText("");
        searchPanel.add(search, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        searchPanel.add(searchInput, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        conf.setHideActionText(true);
        conf.setIcon(new ImageIcon(getClass().getResource("/icons/settings.png")));
        conf.setText("");
        searchPanel.add(conf, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        mainPanel.add(jscroll, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        saveButton.setBackground(new Color(-15000805));
        saveButton.setHideActionText(true);
        saveButton.setIcon(new ImageIcon(getClass().getResource("/icons/save.png")));
        saveButton.setText("");
        mainPanel.add(saveButton, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     *
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }
}


