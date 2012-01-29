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
    private Map<Class, AbstractServer> servers;

    /** Indicates if the servers-map is locked (due to
     * the fact that the Servers currently run).
     */
    private boolean servers_locked;

    /** All registered listeners to this Observer */
    private List<ServerStateChangeListener> listeners;


    /**
     * Singleton - Not instantiable.
     */
    private ServerManager(){
        servers = new HashMap<Class, AbstractServer>(DEFAULT_INITIAL_CAPABILITY);
        servers_locked = false;
        // Get the IP-Address:
        server_address = getLocalIP();
        // Initialize the list for the observer-part:
        listeners = new ArrayList<ServerStateChangeListener>(5);
        // Add servers:
        addDefaultServers();
    }

    /**
     * Registers the default servers to the ServerManager in order
     *  to make the basic program work.
     */
    private void addDefaultServers(){
        registerServer(MmrServer.class);
        if (Boolean.valueOf(Config.INSTANCE.getProperty("webend"))){
            registerServer(HttpServer.class);
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
        final Map<Class, ServerState> states = new HashMap<Class, ServerState>(DEFAULT_INITIAL_CAPABILITY);
        for (Map.Entry<Class,AbstractServer> entry : servers.entrySet()){
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
     * Remove the {@code AbstractServer} with the given class from
     *  the Servers this class manages.
     * If the server is currently running, nothing will happen.
     * @param server_class the {@code Class} of the server which should be
     *  unregistered.
     */
    public void removeServer(Class server_class){
        if (!servers_locked){
            // Servers are not running, remove immediately:
            System.out.println("Popping off AbstractServer: "+server_class);
            servers.remove(server_class);
        }
    }

    /**
     * Register a server to the ServerManager. All
     *  registered servers will be started and stopped
     *  when the ServerManager tells them to.</p>
     * If the given Server is already registered, this method will
     *  terminate gracefully and do nothing.
     * @param server_class the {@code Class} of the AbstractServer.
     */
    public void registerServer(Class server_class){
        // Check if already registered:
        if (servers.containsKey(server_class)) return;
        // Otherwise, regiser:
        System.out.println("Registering AbstractServer: "+server_class);
        try {
            AbstractServer new_server = loadServerInstance(server_class);
            // Initialize:
            new_server.init();
            servers.put(server_class, new_server);
        } catch (ClassCastException e){
            Logger.getLogger("guiLogger").error("Can't register Server", e);
        }
    }

    /**
     * Try to create an instance of the given class.</p>
     *  This method will succeed if the given class implements the
     *  {@code AbstractServer}-interface.
     * @param server_class the class to create an instenace from.
     * @return the created instance of {@code AbstractServer}
     * @throws ClassCastException if the given class does not implement
     *  the {@code AbstractServer}-interface.
     */
    private AbstractServer loadServerInstance(Class server_class)
            throws ClassCastException{
        // TODO Later do this with reflection?
        // Check if AbstractServer
        if (server_class == HttpServer.class)
            return new HttpServer();
        if (server_class == MmrServer.class)
            return new MmrServer();
        else throw new ClassCastException("Can't cast "+server_class+" to "+AbstractServer.class);
    }

    /**
     * Starts all Servers and listens for incoming
     *  connections.
     */
    public void startServers(){
        // Lock the Servers:
        servers_locked = true;
        // Start all registered Servers:
        for (AbstractServer server : servers.values()){
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
        for (AbstractServer server : servers.values()){
            server.stop();
        }
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
