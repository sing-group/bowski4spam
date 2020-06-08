package org.bowssf.gui.actions;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * This class is for the clear radiobutton selection
 *
 *
 * @author Alejandro Borrajo Viéitez
 */
public class ClearActionListener implements ActionListener {

    private ButtonGroup buttonGroup;

    /**
     * The constructor of the class
     * @param bg the buttonGroup of the raddio button
     */
    public ClearActionListener(ButtonGroup bg){
        this.buttonGroup = bg;
    }

    /**
     * The action performed
     * @param e the selected action
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        buttonGroup.clearSelection();
    }
}
