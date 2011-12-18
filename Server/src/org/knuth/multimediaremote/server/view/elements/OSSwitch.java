package org.knuth.multimediaremote.server.view.elements;

import javax.swing.*;

/**
 * @author Lukas Knuth
 * @version 1.0
 * An element for the Swing window toolkit to show, which OS
 *  is currently running.
 */
public class OSSwitch {

    /** Holder for the Logo-Elements */
    private final JPanel all;

    /**
     * Create a new OSSwitch-Window element to show the current
     *  operating system.
     */
    public OSSwitch(){
        all = new JPanel();
        all.setLayout(new BoxLayout(all, BoxLayout.X_AXIS));
        // Add the Shown Logos:

    }

    /**
     * Adds the logos of the different OS-Types to the
     *  Panel.
     */
    private void addLogos(){

    }

    /**
     * Get a View on this OSSwitcher to add it to a Swing-Window/Panel
     * @return the view of this OSSwitcher element.
     */
    public JPanel getView(){
        return all;
    }
}
