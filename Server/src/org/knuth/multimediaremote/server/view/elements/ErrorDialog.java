package org.knuth.multimediaremote.server.view.elements;

import javax.swing.*;

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

    private final static String report_instructions = "<html><p>Please take " +
            "a moment to report this problem, so we can fix it.</p>" +
            "<p>You can report the problem by either:</p>" +
            "<ul><li>opening a Ticket at <a href=\"http://github.com\">github</a></li>" +
            "<li>or sending an E-Mail to <a href=\"mailto:mr.luke.187@googlemail.com\">Us</a>" +
            "</ul>" +
            "<p>If you decide to do so, please add the Log-file from [dir] to your report!</p>" +
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
     * @param exception
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
        // TODO Handle Hyperlinks in JLabel with instructions
        Object[] message_elements = new Object[]{
                new JLabel(message),
                new JLabel(report_instructions)
        };
        pane = new JOptionPane(message_elements,
                JOptionPane.ERROR_MESSAGE,
                JOptionPane.CLOSED_OPTION);
    }

    /**
     * Show dialog.
     */
    public void show(){
        pane.createDialog(null, title).setVisible(true);
    }

}
