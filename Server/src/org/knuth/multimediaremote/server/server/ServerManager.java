package org.knuth.multimediaremote.server.server;

import org.apache.log4j.Logger;
import org.knuth.multimediaremote.server.model.settings.Config;
import org.knuth.multimediaremote.server.server.observer.ServerStateChangeListener;
import org.knuth.multimediaremote.server.view.elements.ErrorDialog;

import java.net.*;
import java.util.*;

/**
 * @author Lukas Knuth
 * @version 1.1
 * Manages and starts/stops all registered server-services.</p>
 * Also offers a Observer to listen for any changes of the Servers.
 */
public enum ServerManager {

    /** Instance to work with */
    INSTANCE;

    /** The default size for all collections which are
     * related to servers.
     */
    private static final int DEFAULT_INITIAL_CAPABILITY = 3;

    /** The IP-Address of this machine */
    private String server_address;

    /** All available Servers are registered here */
    private Map<String, Server> servers;

    /** Indicates if the servers-map is locked (due to
     * the fact that the Servers currently run).
     */
    private boolean servers_locked;

    /** The servers which where unregistered while they where
     *  running. This list will be processed as soon as they
     *  stop.
     */
    private List<String> remove_later;

    /** All registered listeners to this Observer */
    private List<ServerStateChangeListener> listeners;


    /**
     * Singleton - Not instantiable.
     */
    private ServerManager(){
        servers = new HashMap<>(DEFAULT_INITIAL_CAPABILITY);
        remove_later = new ArrayList<>(DEFAULT_INITIAL_CAPABILITY);
        servers_locked = false;
        // Get the IP-Address:
        server_address = getLocalIP();
        // Initialize the list for the observer-part:
        listeners = new ArrayList<>(5);
        // Add servers:
        addDefaultServers();
    }

    /**
     * Registers the default servers to the ServerManager in order
     *  to make the basic program work.
     */
    private void addDefaultServers(){
        registerServer("mmr", new MmrServer());
        if (Boolean.valueOf(Config.INSTANCE.getProperty("webend"))){
            registerServer("http", new HttpServer());
        }
    }

    /**
     * Use the network interfaces to determine the current IPv4 address
     *  in the local network so the client can easily connect.
     * @return the IPv4 address of this machine in the local network or
     *  a message informing the user that there was a problem.
     * @see <a href="http://stackoverflow.com/questions/8765578">StackOverflow</a>
     */
    private String getLocalIP(){
        try {
            // Get all interfaces and search for the currently used:
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()){
                NetworkInterface current = interfaces.nextElement();
                // Check the interface:
                if (!current.isUp() || current.isLoopback() || current.isVirtual()) continue;
                Enumeration<InetAddress> addresses = current.getInetAddresses();
                while (addresses.hasMoreElements()){
                    InetAddress current_addr = addresses.nextElement();
                    // Check if it is an IPv4 address:
                    if (current_addr instanceof Inet4Address)
                        return current_addr.getHostAddress();
                }
            }
            // Otherwise, use the easier method (which is normally
            // correct, but might return "127.0.0.1" which does not
            // help.
            return Inet4Address.getLocalHost().getHostAddress();
        } catch (SocketException e) {
            Logger logger = Logger.getRootLogger();
            logger.error("Can't get local IP", e);
            new ErrorDialog(e).show();
        } catch (UnknownHostException e) {
            Logger logger = Logger.getRootLogger();
            logger.error("Can't get local IP", e);
            new ErrorDialog(e).show();
        }
        // This should not happen. If it does, inform the user:
        return "there has been a problem...";
    }

    /**
     * Notifies all registered listeners about the change in the
     *  servers state and provides information about the new state
     *  to them.
     */
    private void notifyAllListeners(){
        if (listeners.size() <= 0) return;
        // Collect all information:
        final Map<String, ServerState> states = new HashMap<>(DEFAULT_INITIAL_CAPABILITY);
        for (Map.Entry<String,Server> entry : servers.entrySet()){
            states.put(entry.getKey(), entry.getValue().getServerState());
        }
        // Create the container:
        ServerStateContainer container = new ServerStateContainer(states);
        // Send it away:
        for (ServerStateChangeListener listener : listeners){
            listener.serverStateChanged(container);
        }
    }
        
    /**
     * Remove the given listener from the list of this observer.</p>
     * After the listener has been removed from the list, it will
     *  no longer receive any updates about changing server states.
     * @param listener the listener which should be removed.
     */
    public void removeServerStateChangeListener(ServerStateChangeListener listener){
        listeners.remove(listener);
    }

    /**
     * Adds a new {@code ServerStateChangeListener} to this observer.</p>
     * After the new listener has been registered, it's
     *  {@code serverStateChanged()}-method will be called with information
     *  about the new state.
     * @param listener the listener which should be registered.
     */
    public void addServerStateChangeListener(ServerStateChangeListener listener){
        listeners.add(listener);
    }

    /**
     * Remove the Server with the given key-name from
     *  the Servers this class manages.
     * Servers are not guaranteed to be removed immediately
     *  after this method was called. If the servers are
     *  running when this method is called, they are batched
     *  and removed after the Servers shut down.
     * @param key the key-name of the server which should be
     *  unregistered.
     */
    public void removeServer(String key){
        if (servers_locked){
            // Servers are currently running, add to batch:
            System.out.println("Adding Server to batch: "+key);
            remove_later.add(key);
        } else {
            // Servers are not running, remove immediately:
            System.out.println("Popping off Server: "+key);
            servers.remove(key);
        }
    }

    /**
     * Register a server to the ServerManager. All
     *  registered servers will be started and stopped
     *  when the ServerManager tells them to.
     * @param key the key-name for the Server.
     * @param server the new Server to register.
     */
    public void registerServer(String key, Server server){
        System.out.println("Registering Server: "+key);
        // Initialize:
        server.init();
        servers.put(key, server);
    }

    /**
     * Processes all batched Servers which should be removed
     *  while the Server was running and finally remove them.
     */
    private void processRemoveBatch(){
        if (remove_later.size() <= 0) return;
        // Remove all batched Servers:
        for (String server_key : remove_later){
            System.out.println("Removing server from Batch: "+server_key);
            servers.remove(server_key);
        }
        // Clear the Batch:
        remove_later.clear();
    }

    /**
     * Starts all Servers and listens for incoming
     *  connections.
     */
    public void startServers(){
        // Lock the Servers:
        servers_locked = true;
        // Start all registered Servers:
        for (Server server : servers.values()){
            server.start();
        }
        // Notify all listeners:
        notifyAllListeners();
    }

    /**
     * Stops all started servers.
     */
    public void stopServers(){
        // Stop al registered Servers:
        for (Server server : servers.values()){
            server.stop();
        }
        // Remove all batched Servers:
        processRemoveBatch();
        // Unlock the Servers:
        servers_locked = false;
        // Notify all listeners:
        notifyAllListeners();
    }

    /**
     * Query's and caches the  IP-Address of this
     *  machine.
     * @return the IP-Address.
     */
    public String getCurrentIP() {
        return server_address;
    }

}
