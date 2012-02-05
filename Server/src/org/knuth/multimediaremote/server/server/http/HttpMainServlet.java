package org.knuth.multimediaremote.server.server.http;

import org.apache.log4j.Logger;
import org.knuth.multimediaremote.server.controller.Controller;
import org.knuth.multimediaremote.server.model.settings.Config;

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
        String contents;
        PrintWriter out = null;
        try {
            contents = getIndexContents();
            out = new PrintWriter(resp.getWriter(), false);
            resp.setContentType("text/html");
            out.write(contents);
            out.flush();
        } catch (IOException e){
            // Problem reading from the file:
            Logger.getLogger("guiLogger").error("There was a problem reading from the index file", e);
            resp.sendError(404);
        } finally {
            if (out != null) out.close();
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
        FileInputStream is = new FileInputStream(new File(Config.getBaseDir(), "/res/index.html"));
        if (is == null){
            // If the "index.html"-file could not be found:
            throw new FileNotFoundException("Couldn't find \"res/index.html\"-file");
        }
        // Read the file and cache the contents:
        StringBuffer buffer = new StringBuffer();
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(is));
            // Copy contents:
            String line;
            while ( (line = in.readLine()) != null)
                buffer.append(line);
        } finally {
            if (in != null) in.close();
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
        // Write:
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(resp.getWriter());
            if (parseAndExecute( req.getParameter("action") )){
                out.write("{\"status\": \"success\"}");
            } else {
                out.write("{\"status\": \"invalid command\"}");
            }
            out.flush();
        } finally {
            if (out != null) out.close();
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
