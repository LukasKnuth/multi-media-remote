package org.knuth.multimediaremote.server;

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
     * Asynchronously create the GUI.
     * @param agrs Arguments from the Commandline.
     */
    public static void main(String[] agrs){
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                GUIManager.INSTANCE.present();
            }
        });
    }

}
