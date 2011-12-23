package org.knuth.multimediaremote.server.model.remotes;

import org.apache.log4j.Logger;

public final class DetermineOS {

    /** Represents an operating system. */
    private enum OperatingSystem{
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
     * @return The current os as a {@code OperatingSystem-Object}.
     */
    private OperatingSystem determineCurrentOS(){
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
                return new NativeLinuxRemote();
            case WINDOWS:
            case MACOSX:
            default:
                throw new IllegalArgumentException("No Native Remote for this OS found!");
        }
    }

}
