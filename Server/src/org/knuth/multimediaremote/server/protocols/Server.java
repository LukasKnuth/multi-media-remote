package org.knuth.multimediaremote.server.protocols;

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
     */
    public void stop();

}
