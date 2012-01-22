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
 * Server-Application Intro.
 * @author Lukas Knuth
 */
public class Main {

    // TODO Use "chkUpdate" for update check on public github repo
    // TODO Implement the HTTP-Server (or use Jetty embedded?)
    // TODO Add support for multiple languages
    // TODO Add more Logging-statements (for errors and events).
    // TODO Change exit-code if there was a problem (don't exit with 0).

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
                GUIManager.INSTANCE.present();
            }
        });
    }

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
