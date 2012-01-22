package org.knuth.multimediaremote.server.server;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.knuth.multimediaremote.server.model.settings.Config;
import org.knuth.multimediaremote.server.server.http.HttpMainServlet;

/**
 * @author Lukas Knuth
 * @version 1.0
 * A AbstractServer which works over HTTP and enables the usage
 *  over a normal browser (if enabled).
 */
final class HttpServer implements AbstractServer{

    /** The port this server-implementation listens on */
    private int port;
    
    private Server server;

    @Override
    public void init() {
        // Load the port:
        loadPort();
        // Configure the Server:
        server = new Server(port);
        // TODO Remove jettys logging-statements?
        // Servlet Handler:
        ServletContextHandler servlet_handler = new ServletContextHandler();
        servlet_handler.setContextPath("/");
        servlet_handler.addServlet(new ServletHolder(new HttpMainServlet()), "/");
        // Resource Handler:
        ResourceHandler res_handler = new ResourceHandler();
        res_handler.setDirectoriesListed(true);
        String base = this.getClass().getResource("http/res").getPath();
        res_handler.setResourceBase(base);
        System.out.println(base);
        // Put resource handler in a context and map it to the "/res"-directory:
        ContextHandler res_handler_context = new ContextHandler();
        res_handler_context.setHandler(res_handler);
        res_handler_context.setContextPath("/res");
        // TODO Add some redirect or info to the /res/index.html file or move it completely in the servlet.
        // Add contexts:
        HandlerCollection handlers = new HandlerList();
        handlers.addHandler(res_handler_context);
        handlers.addHandler(servlet_handler);
        server.setHandler(handlers);
    }

    /**
     * Load the port from the configuration and store it
     *  in a field.
     */
    private void loadPort(){
        String port_str = Config.INSTANCE.getProperty("port");
        port = Integer.parseInt(port_str) +100;
    }

    @Override
    public void start() {
        try {
            // TODO Add port check and launch server on defined port.
            server.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop() {
        try {
            server.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public ServerState getServerState() {
        if (server != null && server.isStarted() && server.isRunning()){
            return new ServerState(ServerState.States.RUNNING, port);
        } else {
            return new ServerState(ServerState.States.STOPPED, port);
        }
    }
}
