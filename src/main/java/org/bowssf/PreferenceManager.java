package org.bowssf;


import org.bowssf.gui.PreferenceManagerGUI;
import redis.clients.jedis.exceptions.JedisConnectionException;

import javax.swing.*;
import java.io.*;


public class PreferenceManager {
    public static void main(String[] args)  {

        try {
            PreferenceManagerGUI.deploy();
        }catch (JedisConnectionException j){
            JOptionPane.showMessageDialog(null,"Redis está desconectado");
        }
    }
}
