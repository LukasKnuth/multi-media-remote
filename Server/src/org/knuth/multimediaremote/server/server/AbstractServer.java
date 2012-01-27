package org.knuth.multimediaremote.server.server;

/**
 * @author Lukas Knuth
 * @version 1.0
 * Describes a server-service which can be started/stopped and
 *  redirects incoming requests to the {@code Controller}.
 */
interface AbstractServer {
    
    /**
     * Initialize this AbstractServer.</p>
     * This is where all the basic work which stays the same between
     *  launches should take place.</p>
     * This method will only be called once, but is guaranteed to be
     *  called before this server first starts!
     */
    void init();

    /**
     * Start this AbstractServer.</p>
     * In this method, you should only start the server (which has
     *  previously been initialized in the {@code init()}-method)
     *  and fetch the latest port, because it might change between
     *  launches.</p>
     * This method should execute fast and should not block!
     */
    void start();

    /**
     * Stop this AbstractServer.</p>
     * <b>This method is guarantied to be called</b>. So it might be
     *  the case, that the AbstractServer has not yet started when this
     *  method is called.</p>
     * It is the implementations task to not throw any uncaught
     *  exceptions and check for itself if and how the AbstractServer can
     *  be shut down!
     */
    void stop();

    /**
     * Returns a {@code ServerState}-object which holds information
     *  about the current state and data of this AbstractServer-implementation.
     * @return the {@code ServerState}-object with the current state.
     */
    ServerState getServerState();

}
