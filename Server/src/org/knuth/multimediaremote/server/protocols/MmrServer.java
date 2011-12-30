package org.knuth.multimediaremote.server.protocols;

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
final class MmrServer implements Server{
    
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

    public void init() {
        String port_str = Config.INSTANCE.getProperty("port");
        port = Integer.parseInt(port_str);
        server_task = Executors.newSingleThreadExecutor();
    }

    public void start() {
        System.out.println("Starting MMR-Server");
        server_task.execute(server);
    }

    public void stop() {
        System.out.println("Stopping MMR-Server");
        server_task.shutdown();
        // Really shut down the Server and cancel the {@code accept()}-
        // method.
        // @see http://stackoverflow.com/questions/2983835
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
                        System.out.println("Got: "+input);
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
                        }
                    } catch (SocketException e){
                        // Can be ignored as it will accrue when the Server is stopped.
                        // @see http://stackoverflow.com/questions/2983835
                        System.out.println("Thrown...");
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
