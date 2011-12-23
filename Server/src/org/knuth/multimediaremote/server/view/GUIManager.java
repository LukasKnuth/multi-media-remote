package org.knuth.multimediaremote.server.view;

import org.apache.log4j.Logger;
import org.knuth.multimediaremote.server.model.remotes.DetermineOS;
import org.knuth.multimediaremote.server.model.settings.Config;
import org.knuth.multimediaremote.server.view.elements.Log;
import org.knuth.multimediaremote.server.view.elements.OSSwitch;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Manages the Elements on the UI and their interaction.
 * <br>
 * This class is an Enumerated Singleton!
 */
public enum GUIManager {
    /** Singleton Instance */
    INSTANCE;

    /** The JFrame which is shown by the Application */
    private final JFrame f;

    /**
     * Set's up the View.
     */
    private void setUp(){
        // Basic Layout Container:
        JPanel overall = new JPanel();
        overall.setLayout(new BoxLayout(overall, BoxLayout.Y_AXIS));
        f.add(overall);
        // Elements:
        // LOGO

        overall.add(new JSeparator(JSeparator.HORIZONTAL));
        // INSTRUCTIONS

        // OSSWITCHER
        OSSwitch osSwitch = new OSSwitch();
        overall.add(osSwitch.getView());
        overall.add(new JSeparator(JSeparator.HORIZONTAL));
        // SETTINGS
        overall.add( Config.INSTANCE.getView());
        overall.add(new JSeparator(JSeparator.HORIZONTAL));
        // LOG
        Log log = new Log();
        overall.add(log.getView());
        overall.add(new JSeparator(JSeparator.HORIZONTAL));
    }

    /**
     * Private constructor.
     * <br>
     * Also creates the Frame and set's the basic configuration.
      */
    private GUIManager(){
        f = new JFrame("MultiMediaRemote Server Application");
        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        f.addWindowListener(new WindowAdapter() {
            public void windowClosed(WindowEvent w) {
                // Save the config:
                Config.INSTANCE.close();
                // Exit:
                System.exit(0);
            }
        });
        f.setResizable(false);
        f.setSize(400, 600);
        // Set up the All the Controll-Elements:
        setUp();
    }

    /**
     * Do final work and show the GUI.
     */
    public void present(){
        f.setVisible(true);
        Logger logger = Logger.getLogger("guiLogger");
        logger.info("Successfully build the GUI");
        // Check which OS:
        new DetermineOS().getNativeRemote();
    }
    
}
