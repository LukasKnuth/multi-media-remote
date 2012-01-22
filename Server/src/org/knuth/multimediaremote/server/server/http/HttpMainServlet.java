package org.knuth.multimediaremote.server.server.http;

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
            out.write("{\"status\": \"success\"}");
            out.flush();
        }
    }
}
