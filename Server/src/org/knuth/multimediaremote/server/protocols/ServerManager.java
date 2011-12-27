package org.knuth.multimediaremote.server.protocols;

/**
 * @author Lukas Knuth
 * @version 1.0
 * Manages and starts/stops all server-services.
 */
public enum ServerManager {

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
