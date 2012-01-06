package org.knuth.multimediaremote.server.server;

/**
 * @author Lukas Knuth
 * @version 1.0
 * Describes a server-service which can be started/stopped and
 *  redirects incoming requests to the {@code Controller}.
 */
interface Server {

    /**
     * Initialize this Server.
     */
    public void init();

    /**
     * Start this Server.
     */
    public void start();

    /**
     * Stop this Server.
     * <br>
     * <b>This method is guarantied to be called</b>. So it might be
     *  the case, that the Server has not yet started when this
     *  method is called.
     *  <br>
     * It is the implementations task to not throw any uncaught
     *  exceptions and check for itself if and how the Server can
     *  be shut down!
     */
    public void stop();

    /**
     * Returns a {@code ServerState}-object which holds information
     *  about the current state and data of this Server-implementation.
     * @return the {@code ServerState}-object with the current state.
     */
    ServerState getServerState();

}
