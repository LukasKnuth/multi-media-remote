package org.knuth.multimediaremote.server.view.elements;

import org.knuth.multimediaremote.server.model.l18n.L18N;
import org.knuth.multimediaremote.server.model.l18n.T;
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

    /** The label holding the information */
    private JLabel instructions;

    /**
     * Creates a new Label with instructions on how to use the Server
     *  and how to connect to it.</p>
     * These instructions update themselves according to the current
     *  Server-state.
     */
    public Instructions(){
        instructions = new JLabel(String.format(L18N.tS(T.View.Instructions.instructions),
                L18N.tS(T.View.Instructions.not_running),
                L18N.tS(T.View.Instructions.not_running),
                L18N.tS(T.View.Instructions.not_running)
        ));
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
                    mmr_port = L18N.tS(T.View.Instructions.not_running);
                    ip = L18N.tS(T.View.Instructions.not_running);
            }
        } else {
            mmr_port = L18N.tS(T.View.Instructions.deactivated);
            ip = L18N.tS(T.View.Instructions.deactivated);
        }
        // HTTP:
        if (container.containsStateFor(HttpServer.class)){
            ServerState state = container.getStateFor(HttpServer.class);
            switch (state.getCurrentState()){
                case RUNNING:
                    http = "http://"+ip+":"+state.getPort()+"";
                    break;
                case STOPPED:
                    http = L18N.tS(T.View.Instructions.not_running);
            }
        } else {
            http = L18N.tS(T.View.Instructions.deactivated);
        }
        // Format the String:
        instructions.setText(String.format(L18N.tS(T.View.Instructions.instructions),
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
