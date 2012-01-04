package org.knuth.multimediaremote.server;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.knuth.multimediaremote.server.model.exceptions.LibraryNotFoundException;
import org.knuth.multimediaremote.server.model.exceptions.UnknownOSException;
import org.knuth.multimediaremote.server.model.remotes.DetermineOS;
import org.knuth.multimediaremote.server.model.settings.Config;
import org.knuth.multimediaremote.server.view.GUIManager;
import org.knuth.multimediaremote.server.view.elements.ErrorDialog;

import javax.swing.*;

/**
 * Server-Application Intro.
 * @author Lukas Knuth
 */
public class Main {

    /**
     * Not instantiable.
     */
    private Main(){}

    /**
     * Initialize the Logger and asynchronously create the GUI.
     * @param agrs Arguments from the Commandline.
     */
    public static void main(String[] agrs){
        // Create and show the GUI:
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                // Initialize the logger:
                PropertyConfigurator.configure(Main.class.getResource("log/log4j-config.properties"));
                Logger logger = Logger.getRootLogger();
                // Check the current OS and load the correct library:
                try {
                    DetermineOS.getNativeRemote();
                } catch (UnknownOSException e) { // TODO Prompt the user to report the problem.
                    // Thrown if the OS couldn't be determined:
                    logger.error("Unknown OS-type", e);
                    new ErrorDialog(e).show();
                    System.exit(1);
                } catch (LibraryNotFoundException e){
                    // Thrown when the native library couldn't be found:
                    logger.error("Couldn't find library", e);
                    new ErrorDialog(e).show();
                    System.exit(1);
                }
                // Read and initialize the config:
                Config.INSTANCE.initialize();
                // Present GUI:
                GUIManager.INSTANCE.present();
            }
        });
    }

}
