package org.knuth.multimediaremote.server.view.elements;

import org.knuth.multimediaremote.server.server.*;
import org.knuth.multimediaremote.server.server.observer.ServerStateChangeListener;

import javax.swing.*;

/**
 * @author Lukas Knuth
 * @version 1.0
 * Instructions on how to use and connect to the
 *  Server.
 */
public class Instructions implements ServerStateChangeListener{

    /** The standard instruction-string */
    private static final String instructions_str = "<html>" +
            "<p><b>Connection-Data <i>for clients</i></b>" +
            "<ul><li>Server-IP: %1$s</li>" +
            "<li>Port: %2$s</li>" +
            "</ul>" +
            "<b>Connection-Data <i>for browsers</i></b>" +
            "<ul><li>Address: %3$s</li>" +
            "</ul>"+
            "<p>Have fun!</p></html>";

    /** The label holding the information */
    private JLabel instructions;

    /**
     * Creates a new Label with instructions on how to use the Server
     *  and how to connect to it.</p>
     * These instructions update themselves according to the current
     *  Server-state.
     */
    public Instructions(){
        instructions = new JLabel(String.format(instructions_str,
                "<i>not running</i>", "<i>not running</i>", "<i>not running</i>"));
        ServerManager.INSTANCE.addServerStateChangeListener(this);
    }

    @Override
    public void serverStateChanged(ServerStateContainer container) {
        String ip = ServerManager.INSTANCE.getCurrentIP();
        String mmr_port = "";
        String http = "";
        // Get the information:
        if (container.containsStateFor(MmrServer.class)){
            ServerState state = container.getStateFor(MmrServer.class);
            switch (state.getCurrentState()){
                case RUNNING:
                    mmr_port = state.getPort()+"";
                    break;
                case STOPPED:
                    mmr_port = "<i>not running</i>";
                    ip = "<i>not running</i>";
            }
        } else {
            mmr_port = "<b>deactivated</b>";
            ip = "<b>deactivated</b>";
        }
        // HTTP:
        if (container.containsStateFor(HttpServer.class)){
            ServerState state = container.getStateFor(HttpServer.class);
            switch (state.getCurrentState()){
                case RUNNING:
                    http = "http://"+ip+":"+state.getPort()+"";
                    break;
                case STOPPED:
                    http = "<i>not running</i>";
            }
        } else {
            http = "<b>deactivated</b>";
        }
        // Format the String:
        instructions.setText(String.format(instructions_str,
                ip, mmr_port, http)
        );
    }

    /**
     * Returns the {@code JLabel} used to represent the instruction-
     *  string on the GUI.
     * @return the {@code JLabel} with the instruction.
     */
    public JLabel getLabel(){
        return instructions;
    }
}
