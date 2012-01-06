package org.knuth.multimediaremote.server.server.observer;

import org.knuth.multimediaremote.server.server.ServerStateContainer;

/**
 * @author Lukas Knuth
 * @version 1.0
 * Implemented to declare that the implementing class listens
 *  for changes on the Server-status (started/stopped).
 */
public interface ServerStateChangeListener {

    /**
     * This method will be called every time the servers state
     *  changes. It will provide all information about the
     *  server and it's state.
     * @param container the container, containing information
     *  about the servers state.
     */
    public void serverStateChanged(ServerStateContainer container);
}
