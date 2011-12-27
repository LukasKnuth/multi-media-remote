package org.knuth.multimediaremote.server.view;

import org.apache.log4j.Logger;
import org.knuth.multimediaremote.server.model.settings.Config;
import org.knuth.multimediaremote.server.view.elements.Log;

import javax.swing.*;
import java.awt.*;
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
        overall.setLayout(new BoxLayout(overall, BoxLayout.PAGE_AXIS));
        f.add(overall);
        // Elements:
        // LOGO

        overall.add(new JSeparator(JSeparator.HORIZONTAL));
        // INSTRUCTIONS
        String instructions_str = "<html><p>Specify the Port on which the Server should run (if you don't know " +
                "what that is, you should probably leave it like it is) and hit the \"Start " +
                "Server\"-button.</p>" +
                "<p>The Server listens on [your-ip]:[port] for you. You can enter that in your " +
                "MultiMediaRemote-client software.</p>" +
                "<p>If you choose to also use the WebEnd, you can access it via your Browser with the " +
                "above IP and the Port + 1000</p>" +
                "<p>Have fun!</p></html>";
        JLabel instructions = new JLabel(instructions_str);
        instructions.setAlignmentX(Component.CENTER_ALIGNMENT);
        overall.add(instructions);
        // OSSWITCHER

        overall.add(new JSeparator(JSeparator.HORIZONTAL));
        // SETTINGS
        JPanel settings = Config.INSTANCE.getView();
        overall.add(settings);
        overall.add(new JSeparator(JSeparator.HORIZONTAL));
        // LOG
        overall.add(new Log().getView());
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
    }
    
}
