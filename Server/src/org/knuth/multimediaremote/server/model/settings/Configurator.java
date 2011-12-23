package org.knuth.multimediaremote.server.model.settings;

import javax.swing.*;
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
class Configurator implements ActionListener, PropertyChangeListener {

    /** The JPanel to hold all the GUI-Elements */
    private JPanel overall;

    /** The field used to specify the servers port */
    private JFormattedTextField port_field;
    /** The check-box determining if the webend is launched */
    private JCheckBox webend;
    /** The button used to start the Server */
    private JButton starter;

    public Configurator(){
        // Create and set up the GUI-Representation
        setUp();
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
        overall.setLayout(new BoxLayout(overall, BoxLayout.Y_AXIS));
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
        overall.add(port);
        // Webend config:
        webend = new JCheckBox("Should I launch a Webend for the Server?");
        webend.addActionListener(this);
        boolean checked = Boolean.valueOf( Config.INSTANCE.getProperty("webend") ); // Load from prefs.
        webend.setSelected(checked);
        overall.add(webend);
        // Server starter
        starter = new JButton("Start Server");
        starter.addActionListener(this);
        overall.add(starter);
    }

    /**
     * ActionListener for the Server-Start button and the
     *  Webend-checkbox.
     */
    public void actionPerformed(ActionEvent e) {
        if (starter == e.getSource()){
            System.out.println("Starting Server...");
        } else if (webend == e.getSource()){
            Config.INSTANCE.setProperty("webend", webend.isSelected()+"");
        }
    }

    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getSource() == port_field){
            // Set the new Value:
            Config.INSTANCE.setProperty("port", evt.getNewValue().toString());
        }
    }
}
