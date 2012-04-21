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
import java.io.File;
import java.io.IOException;
import java.util.Properties;

/**
 * @author Lukas Knuth
 * @version 1.0
 * Server-Application Intro.
 */
public class Main {

    // TODO Use "chkUpdate" for update check on public github repo
    // TODO Change exit-code if there was a problem (don't exit with 0).
    /*
    * When exporting the Server, move or delete the symbolic links and files (e.g.
    *  "config" and "logging") from the "out/production"-folder so they aren't
     *  included in the produced .jar-file!
     */
    // TODO Create Ant build-script to collect the releases in a different folder.
    // TODO Make own File-Appender for log which creates a log per run.
    // TODO Fix JavaDoc for classes by putting them above author and version!

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
                configureLogger();
                Logger logger = Logger.getRootLogger();
                // Check the current OS and load the correct library:
                try {
                    DetermineOS.getNativeRemote();
                } catch (UnknownOSException e) {
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
                GUIManager guiManager = new GUIManager();
                guiManager.present();
            }
        });
    }

    /**
     * Loads the config-file for the Log4J library and sets the output-
     *  directory to the path containing the ".jar"-file.
     * This has to be done "in-code" since there are no relative paths
     *  finding out the correct path is tricky.
     * @see org.knuth.multimediaremote.server.model.settings.Config#getBaseDir()
     */
    private static void configureLogger(){
        try {
            Properties properties = new Properties();
            // Load standard config:
            properties.load(Main.class.getResourceAsStream("log/log4j-config.properties"));
            // Change dynamic path for Log-file:
            properties.setProperty("log4j.appender.file.File",
                    Config.getBaseDir().getAbsolutePath()+ File.separator +"logging.log");
            // Initialize the config for the logger.
            PropertyConfigurator.configure(properties);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
