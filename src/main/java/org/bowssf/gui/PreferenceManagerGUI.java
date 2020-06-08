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
        saveButton.addActionListener(new SaveActionListener(root.getFirstChild()));
        search.addActionListener(new SearchActionListener(searchInput, root.getFirstChild(), tree));
        conf.addActionListener(new ConfigurationActionListener(this));
    }

    /**
     * For deploy the app
     *
     * @throws JedisConnectionException if jedis is not connected
     */
    public static void deploy() throws JedisConnectionException {
        final JFrame frame = new JFrame("app");
        PreferenceManagerGUI b = new PreferenceManagerGUI();
        frame.setContentPane(b.mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setResizable(false);
        frame.setSize(300, 150);
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

}


