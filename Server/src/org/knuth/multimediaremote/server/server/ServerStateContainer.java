package org.knuth.multimediaremote.server.server;

import java.util.Map;

/**
 * @author Lukas Knuth
 * @version 1.0
 * This container contains the states for all registered
 *  servers.
 */
public class ServerStateContainer {

    /** Stores the states of all registered servers */
    private Map<Class, ServerState> states;

    /**
     * Create a new container which holds the states of all
     *  registered servers.
     * @param states a collection of states for all registered
     *  servers.
     */
    ServerStateContainer(Map<Class, ServerState> states){
        this.states = states;
    }

    /**
     * Get the state for a specified server.
     * @param server_class the {@code Class} of the server.
     * @return this servers current state.
     */
    public ServerState getStateFor(Class server_class){
        return states.get(server_class);
    }

    /**
     * Checks if this container contains information
     *  for the given server-name.
     * @param server_class the {@code Class} of the server
     * @return whether this container has information
     *  from the given server or not.
     */
    public boolean containsStateFor(Class server_class){
        return states.containsKey(server_class);
    }
}
