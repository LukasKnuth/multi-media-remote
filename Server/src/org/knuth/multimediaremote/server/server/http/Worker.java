package org.knuth.multimediaremote.server.server.http;

import java.io.*;
import java.net.Socket;

/** The workers-class, which handle a single client */
public class Worker implements Runnable{

    /** The socket from the connected client */
    private final Socket client_socket;

    /**
     * Create a new Worker which handel's one single client
     * @param client_socket the {@code Socket} of that client.
     */
    public Worker(Socket client_socket){
        this.client_socket = client_socket;
    }

    @Override
    public void run() {
        BufferedWriter out = null;
        BufferedReader in = null;
        try {
            // Get the Streams:
            out = new BufferedWriter(new OutputStreamWriter(client_socket.getOutputStream(), "UTF-8"));
            in = new BufferedReader(new InputStreamReader(client_socket.getInputStream(), "UTF-8"));
            // Read the request:
            String req = readRequest(in);
            // TODO Parse request and check if GET or POST
            // TODO Write HttpResourceManager to get the contents of a file from the "http"-package
            // TODO Implement caching for GET-resources with HashMap?
            // Write the response-header:
            String file = req.substring(req.indexOf("GET ")+4, req.indexOf(" HTTP"));
            String content = HttpResourceManager.getResourceContents(file);
            out.write(content);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // Clean up:
            try {
                if (out != null) out.close();
                if (in != null) in.close();
                if (client_socket != null) client_socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Reads the HTTP-request from the sockets InputStream
     * @param r a {@code BufferedReader} to read from the sockets
     *  {@code InputStream}.
     * @return the request as a String.
     * @throws IOException if there was a problem reading from the
     *  Stream.
     */
    private String readRequest(BufferedReader r) throws IOException {
        StringBuilder builder = new StringBuilder();
        String line;
        while ( !"".equals(line = r.readLine()) ) {
            builder.append(line);
        }
        System.out.println("Request: "+builder.toString());
        try {
            return builder.toString();
        } finally {
            // Free for Garbage-collection:
            builder = null;
        }
    }
}