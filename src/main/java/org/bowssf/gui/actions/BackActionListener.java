package org.bowssf.gui.actions;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/**
 * This class is for the action back
 *
 *
 * @author Alejandro Borrajo Viéitez
 */
public class BackActionListener implements ActionListener {
    private final JPanel mainPanel;

    /**
     * The constructor of the class
     * @param mainPanel the main panel of the frame
     */
    public BackActionListener(JPanel mainPanel){
        this.mainPanel = mainPanel;
    }

    /**
     * The performed action
     * @param e the event of the action
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(mainPanel);
        topFrame.dispose();
    }
}
