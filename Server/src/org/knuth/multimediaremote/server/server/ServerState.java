package org.knuth.multimediaremote.server.server;

/**
 * @author Lukas Knuth
 * @version 1.0
 * A collection of information about the current AbstractServer-
 *  state.
 */
public final class ServerState {

    /**
     * Possible states for the AbstractServer. This is not specific
     *  for any single implementation, but for all registered
     *  servers.
     */
    public enum States{
        RUNNING,
        STOPPED
    }

    /** The current state the AbstractServer(s) is in. */
    private States current_state;

    /** The port the server listens to */
    private int port;

    /**
     * Create a new object which holds information about the
     *  current state and data of a AbstractServer-implementation.
     * @param current_state the servers
     */
    ServerState(States current_state, int port){
        this.current_state = current_state;
        this.port = port;
    }

    /**
     * Get the current state the server-implementation is in.
     * @return the servers current state.
     */
    public States getCurrentState() {
        return current_state;
    }

    /**
     * Get the port the server is listening to.
     * @return the port.
     */
    public int getPort() {
        return port;
    }
}
