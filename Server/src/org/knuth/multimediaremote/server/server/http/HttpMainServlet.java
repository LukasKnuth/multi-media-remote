package org.knuth.multimediaremote.server.server.http;

import org.knuth.multimediaremote.server.controller.Controller;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * @author Lukas Knuth
 * @version 1.0
 * This is the main servlet which offers the controls and handles
 *  the incoming POST-requests.
 */
public class HttpMainServlet extends HttpServlet{

    /** The cached index-file */
    private String indexCache;

    /**
     * Gets called when the servlet is requested and returns the controls
     *  to send requests to the Server.
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("text/html");
        try (PrintWriter out = new PrintWriter(resp.getWriter())) {
            out.write(getIndexContents());
            out.flush();
        } catch (IOException e){
            // Problem reading from the file:
            resp.sendError(404);
        }
    }

    /**
     * Reads the "index.html"-file from the resource folder and caches it's
     *  contents.
     * @return the contents (from the file or the cache) of the index file.
     * @throws IOException if the index-file could not be found or read.
     */
    private String getIndexContents() throws IOException {
        // Check if already cached:
        if (this.indexCache != null) return this.indexCache;
        // Read contents from "index.html"-resource file:
        InputStream is = this.getClass().getResourceAsStream("res/index.html");
        if (is == null){
            // If the "index.html"-file could not be found:
            throw new FileNotFoundException("Couldn't find \"res/index.html\"-file");
        }
        // Read the file and cache the contents:
        StringBuffer buffer = new StringBuffer();
        try(BufferedReader in = new BufferedReader(new InputStreamReader(is))) {
            // Copy contents:
            String line;
            while ( (line = in.readLine()) != null)
                buffer.append(line);
        }
        // Write to cache:
        this.indexCache = buffer.toString();
        return indexCache;
    }

    /**
     * Gets called when a AJAX request was sent to the Servlet to execute
     *  a given action on the MMR-Server.
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException{
        System.out.println("POST for action: "+req.getParameter("action"));
        resp.setContentType("application/json");
        try (PrintWriter out = new PrintWriter(resp.getWriter())){
            if (parseAndExecute( req.getParameter("action") )){
                out.write("{\"status\": \"success\"}");
            } else {
                out.write("{\"status\": \"invalid command\"}");
            }
            out.flush();
        }
    }

    /**
     * Tries to parse the given command (from the AJAX-request) and adds it to
     *  the queue for execution.
     * @param command the command passed by the AJAX-request.
     * @return {@code true} if the command was successfully parsed and added for
     *  execution, {@code false} otherwise.
     */
    private boolean parseAndExecute(String command){
        try {
            // Try to parse and execute:
            Controller.Actions action = Controller.Actions.valueOf(command);
            Controller.INSTANCE.addAction(action);
            return true;
        } catch (IllegalArgumentException e){
            // Invalid command (couldn't parse):
            return false;
        }
    }
}
