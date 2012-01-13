package org.knuth.multimediaremote.server.model.settings;

import org.apache.log4j.Logger;
import org.knuth.multimediaremote.server.server.ServerManager;
import org.knuth.multimediaremote.server.server.ServerState;
import org.knuth.multimediaremote.server.server.ServerStateContainer;
import org.knuth.multimediaremote.server.server.observer.ServerStateChangeListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.NumberFormat;

/**
 * @author Lukas Knuth
 * @version 1.0
 * The GUI-representation of the configuration.
 */
class Configurator implements ActionListener, PropertyChangeListener, ServerStateChangeListener {

    /** The JPanel to hold all the GUI-Elements */
    private JPanel overall;

    /** The field used to specify the servers port */
    private JFormattedTextField port_field;
    /** The check-box determining if the webend is launched */
    private JCheckBox webend;
    /** The button used to start the Server */
    private JButton starter;

    /**
     * Hosts GUI-elements to change all public writable
     *  properties.
     */
    public Configurator(){
        // Create and set up the GUI-Representation
        setUp();
        // Register ServerStateChangeListener:
        ServerManager.INSTANCE.addServerStateChangeListener(this);
    }

    /**
     * Returns the Swing-representation of the settings.
     * @return a JPanel holding all elements.
     */
    public JPanel getView(){
        return overall;
    }

    /**
     * Sets up the GUI-elements and their listeners.
     */
    private void setUp(){
        overall = new JPanel();
        overall.setLayout(new BoxLayout(overall, BoxLayout.PAGE_AXIS));
        // Port-config:
        JPanel port = new JPanel();
        port.add(new JLabel("Port for the Server:"));
        NumberFormat format = NumberFormat.getIntegerInstance();
        format.setGroupingUsed(false);
        port_field = new JFormattedTextField(format);
        port_field.setColumns(6);
        port_field.setText(Config.INSTANCE.getProperty("port")); // Load from prefs.
        port_field.addPropertyChangeListener("value", this);
        port.add(port_field);
        port.setAlignmentX(Component.CENTER_ALIGNMENT);
        overall.add(port);
        // Webend config:
        webend = new JCheckBox("Should I launch a Webend for the Server?");
        webend.addActionListener(this);
        boolean checked = Boolean.valueOf( Config.INSTANCE.getProperty("webend") ); // Load from prefs.
        webend.setSelected(checked);
        webend.setAlignmentX(Component.CENTER_ALIGNMENT);
        overall.add(webend);
        // Server starter
        starter = new JButton("Start Server");
        starter.setActionCommand("start");
        starter.addActionListener(this);
        starter.setAlignmentX(Component.CENTER_ALIGNMENT);
        overall.add(starter);
    }

    /**
     * ActionListener for the Server-Start button and the
     *  Webend-checkbox.
     */
    public void actionPerformed(ActionEvent e) {
        if (starter == e.getSource()){
            if ("start".equals(e.getActionCommand()) ){
                System.out.println("Starting Server...");
                ServerManager.INSTANCE.startServers();
            } else if ("stop".equals(e.getActionCommand()) ){
                System.out.println("Stopping Server...");
                ServerManager.INSTANCE.stopServers();
            }
        } else if (webend == e.getSource()){
            // TODO Register HTTP-Server to the ServerManager
            Config.INSTANCE.setProperty("webend", webend.isSelected() + "");
        }
    }

    @Override
    public void serverStateChanged(ServerStateContainer container) {
        Logger logger = Logger.getLogger("guiLogger");
        if (container.containsStateFor("mmr")){
            ServerState state = container.getStateFor("mmr");
            switch (state.getCurrentState()){
                case RUNNING:
                    starter.setActionCommand("stop");
                    starter.setText("Stop Server");
                    logger.info("Server successfully started.");
                    break;
                case STOPPED:
                    starter.setActionCommand("start");
                    starter.setText("Start Server");
                    logger.info("Server stopped.");
                    break;
            }
        }
    }

    /**
     * Called when the Port was changed.
     */
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getSource() == port_field){
            // Set the new Value:
            Config.INSTANCE.setProperty("port", evt.getNewValue().toString());
        }
    }

}
