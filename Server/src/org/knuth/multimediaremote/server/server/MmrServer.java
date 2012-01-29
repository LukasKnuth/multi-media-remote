package org.knuth.multimediaremote.server.server;

import org.knuth.multimediaremote.server.controller.Controller;
import org.knuth.multimediaremote.server.model.settings.Config;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Lukas Knuth
 * @version 1.0
 * The Server which speaks the own MMR-protocol.
 * <br>
 * This is not a protocol-standard, just the implementation
 *  which is used by client-software for the Multi-
 *  Media-Remote.
 */
public final class MmrServer implements AbstractServer {

    /**
     * Package-private constructor to only have {@code ServerManager}
     *  have an instance..
     */
    MmrServer(){}
    
    /** The port this Server listens on */
    private int port;

    /** Send to the client to indicate that the action was
     *  successfully performed.
     */
    private static final String SUCCESS = "SUCCESS";

    /** Send to the client to indicate that the action has
     *  failed to perform cleanly.
     */
    private static final String FAILURE = "FAILED";

    /** The socket which this Server uses */
    private ServerSocket serverSocket;

    /** The task which runs the Server in a background-thread */
    private ExecutorService server_task;

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
        port = Integer.parseInt(port_str);
    }

    @Override
    public void start() {
        // Get the port again, maybe it changed:
        loadPort();
        server_task = Executors.newSingleThreadExecutor();
        server_task.execute(server);
    }

    @Override
    public void stop() {
        // Really shut down the Server and cancel the {@code accept()}-
        // method.
        // @see http://stackoverflow.com/questions/2983835
        try {
            if (server_task != null)
                server_task.shutdown();
            if (serverSocket != null)
                serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ServerState getServerState() {
        ServerState.States state;
        if (server_task != null && !server_task.isShutdown()){
            state = ServerState.States.RUNNING;
        } else {
            state = ServerState.States.STOPPED;
        }
        // create the state:
        return new ServerState(state, port);
    }

    /**
     * The Server-Runnable (runs Asynchronously)
     */
    private Runnable server = new Runnable() {
        public void run() {
            try {
                serverSocket = new ServerSocket(port);
                Socket client = null;
                BufferedReader in = null;
                BufferedWriter out = null;
                while (!server_task.isShutdown()){
                    try {
                        client = serverSocket.accept();
                        // Initialize Streams:
                        in = new BufferedReader( new InputStreamReader( client.getInputStream() ));
                        out = new BufferedWriter( new OutputStreamWriter( client.getOutputStream() ));
                        // Read from the Stream:
                        String input = in.readLine();
                        // Parse input:
                        try {
                            Controller.Actions action = Controller.Actions.valueOf(input);
                            Controller.INSTANCE.addAction(action);
                            out.write(SUCCESS);
                            out.newLine();
                            out.flush();
                        } catch (IllegalArgumentException e){
                            // Invalid action:
                            out.write(FAILURE);
                            out.newLine();
                            out.flush();
                        } catch (NullPointerException e){
                            // Nothing was sent (maybe connection problems):
                            out.write(FAILURE);
                            out.newLine();
                            out.flush();
                        }
                    } catch (SocketException e){
                        // Can be ignored as it will accrue when the Server is stopped.
                        // @see http://stackoverflow.com/questions/2983835
                    } finally {
                        // Always close the Streams
                        if (in != null) in.close();
                        if (out != null) out.close();
                        if (client != null) client.close(); // TODO Better way without 'TIME_WAIT' (Linux 'netstat -a')
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                // Close the Server-Socket.
                if (serverSocket != null) try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    };

}
