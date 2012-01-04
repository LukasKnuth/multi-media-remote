package org.knuth.multimediaremote.server.model.remotes;

import org.apache.log4j.Logger;
import org.knuth.multimediaremote.server.model.exceptions.LibraryNotFoundException;
import org.knuth.multimediaremote.server.model.exceptions.UnknownOSException;

import java.io.File;

public final class DetermineOS {

    /** Represents an operating system. */
    enum OperatingSystem{
        LINUX, MACOSX, WINDOWS;
    }

    /** NOT TO BE USED! Cached result of the OS-Determination */
    private static OperatingSystem current_os;

    /** Caches one instance of a NativeRemote which is returned
     * multiple times when the getNativeRemote()-method is invoked.
     */
    private static NativeRemote native_remote_cache;

    /**
     * Runs a test to determine which OS is currently running.
     *  <br>
     *  The result's are cached, so it's okay to run this method
     *  multiple times!
     * @return the current os as a {@code OperatingSystem-Object}.
     */
    static OperatingSystem determineCurrentOS(){
        // Check if already set:
        if (current_os != null) return current_os;
        // Else, find out:
        final OperatingSystem temp;
        Logger logger = Logger.getLogger("guiLogger");
        String os_str = System.getProperty("os.name").toLowerCase();
        // Check the OS-String.
        if ("linux".startsWith(os_str)) temp = OperatingSystem.LINUX;
        else if ("windows".startsWith(os_str)) temp = OperatingSystem.WINDOWS;
        else if ("macosx".startsWith(os_str)) temp = OperatingSystem.MACOSX;
        else {
            logger.error("Couldn't determine an OS for: "+os_str);
            // TODO Prompt user to write email/file bug with log-file
            throw new UnknownOSException(os_str);
        }
        // Log the OS:
        logger.info("Found OS to be "+temp);
        current_os = temp;
        return temp;
    }

    /**
     * Returns a native {@code Remote}-implementation for the
     *  currently running OS.
     * @return a native {@code Remote} for the currently running OS.
     */
    public static Remote getNativeRemote() {
        // Check if already initialized and cached:
        if (native_remote_cache != null) return native_remote_cache;
        // Otherwise check which System...
        Logger logger = Logger.getLogger("guiLogger");
        switch (determineCurrentOS()){
            case LINUX:
                File lib_file = new File("natives/libLinuxRemote.so");
                if (!lib_file.exists()){
                    logger.error("Can't find the native library's in the \"natives\"-directory!");
                    throw new LibraryNotFoundException(lib_file.getAbsolutePath());
                }
                System.load(lib_file.getAbsolutePath()); // TODO What if loading doesn't work??
                // Cache the Remote:
                native_remote_cache = new NativeRemote();
                return native_remote_cache;
            case WINDOWS:
            case MACOSX:
            default:
                // Can't happen, since the determineCurrentOS()-method will throw
                // a RuntimeException, which will cause to program to halt.
                throw new RuntimeException("Unknown OS-Type");
        }
    }

    /**
     * Static Utility Class - Not instantiable.
     */
    private DetermineOS(){}

}
