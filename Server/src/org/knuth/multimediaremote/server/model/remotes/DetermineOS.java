package org.knuth.multimediaremote.server.model.remotes;

public class DetermineOS {

    private enum OperatingSystem{
        LINUX, MACOSX, WINDOWS;
    }
    private OperatingSystem currentOs;

    /**
     * Runs a test to determine which OS is currently running.
     *  <br>
     *  The result's are cached, so it's okay to run this method
     *  multiple times!
     * @throws IllegalArgumentException if the OS is unknown.
     * @return The current os as a {@code OperatingSystem-Object}.
     */
    private OperatingSystem determineCurrentOS(){
        if (currentOs != null) return currentOs;
        String os_str = System.getProperty("os.name").toLowerCase();
        if ("linux".startsWith(os_str)) this.currentOs = OperatingSystem.LINUX;
        else if ("windows".startsWith(os_str)) this.currentOs = OperatingSystem.WINDOWS;
        else if ("macosx".startsWith(os_str)) this.currentOs = OperatingSystem.MACOSX;
        else {
            throw new IllegalArgumentException("Unknown OS-Type: "+os_str);
        }
        return this.currentOs;
    }

    /**
     * Returns a {@code Remote}-implementation for the currently
     *  running OS.
     * @return a {@code Remote} for the currently running OS.
     */
    public Remote getNativeRemote(){
        // Check which System...
        return new NativeLinuxRemote(); // E.G.!
    }

}
