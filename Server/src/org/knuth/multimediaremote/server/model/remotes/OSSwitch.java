package org.knuth.multimediaremote.server.model.remotes;

import javax.swing.*;
import java.net.URL;

/**
 * @author Lukas Knuth
 * @version 1.0
 * An element for the Swing window toolkit to show, which OS
 *  is currently running.
 */
public class OSSwitch {

    /** Holder for the Logo-Elements */
    private final JPanel all;

    /** Stores the current OS-name for the Tooltip */
    private String os_name;

    /**
     * Create a new OSSwitch-Window element to show the current
     *  operating system.
     */
    public OSSwitch(){
        all = new JPanel();
        all.setLayout(new BoxLayout(all, BoxLayout.X_AXIS));
        // Add the Shown Logos:
        addLogos();
        // Add the Tooltip:
        all.setToolTipText("We found your OS to be "+os_name);
    }

    /**
     * Adds the logos of the different OS-Types to the
     *  Panel.
     */
    private void addLogos(){
        // Apple
        URL apple_url = OSSwitch.class.getResource("/org/knuth/multimediaremote/server/view/res/apple_logo.png");
        ImageIcon apple = new ImageIcon(apple_url);
        // Linux
        URL linux_url = OSSwitch.class.getResource("/org/knuth/multimediaremote/server/view/res/linux_logo.png");
        ImageIcon linux = new ImageIcon(linux_url);
        // Windows
        URL windows_url = OSSwitch.class.getResource("/org/knuth/multimediaremote/server/view/res/windows_logo.png");
        ImageIcon windows = new ImageIcon(windows_url);
        // Grey others:
        DetermineOS.OperatingSystem os = DetermineOS.determineCurrentOS();
        switch (os){
            case LINUX:
                // Make Windows and Mac gray:
                apple = new ImageIcon(GrayFilter.createDisabledImage(apple.getImage()) );
                windows = new ImageIcon(GrayFilter.createDisabledImage(windows.getImage()) );
                os_name = "Linux";
                break;
            case WINDOWS:
                linux = new ImageIcon(GrayFilter.createDisabledImage(linux.getImage()) );
                apple = new ImageIcon(GrayFilter.createDisabledImage(apple.getImage()) );
                os_name = "Windows";
                break;
            case MACOSX:
                windows = new ImageIcon(GrayFilter.createDisabledImage(windows.getImage()) );
                linux = new ImageIcon(GrayFilter.createDisabledImage(linux.getImage()) );
                os_name = "Mac OS X";
                break;
            default:
                // If not determined, gray them all.
                windows = new ImageIcon(GrayFilter.createDisabledImage(windows.getImage()) );
                linux = new ImageIcon(GrayFilter.createDisabledImage(linux.getImage()) );
                apple = new ImageIcon(GrayFilter.createDisabledImage(apple.getImage()) );
        }
        // Put them on the Panel:
        all.add(new JLabel(apple));
        all.add(new JLabel(linux));
        all.add(new JLabel(windows));
    }

    /**
     * Get a View on this OSSwitcher to add it to a Swing-Window/Panel
     * @return the view of this OSSwitcher element.
     */
    public JPanel getView(){
        return all;
    }
}
