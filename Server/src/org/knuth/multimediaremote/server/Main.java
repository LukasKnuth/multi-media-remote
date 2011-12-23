package org.knuth.multimediaremote.server;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.knuth.multimediaremote.server.model.settings.Config;
import org.knuth.multimediaremote.server.view.GUIManager;

import javax.swing.*;

/**
 * Server-Application Intro.
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
                Logger logger = Logger.getLogger("guiLogger");
                logger.info("Hello World!");
                // Read and initialize the config:
                new SwingWorker<Void, Void>(){
                    @Override
                    protected Void doInBackground() throws Exception {
                        Config.INSTANCE.initialize();
                        return null;
                    }
                }.execute();
                // Present GUI:
                GUIManager.INSTANCE.present();
            }
        });
    }

}
