package org.knuth.multimediaremote.server.model.exceptions;

/**
 * @author Lukas Knuth
 * @version 1.0
 * Thrown if the operating system could not be determined.
 */
public class UnknownOSException extends RuntimeException{

    /**
     * A RuntimeException which indicates, that the current OS
     *  could not be determined with the given OS-name.
     * @param found_os_name the OS-name which couldn't be "parsed"
     *  to an operating system.
     */
    public UnknownOSException(String found_os_name){
        super("Unknown OS-Type: "+found_os_name);
    }
}
