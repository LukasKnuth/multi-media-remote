package org.knuth.multimediaremote.server.model.remotes;

import org.apache.log4j.Logger;

import java.io.File;

public final class DetermineOS {

    // TODO Make this a static utility class?

    /** Represents an operating system. */
    enum OperatingSystem{
        LINUX, MACOSX, WINDOWS;
    }

    /** NOT TO BE USED! Cached result of the OS-Determination */
    private OperatingSystem current_os;

    /**
     * Runs a test to determine which OS is currently running.
     *  <br>
     *  The result's are cached, so it's okay to run this method
     *  multiple times!
     * @throws IllegalArgumentException if the OS is unknown.
     * @return the current os as a {@code OperatingSystem-Object}.
     */
    OperatingSystem determineCurrentOS(){
        // Check if already set:
        if (current_os != null) return current_os;
        // Else, find out:
        Logger logger = Logger.getLogger("guiLogger");
        String os_str = System.getProperty("os.name").toLowerCase();
        if ("linux".startsWith(os_str)) this.current_os = OperatingSystem.LINUX;
        else if ("windows".startsWith(os_str)) this.current_os = OperatingSystem.WINDOWS;
        else if ("macosx".startsWith(os_str)) this.current_os = OperatingSystem.MACOSX;
        else {
            logger.error("Couldn't determine the current OS...");
            throw new IllegalArgumentException("Unknown OS-Type: "+os_str);
        }
        // Log the OS:
        logger.info("Found OS to be "+current_os);
        // Load the correct
        return this.current_os;
    }

    /**
     * Returns a {@code Remote}-implementation for the currently
     *  running OS.
     * @return a {@code Remote} for the currently running OS.
     */
    public Remote getNativeRemote(){
        // Check which System...
        switch (determineCurrentOS()){
            case LINUX:
                Logger logger = Logger.getLogger("guiLogger");
                File lib_file = new File("natives/libLinuxRemote.so");
                if (!lib_file.exists()){
                    System.out.println(lib_file.getAbsolutePath());
                    logger.error("Can't find the native library's in the \"natives\"-directory!");
                    throw new RuntimeException("Cant find native library!"); // TODO Better Error-handling (do this in bootstrap and don't start Server)
                }
                System.load(lib_file.getAbsolutePath());
                return new NativeRemote();
            case WINDOWS:
            case MACOSX:
            default:
                throw new IllegalArgumentException("No Native Remote for this OS found!");
        }
    }

}
