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
    private Map<String, ServerState> states;

    /**
     * Create a new container which holds the states of all
     *  registered servers.
     * @param states a collection of states for all registered
     *  servers.
     */
    ServerStateContainer(Map<String, ServerState> states){
        this.states = states;
    }

    /**
     * Get the state for a specified server.
     * @param key_name the name of the server.
     * @return this servers current state.
     */
    public ServerState getStateFor(String key_name){
        return states.get(key_name);
    }

    /**
     * Checks if this container contains information
     *  for the given server-name.
     * @param key_name the name of the server
     * @return whether this container has information
     *  from the given server or not.
     */
    public boolean containsStateFor(String key_name){
        return states.containsKey(key_name);
    }
}
