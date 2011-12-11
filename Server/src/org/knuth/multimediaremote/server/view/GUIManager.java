package org.knuth.multimediaremote.server.view;

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
        JButton test = new JButton("Testbutton");
        overall.add(test);
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
            public void windowClosed(WindowEvent w){
                System.exit(0);
            }
        });
        f.setResizable(false);
        f.setSize(400, 600);
        System.out.println(System.getProperty("os.name"));
    }

    /**
     * Do final work and show the GUI.
     */
    public void present(){
        f.setVisible(true);
    }
    
}
