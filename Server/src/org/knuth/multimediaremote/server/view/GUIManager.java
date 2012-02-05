package org.knuth.multimediaremote.server.view;

import org.apache.log4j.Logger;
import org.knuth.multimediaremote.server.controller.Controller;
import org.knuth.multimediaremote.server.model.remotes.OSSwitch;
import org.knuth.multimediaremote.server.model.settings.Config;
import org.knuth.multimediaremote.server.server.ServerManager;
import org.knuth.multimediaremote.server.view.elements.Instructions;
import org.knuth.multimediaremote.server.view.elements.Log;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * @author Lukas Knuth
 * @version 1.0
 * Manages the Elements on the UI and their interaction.
 */
public class GUIManager {

    /** The JFrame which is shown by the Application */
    private final JFrame f;

    /**
     * Set's up the View.
     */
    private void setUp(){
        // Basic Layout Container:
        JPanel overall = new JPanel();
        overall.setLayout(new BoxLayout(overall, BoxLayout.PAGE_AXIS));
        f.add(overall);
        // Elements:
        // LOGO
        ImageIcon logo = new ImageIcon(GUIManager.class.getResource("res/logo_platzhalter.png"));
        JLabel logo_label = new JLabel(logo);
        logo_label.setAlignmentX(Component.CENTER_ALIGNMENT);
        overall.add(logo_label);
        overall.add(new JSeparator(JSeparator.HORIZONTAL));
        // INSTRUCTIONS
        JLabel instructions = new Instructions().getLabel();
        instructions.setAlignmentX(Component.CENTER_ALIGNMENT);
        overall.add(instructions);
        // OS-SWITCHER
        overall.add(new OSSwitch().getView());
        overall.add(new JSeparator(JSeparator.HORIZONTAL));
        // SETTINGS
        JPanel settings = Config.INSTANCE.getView();
        overall.add(settings);
        overall.add(new JSeparator(JSeparator.HORIZONTAL));
        // LOG
        overall.add(new Log().getView());
    }

    /**
     * Does the basic set-up for the {@code JFrame} and also
     *  calls the {@code setUp}-method to do further work.
     * @see org.knuth.multimediaremote.server.view.GUIManager#setUp()
      */
    public GUIManager(){
        f = new JFrame("MultiMediaRemote Server Application");
        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        f.addWindowListener(new WindowAdapter() {
            public void windowClosed(WindowEvent w) {
                // Save the config:
                Config.INSTANCE.close();
                // Shutdown all Servers:
                ServerManager.INSTANCE.stopServers();
                // Shutdown the Controller:
                Controller.INSTANCE.shutdown();
                // Exit:
                System.exit(0);
            }
        });
        f.setResizable(false);
        // Set up the All the Control-Elements:
        setUp();
        f.pack();
    }

    /**
     * Do final work and show the GUI.
     */
    public void present(){
        f.setVisible(true);
        Logger logger = Logger.getLogger("guiLogger");
        logger.info("Successfully build the GUI");
    }
    
}
