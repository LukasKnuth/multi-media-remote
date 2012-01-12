package org.knuth.multimediaremote.server.model.remotes;

import org.apache.log4j.Logger;
import org.knuth.multimediaremote.server.model.exceptions.LibraryNotFoundException;
import org.knuth.multimediaremote.server.model.exceptions.UnknownOSException;
import org.knuth.multimediaremote.server.model.settings.Config;

import java.io.File;

public final class DetermineOS {

    /** Represents an operating system. */
    enum OperatingSystem{
        LINUX, MACOSX, WINDOWS;
    }

    /** NOT TO BE USED! Cached result of the OS-Determination */
    private static OperatingSystem current_os;

    /** Used to determine whether the system is 32 or 64 bit */
    private enum SystemArch{
        x86, x86_64
    }

    /** The currently running Arch */
    private static SystemArch current_arch;

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
        final String os_str = System.getProperty("os.name").toLowerCase();
        // Check the OS-String.
        if ("linux".startsWith(os_str)) temp = OperatingSystem.LINUX;
        else if ("windows".startsWith(os_str)) temp = OperatingSystem.WINDOWS;
        else if ("macosx".startsWith(os_str)) temp = OperatingSystem.MACOSX;
        else {
            logger.error("Couldn't determine an OS for: "+os_str);
            throw new UnknownOSException(os_str);
        }
        // Check the Arch:
        // @see http://stackoverflow.com/questions/807263
        // TODO Use real check from link above and move this to another private method.
        final SystemArch arch;
        final String arch_str = System.getProperty("os.arch").toLowerCase();
        if ("i386".equals(arch_str)) arch = SystemArch.x86; // 32 Bit
        else if ("amd64".startsWith(arch_str) || "x86_64".startsWith(arch_str))
            arch = SystemArch.x86_64; // 64 Bit
        else {
            logger.error("Unsupported system architecture: "+arch_str);
            // TODO Throw UnknownOSException or make new one?
            throw new RuntimeException("Unknown Arch");
        }
        // Log the OS:
        logger.info("Found OS to be "+temp+" - "+arch);
        current_os = temp;
        current_arch = arch;
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
        // Check which OS and load it's library:
        switch (determineCurrentOS()){
            case LINUX:
                // Check the Arch:
                final File lib_file;
                switch (current_arch){
                    case x86: // 32 Bit
                        lib_file = new File(Config.getBaseDir(), "natives/libLinuxRemote_x86.so");
                        break;
                    case x86_64: // 64 Bit
                        lib_file = new File(Config.getBaseDir(), "natives/libLinuxRemote_x86_64.so");
                        break;
                    default:
                        // Can't happen...
                        // TODO Do better job here...
                        lib_file = Config.getBaseDir();
                }
                // Load the library:
                if (!lib_file.exists()){
                    logger.error("Can't find the native library's in the \"natives\"-directory!");
                    throw new LibraryNotFoundException(lib_file.getAbsolutePath());
                }
                try {
                    System.load(lib_file.getAbsolutePath());
                } catch (UnsatisfiedLinkError e){
                    // Couldn't load the Library.
                    // TODO Do proper error-handling and prompt user
                    logger.error("Couldn't load the native library", e);
                }
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
