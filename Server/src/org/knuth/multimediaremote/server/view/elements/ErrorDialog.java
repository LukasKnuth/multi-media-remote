package org.knuth.multimediaremote.server.view.elements;

import org.knuth.multimediaremote.server.model.settings.Config;

import javax.swing.*;
import java.awt.*;

/**
 * @author Lukas Knuth
 * @version 1.0
 * A dialog shown if an error occurred and the user should
 *  be notified to report it.
 */
public class ErrorDialog {

    /** The JOptionPane used to display the problem and instructions */
    private JOptionPane pane;

    /** The title for the dialog */
    private final static String title = "This should not have happened...";

    /** The basic instructions to report a Bug/Issue */
    private final static String report_instructions = "<p>Please take " +
            "a moment to <b>report this problem</b>, so we can fix it.</p>" +
            "<p>You can report the problem by either:</p>" +
            "<ul><li>opening a <b>Ticket</b> at  " +
                "<input type=\"text\" size=\"20\" value=\"http://github.com\" />" +
            "<li>or sending an <b>E-Mail</b> to  " +
                "<input type=\"text\" size=\"20\" value=\"mr.luke.187@googlemail.com\" />" +
            "</ul>" +
            "<p>If you decide to do so, please <u>add the contents of your Log-file</u> to your report!</p>" +
            "<p><b>Log-File:</b>  <input type=\"text\" size=\"40\" value=\"" +
                Config.getBaseDir().toString()+"/logging.log" +
            "\"/></p>" +
            "<p>Thanks for taking the time!</p></html>";

    /**
     * Create a new ErrorDialog with the given message.</p>
     *  To show the dialog, use the {@code show()}-method.
     * @param message the message to present on the dialog.
     */
    public ErrorDialog(String message){
        setUp(message);
    }

    /**
     * Create a new ErrorDialog with the message from the
     *  supplied Throwable.</p>
     *  To show the dialog, use the {@code show()}-method.
     * @param exception the exception to get the message from.
     */
    public ErrorDialog(Throwable exception){
        setUp(exception.getMessage());
    }

    /**
     * Set up the dialog and it's elements and make it ready
     *  to be presented.
     * @param message the message to show on the dialog.
     */
    private void setUp(String message){
        pane = new JOptionPane(getTextPane(message),
                JOptionPane.ERROR_MESSAGE,
                JOptionPane.CLOSED_OPTION);
    }

    /**
     * Set up the options to make the {@code JTextPane} look like a
     *  {@code JLabel}.</p>
     * This adds support for clickable Hyper-Links.
     * @param message the error-message to show in this Label.
     * @return the modified {@code JTextPane}.
     */
    private JTextPane getTextPane(String message){
        final JTextPane text_pane = new JTextPane();
        text_pane.setEditable(false);
        text_pane.setBackground(new Color(238, 238, 238)); // Default BG-color of JLabel
        text_pane.setContentType("text/html");
        text_pane.setText("<html><p>"+message+"</p>"+report_instructions);
        return text_pane;
    }

    /**
     * Show the dialog.
     */
    public void show(){
        pane.createDialog(null, title).setVisible(true);
    }

}
