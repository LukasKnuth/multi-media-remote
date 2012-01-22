package org.knuth.multimediaremote.server.server.http;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author Lukas Knuth
 * @version 1.0
 * This is the main servlet which offers the controls and handles
 *  the incoming POST-requests.
 */
public class HttpMainServlet extends HttpServlet{

    /**
     * Gets called when the servlet is requested and returns the controls
     *  to send requests to the Server.
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("text/html");
        PrintWriter out = null;
        try {
            out = new PrintWriter(resp.getWriter());
            out.write("<h1>Hello World!</h1>");
            out.write("<img src=\"test.png\" alt=\"shit\">");
            out.flush();
        } finally {
            if (out != null) out.close();
        }
    }

    /**
     * Gets called when a AJAX request was sent to the Servlet to execute
     *  a given action on the MMR-Server.
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException{

    }
}
