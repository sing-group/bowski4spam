package org.bowssf.gui.actions;

import org.bowssf.gui.PreferenceManagerGUI;
import org.bowssf.gui.ConfigurationGUI;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/**
 * This class is for the open the frame configuration
 *
 *
 * @author Alejandro Borrajo Viéitez
 */
public class ConfigurationActionListener implements ActionListener {
    private PreferenceManagerGUI preferenceManager;

    /**
     * The constructor of the class
     * @param preferenceManager the manager frame
     */
    public ConfigurationActionListener(PreferenceManagerGUI preferenceManager){
        this.preferenceManager = preferenceManager;
    }

    /**
     * The action performed
     * @param e the selected action
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        final JFrame frame = new JFrame("Configuration");
        ConfigurationGUI configurationGUI = new ConfigurationGUI(this.preferenceManager);
        frame.setContentPane(configurationGUI.getMainpanel());
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 150);
        frame.setResizable(false);
        frame.setVisible(true);
    }
}
