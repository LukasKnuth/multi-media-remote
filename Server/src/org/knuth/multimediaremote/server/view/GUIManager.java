package org.knuth.multimediaremote.server.view;

import org.apache.log4j.Logger;
import org.knuth.multimediaremote.server.controller.Controller;
import org.knuth.multimediaremote.server.model.remotes.OSSwitch;
import org.knuth.multimediaremote.server.model.settings.Config;
import org.knuth.multimediaremote.server.server.ServerManager;
import org.knuth.multimediaremote.server.view.elements.Instructions;
import org.knuth.multimediaremote.server.view.elements.Log;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;

/**
 * @author Lukas Knuth
 * @version 1.0
 * Manages the Elements on the UI and their interaction.
 */
public class GUIManager {

    /** The JFrame which is shown by the Application */
    private final JFrame f;

    /** The tray-icon to minimize the application to */
    private TrayIcon tray;

    /**
     * Set's up the components for the GUI.
     */
    private void setUp(){
        // Basic Layout Container:
        JPanel overall = new JPanel();
        overall.setLayout(new BoxLayout(overall, BoxLayout.PAGE_AXIS));
        f.add(overall);
        // Elements:
        // LOGO
        ImageIcon logo = new ImageIcon(GUIManager.class.getResource("res/logo_platzhalter.png"));
        JLabel logo_label = new JLabel(logo);
        logo_label.setAlignmentX(Component.CENTER_ALIGNMENT);
        overall.add(logo_label);
        overall.add(new JSeparator(JSeparator.HORIZONTAL));
        // INSTRUCTIONS
        JLabel instructions = new Instructions().getLabel();
        instructions.setAlignmentX(Component.CENTER_ALIGNMENT);
        overall.add(instructions);
        // OS-SWITCHER
        overall.add(new OSSwitch().getView());
        overall.add(new JSeparator(JSeparator.HORIZONTAL));
        // SETTINGS
        JPanel settings = Config.INSTANCE.getView();
        overall.add(settings);
        overall.add(new JSeparator(JSeparator.HORIZONTAL));
        // LOG
        overall.add(new Log().getView());
    }

    /**
     * Does the basic set-up for the {@code JFrame} and gets
     *  everything ready to show.</p>
     * To show the Frame, call the {@code present()}-method.
     * @see org.knuth.multimediaremote.server.view.GUIManager#present()
      */
    public GUIManager(){
        f = new JFrame("MultiMediaRemote Server Application");
        f.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        f.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent w) {
                // Minimize or close?
                if (tray != null && ServerManager.INSTANCE.isServerRunning()){
                    // Tray is supported, use it!
                    minimizeToTray();
                } else if (tray == null && ServerManager.INSTANCE.isServerRunning()){
                    // Tray is not supported, ask to close the program:
                    int close = JOptionPane.showConfirmDialog(f,
                            "Some Server-instances are still running!\nDo you want me to " +
                                    "shut them down and exit the program?",
                            "Servers are still running", JOptionPane.OK_CANCEL_OPTION);
                    if (close == JOptionPane.OK_OPTION){
                        disposeAndShutdown();
                    } else return; // Cancel shutdown.
                } else {
                    // Nothing is running, so close.
                    disposeAndShutdown();
                }
            }
        });
        f.setResizable(false);
        // Set up the All the Control-Elements:
        setUp();
        f.pack();
        // Set up the System-tray:
        setUpSystemTray();
    }

    /**
     * Calling this method will cause the Window to be disposed
     *  and the application to be shutdown.</p>
     * Shutting down the application includes saving the configs,
     *  shutting down all running servers and the controller.</p>
     * Calling this method to shut down the application will cause
     *  it to exit with an exit-code of {@code 0}.
     */
    private void disposeAndShutdown(){
        // Save the config:
        Config.INSTANCE.close();
        // Shutdown all Servers:
        ServerManager.INSTANCE.stopServers();
        // Shutdown the Controller:
        Controller.INSTANCE.shutdown();
        // Dispose the Window:
        f.dispose();
        // Exit:
        System.exit(0);
    }

    /**
     * Do final work and show the GUI.
     */
    public void present(){
        f.setVisible(true);
        Logger.getLogger("guiLogger").info("Successfully build the GUI");
    }

    /**
     * Minimize the application to the tray and show a message informing
     *  the user that the application is minimized.</p>
     * If there was a problem minimizing to the tray, this method will ensure
     *  that the window is displayed and log the problem.
     */
    private void minimizeToTray(){
        try {
            SystemTray.getSystemTray().add(tray);
            f.setVisible(false);
            tray.displayMessage(
                    "Down here!", "We could show the IP's here...", TrayIcon.MessageType.INFO
            );
        } catch (Exception e) {
            Logger.getLogger("guiLogger").error("Could not minimize the Server to the tray.", e);
            // Better save then sorry:
            f.setVisible(true);
        }
    }

    /**
     * Checks whether the system-tray is usable and sets everything up.</p>
     * This method should only be called once.
     */
    private void setUpSystemTray(){
        // Check if supported:
        if (!SystemTray.isSupported()){
            Logger.getLogger("guiLogger").error("Your System does not support Tray icons.");
            return;
        }
        // Get an Icon:
        // TODO Linux problem with transparent icons... fix?
        URL tray_icon_url = GUIManager.class.getResource("res/tray_icon.gif");
        ImageIcon tray_icon = new ImageIcon(tray_icon_url);
        // Set everything up:
        tray = new TrayIcon(tray_icon.getImage(), "MMR Tray");
        tray.setImageAutoSize(true);
        // Action Listener:
        tray.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                // Remove the tray-icon:
                SystemTray.getSystemTray().remove(tray);
                // Show the window again:
                f.setVisible(true);
            }
        });
    }
    
}
