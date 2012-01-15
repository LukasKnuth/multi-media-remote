package org.knuth.multimediaremote.server.server;

import org.knuth.multimediaremote.server.model.settings.Config;
import org.knuth.multimediaremote.server.server.http.Worker;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Lukas Knuth
 * @version 1.0
 * A Server which works over HTTP and enables the usage
 *  over a normal browser (if enabled).
 */
final class HttpServer implements Server{

    /** The port this server runs on */
    private int port;

    /** The ExecutorService this server runs in */
    private ExecutorService server_service;
    
    /** The ExecutorService which holds all the workers */
    private ExecutorService workers;

    @Override
    public void init() {
        loadPort();
    }

    /**
     * Load the port from the configuration and store it
     *  in a field.
     */
    private void loadPort(){
        String port_str = Config.INSTANCE.getProperty("port");
        port = Integer.parseInt(port_str)+100;
    }

    @Override
    public void start() {
        loadPort();
        // The Server which accepts connections:
        server_service = Executors.newSingleThreadExecutor();
        server_service.execute(server);
        // The workers which present results:
        workers = Executors.newCachedThreadPool();
    }

    @Override
    public void stop() {
        // Really shut down the Server and cancel the {@code accept()}-
        // method.
        // @see http://stackoverflow.com/questions/2983835
        try {
            if (server_service != null) server_service.shutdown();
            if (serverSocket != null) serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (workers != null) workers.shutdown();
    }

    @Override
    public ServerState getServerState() {
        if (server_service != null && !server_service.isShutdown()
                && workers != null && !workers.isShutdown()){
            return new ServerState(ServerState.States.RUNNING, port);
        }
        return new ServerState(ServerState.States.STOPPED, port);
    }

    /** The ServerSocket used by the server to get incomming connections */
    private ServerSocket serverSocket;

    /** The server-runnable. */
    private Runnable server = new Runnable() {
        @Override
        public void run() {
            try {
                serverSocket = new ServerSocket(port);
                while (!server_service.isShutdown()){
                    try {
                        System.out.println("HTTP Accepting");
                        Socket client = serverSocket.accept();
                        System.out.println("HTTP connected: "+client.getInetAddress());
                        // Accept connection and add to the workers:
                        workers.execute(new Worker(client));
                    } catch (SocketException e){
                        // Can be ignored as it will accrue when the Server is stopped.
                        // @see http://stackoverflow.com/questions/2983835
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };
}
