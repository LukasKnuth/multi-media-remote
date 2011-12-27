package org.knuth.multimediaremote.server.protocols;

import org.knuth.multimediaremote.server.controller.Controller;
import org.knuth.multimediaremote.server.model.settings.Config;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

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

    /** Indicates if the server is currently running
     *  e.g. listening for new actions.
     */
    private boolean running; // TODO Very ugly. Does not stop when already waiting for input!
    
    public void init() {
        String port_str = Config.INSTANCE.getProperty("port");
        port = Integer.parseInt(port_str);
        running = false;
    }

    public void start() {
        System.out.println("Starting MMR-Server");
        new Thread(server).start();
    }

    public void stop() {
        System.out.println("Stopping MMR-Server");
        running = false;
    }

    /**
     * The Server-Runnable (runs Asynchronously)
     */
    private Runnable server = new Runnable() {
        public void run() {
            running = true;
            ServerSocket serverSocket = null;
            try {
                serverSocket = new ServerSocket(port);
                Socket client = null;
                BufferedReader in = null;
                BufferedWriter out = null;
                while (running){
                    try {
                        client = serverSocket.accept();
                        // Initialize Streams:
                        in = new BufferedReader( new InputStreamReader( client.getInputStream() ));
                        out = new BufferedWriter( new OutputStreamWriter( client.getOutputStream() ));
                        // Read from the Stream:
                        StringBuilder builder = new StringBuilder();
                        while (in.ready()){
                            builder.append(in.readLine());
                        }
                        // Parse input:
                        try {
                            Controller.Actions action = Controller.Actions.valueOf(builder.toString());
                            Controller.INSTANCE.addAction(action);
                            out.write(SUCCESS);
                            out.flush();
                        } catch (IllegalArgumentException e){
                            // Invalid action:
                            out.write(FAILURE);
                            out.flush();
                        }
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
