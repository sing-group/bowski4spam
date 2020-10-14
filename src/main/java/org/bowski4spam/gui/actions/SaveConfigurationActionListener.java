package org.bowssf.gui.actions;

import it.uniroma1.lcl.jlt.util.Language;
import org.bowssf.gui.PreferenceManagerGUI;
import org.bowssf.gui.ConfigurationGUI;
import org.bowssf.util.IniConfig;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * This class is for the save the configuration
 *
 *
 * @author Alejandro Borrajo Viéitez
 */
public class SaveConfigurationActionListener implements ActionListener {
    private final ConfigurationGUI configurationGUI;


    /**
     * The constructor of the class
     * @param configurationGUI the configuration frame
     */
    public SaveConfigurationActionListener(ConfigurationGUI configurationGUI) {
        this.configurationGUI = configurationGUI;
    }

    /**
     * the action performed
     * @param e the selected action
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        JFrame conf = (JFrame) SwingUtilities.getWindowAncestor(this.configurationGUI.getMainpanel());
        JFrame app = (JFrame) SwingUtilities.getWindowAncestor(configurationGUI.getPreferenceManager().getMainPanel());
        IniConfig iniConfig = IniConfig.getInstance();
        iniConfig.setPreloadingLevels(Integer.parseInt(configurationGUI.getLevelsJField().getText()));
        iniConfig.setForceJedis(configurationGUI.getForceJedisRadioButton().isSelected());
        iniConfig.setForceBabelNet(configurationGUI.getForceBabelnetRadioButton().isSelected());
        Language language = Language.EN;
        if(configurationGUI.getLanguageCombobox().getSelectedIndex() == 1){
            language = Language.ES;
        }
        iniConfig.setLanguage(language);
        iniConfig.save();
        conf.dispose();
        app.dispose();
        PreferenceManagerGUI.deploy();


    }
}
