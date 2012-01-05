package org.knuth.multimediaremote.server.protocols;

/**
 * @author Lukas Knuth
 * @version 1.0
 * Manages and starts/stops all server-services.
 */
public enum ServerManager {

    // TODO ServerStateChanged observer that notifies the launch-button and instructions when state changes
    // TODO ServerState-object (not instantiable) with information (current state, IP, Port, etz).
    // TODO Let all Servers register them self's at the ServerManager and collect them in a HashMap.
    // TODO Collect those servers in the ServerState-object and provide a state for every server there.

    /** Instance to work with */
    INSTANCE;

    /** All available Servers are registered here */
    private Server[] servers;

    /**
     * Singleton - Not instantiable.
     */
    private ServerManager(){
        servers = new Server[2];
        servers[0] = new MmrServer();
        servers[0].init();
        servers[1] = new HttpServer();
        servers[1].init();
    }

    /**
     * Starts all Servers and listens for incoming
     *  connections.
     */
    public void startServers(){
        for (Server server : servers){
            server.start();
        }
    }

    /**
     * Stops all started servers.
     */
    public void stopServers(){
        for (Server server : servers){
            server.stop();
        }
    }


}
