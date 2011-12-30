package org.knuth.multimediaremote.android.client;

import android.util.Log;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Lukas Knuth
 * @version 1.0
 * The client for the MMR-Server. Sends requests to the
 *  server and interprets the response.
 */
public class Client {
    
    /** The IP-Address of the MMR-Server */
    private String ip_address;
    /** The port-number the MMR-Server listens to */
    private int port;

    /**
     * Possible actions that the Server does
     *  understand.
     */
    public enum Actions{
        PAUSE_PLAY, STOP,
        NEXT, PREVIOUS,
        VOL_UP, VOL_DOWN, MUTE;

        /** Not instantiable */
        private Actions(){
            poisoned = false;
        }
        /** Status of the poison-pill */
        private boolean poisoned;

        /**
         * Set the poison-pill to be taken.
         * @see <a href="http://stackoverflow.com/questions/812342">StackOverflow</a>
         */
        void interrupt(){
            poisoned = true;
        }

        /**
         * Indicates if the poison was taken and the queue
         *  should stop.
         * @return if the queue is poisoned.
         */
        boolean isInterrupted(){
            return poisoned;
        }
    }

    /** Server-return status when operation was successfully */
    private static final String SUCCESS = "SUCCESS";
    /** Server-return status when operation failed */
    private static final String FAILURE = "FAILED";

    /** The queue used to send the actions one by one */
    private BlockingQueue<Actions> queue;

    /** The Executor in which the queue is processed */
    private ExecutorService send_queue_service;

    /** Processes the actions in the queue. */
    private Runnable send_queue = new Runnable() {
        public void run() {
            while (!send_queue_service.isShutdown()){
                try {
                    Actions action = queue.take();
                    // Check for poison-pill:
                    if (action.isInterrupted())
                        return;
                    // Send request to server:
                    sendAction(action);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    /**
     * Create a new client for a running MMR-Server
     * @param ip the servers IP-Address
     * @param port the port the Server listens to
     */
    public Client(String ip, int port){
        this.ip_address = ip;
        this.port = port;
        // Set up the queue
        queue = new ArrayBlockingQueue<Actions>(3);
        send_queue_service = Executors.newSingleThreadExecutor();
        send_queue_service.execute(send_queue);
    }

    /**
     * Sends the given action to the MMR-Server with the
     *  specified address and port.
     * @param action the action to send to the server.
     */
    private void sendAction(Actions action){
        Socket server = null;
        BufferedWriter out = null;
        BufferedReader in = null;
        try {
            server = new Socket(ip_address, port);
            out = new BufferedWriter(new OutputStreamWriter(server.getOutputStream()));
            in = new BufferedReader(new InputStreamReader(server.getInputStream()));
            // Write the command:
            out.write(action.name());
            out.newLine();
            out.flush();
            // Get Server-return
            String server_answer = in.readLine();
            // Parse return
            if (SUCCESS.equals(server_answer)){
                Log.d("OnlyLog", "Succesfuly send: "+action);
            } else if (FAILURE.equals(server_answer)) {
                Log.e("OnlyLog", "Operation failed: "+action);
            } else {
                Log.e("OnlyLog", "Server returns: "+server_answer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // Clean up:
            try {
                if (out != null) out.close();
                if (in != null) in.close();
                if (server != null) server.close();
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    public void shutdown(){
        Actions pill = Actions.MUTE;
        pill.interrupt();
        addAction(pill);
        // Shutdown Executor:
        send_queue_service.shutdown();
    }

    /**
     * Send a new action to the connected MMR-Server.
     * @param action the action the Server shell execute.
     */
    public void addAction(Actions action){
        queue.add(action);
    }

}
