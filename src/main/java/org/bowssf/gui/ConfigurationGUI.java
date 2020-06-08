package org.bowssf.gui;

import it.uniroma1.lcl.jlt.util.Language;
import org.bowssf.gui.actions.BackActionListener;
import org.bowssf.gui.actions.ClearActionListener;
import org.bowssf.gui.actions.SaveConfigurationActionListener;
import org.bowssf.util.IniConfig;
import javax.swing.*;


/**
 * This class is the interface for the configuration
 *
 *
 * @author Alejandro Borrajo Viéitez
 */
public class ConfigurationGUI extends JFrame{

    private final String ICON_PATH = "src/main/resources/icons/";
    private final static IniConfig iniConfig = IniConfig.getInstance();
    private JPanel mainpanel;
    private JTextField levelsJField;
    private JButton saveButton;
    private JButton backButton;
    private JRadioButton forceBabelnetRadioButton;
    private JRadioButton forceJedisRadioButton;
    private JComboBox languageCombobox;
    private JButton clearSelection;
    private ButtonGroup bgroup;
    private PreferenceManagerGUI preferenceManager;


    /**
     * Gets the other framework
     * @return return the other framework
     */
    public PreferenceManagerGUI getPreferenceManager() {
        return preferenceManager;
    }

    /**
     * Gets the level field
     * @return the field
     */
    public JTextField getLevelsJField() {
        return levelsJField;
    }

    /**
     * Gets a radioButton
     * @return a radio button for babelnet force
     */
    public JRadioButton getForceBabelnetRadioButton() {
        return forceBabelnetRadioButton;
    }

    /**
     * Gets a radioButtom
     * @return a radio button for jedis force
     */
    public JRadioButton getForceJedisRadioButton() {
        return forceJedisRadioButton;
    }

    /**
     * Gets a combobox
     * @return a combobox for language
     */
    public JComboBox getLanguageCombobox() {
        return languageCombobox;
    }

    /**
     * Constructor of the class
     * @param preferenceManager the other frame
     */
    public ConfigurationGUI(PreferenceManagerGUI preferenceManager){
        this.preferenceManager = preferenceManager;
        backButton.addActionListener(new BackActionListener(mainpanel));
        saveButton.addActionListener(new SaveConfigurationActionListener(this));

        levelsJField.setText(Integer.toString(iniConfig.getPreloadingLevels()));
        forceJedisRadioButton.setSelected(iniConfig.isForceJedis());
        forceBabelnetRadioButton.setSelected(iniConfig.isForceBabelNet());
        bgroup = new ButtonGroup();
        bgroup.add(forceJedisRadioButton);
        bgroup.add(forceBabelnetRadioButton);
        int select = 0;
        if(iniConfig.getLanguage() == Language.ES){
            select = 1;
        }
        languageCombobox.setSelectedIndex(select);

        clearSelection.addActionListener(new ClearActionListener(this.bgroup));
    }

    /**
     * Gets a JPanel
     * @return the mainpanel
     */
    public JPanel getMainpanel() {
        return mainpanel;
    }

}
