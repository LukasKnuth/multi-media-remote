package org.knuth.multimediaremote.server.model.exceptions;

/**
 * @author Lukas Knuth
 * @version 1.0
 * Thrown if the native library could not be found and loaded.
 */
public class LibraryNotFoundException extends RuntimeException{

    /**
     * A RuntimeException which indicates, that the searched
     *  native library could not be found and loaded.
     * @param search_path the path in which the library was
     *  searched but not found.
     */
    public LibraryNotFoundException(String search_path){
        super("Couldn't find the native library under: "+search_path);
    }

}
