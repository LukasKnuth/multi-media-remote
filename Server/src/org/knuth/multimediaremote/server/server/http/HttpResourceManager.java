package org.knuth.multimediaremote.server.server.http;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.AccessDeniedException;

/**
 * @author Lukas Knuth
 * @version 1.0
 * This class manages access to all resources which should be
 *  available from the HTTP-Server implementation.
 */
public class HttpResourceManager {

    /** The name of the index-file if no file-name was given */
    private static final String INDEX_FILE_NAME = "index.html";

    /** Possible states of the requested file */
    private enum FileState{
        NOT_FOUND,
        ACCESS_DENIED,
        OK
    }

    /**
     * Parses the given file-name, reads the contents of that
     *  resource-file (if it exists) and creates the complete
     *  HTTP-response string (with headers).</p>
     * If the file does not exist or can't be accessed, this
     *  method will add the corresponding HTTP-status code
     *  to the returned String. There is no need for any error-
     *  handling!
     * @param res_name the name of the desired resource-file.
     * @return the HTTP-header and contents of that file.
     * @throws NullPointerException if the given {@code res_name}
     *  is {@code null}.
     */
    public static String getResourceContents(String res_name) 
           throws NullPointerException{
        // Check argument:
        if (res_name == null) throw new NullPointerException();
        // Try to get a URL on the resource:
        URL res_url = null;
        try {
            res_url = getResourceURL(res_name);
        } catch (FileNotFoundException e) {
            String header = generateHeader(FileState.NOT_FOUND, null);
            String content = "<h1>Not found...</h1>";
            return header+content;
        } catch (AccessDeniedException e) {
            String header = generateHeader(FileState.ACCESS_DENIED, null);
            String content = "<h1>Access Denied</h1>";
            return header+content;
        }
        // Read contents from that URL:
        String res_file = res_url.getFile().substring(res_url.getFile().lastIndexOf("/")+1);
        String header = generateHeader(FileState.OK, res_file);
        String content = readContents(res_url);
        return header+content;
    }

    /**
     * Generate a header for the given {@code FileState}.
     * @param state the state of the requested file.
     * @param res_name the name of the resource (to get the
     *  correct MIME-type), or {@code null} if an error
     *  occurred.
     * @return the corresponding header.
     */
    private static String generateHeader(FileState state, String res_name){
        StringBuilder header = new StringBuilder();
        // Status code:
        switch (state){
            case OK:
                header.append("HTTP/1.1 200 OK\r\n");
                String content_type = URLConnection.guessContentTypeFromName(res_name);
                if (content_type == null) content_type = "text/plain";
                System.out.println(res_name+" > "+content_type);
                header.append("Content-Type: "+content_type+"; charset=UTF-8\r\n\"");
                break;
            case NOT_FOUND:
                header.append("HTTP/1.1 404 Not Found\r\n");
                // Content-type:
                header.append("Content-Type: text/html; charset=UTF-8\r\n");
                break;
            case ACCESS_DENIED:
                header.append("HTTP/1.1 403 Access Denied\r\n");
                // Content-type:
                header.append("Content-Type: text/html; charset=UTF-8\r\n");
        }
        // Server:
        header.append("Server: MultiMediaRemote\r\n");
        // Connection (no keep-alive):
        header.append("Connection: close\r\n");
        // Content from here:
        header.append("\r\n");
        return header.toString();
    }

    /**
     * Read the contents from the given URL and parse them to
     *  a Base64 String.
     * @param res_url the URL to read from.
     * @return the Base64 representation of the binary content
     *  from the given URL.
     */
    private static String readContents(URL res_url){
        InputStream in = null;
        ByteArrayOutputStream out = null;
        try {
            in = res_url.openStream();
            out = new ByteArrayOutputStream();
            // Read:
            byte[] buffer = new byte[512];
            int read;
            while ((read = in.read(buffer, 0, buffer.length)) != -1){
                out.write(buffer, 0, read);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // Clean Up:
            try {
                if (in != null) in.close();
                if (out != null) out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // TODO Does not work for binary files like pictures.
        try {
            return out.toString("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return  out.toString();
    }

    /**
     * Parses the given resource-name and tries to get a URL on
     *  the resource.
     * @param res_name the name of the resource to load.
     * @return a {@code URL}-object on that given resource, if it
     *  could be found and accessed.
     * @throws FileNotFoundException if the resource could not be
     *  found.
     * @throws  AccessDeniedException if the resource was found but
     *  could not be accessed. This usually happens if the browser
     *  is trying to access a file not located in the {@code res}-
     *  package.
     */
    private static URL getResourceURL(String res_name)
            throws FileNotFoundException, AccessDeniedException{
        // Check if relative path:
        if (res_name.startsWith("..")) throw new AccessDeniedException(res_name);
        if (res_name.startsWith("/")){
            res_name = res_name.substring(1);
        }
        // Check if index-file:
        if (res_name.equals("")) res_name = INDEX_FILE_NAME;
        // Try to get a URL:
        URL res_url = HttpResourceManager.class.getResource("res/"+res_name);
        // Check if available:
        if (res_url == null) throw new FileNotFoundException();
        else return res_url;
    }
    
    /** Not instantiable */
    private HttpResourceManager(){}
}
