package org.bowssf.gui.actions;

import org.bowssf.gui.BowssfGUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.jar.JarEntry;

public class SearchAction implements ActionListener {
    private String text;
    public SearchAction(String text) {
        this.text = text;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JOptionPane.showMessageDialog(null,text);
    }
}
