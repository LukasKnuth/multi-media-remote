package org.knuth.multimediaremote.server.view.elements;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Level;
import org.apache.log4j.spi.LoggingEvent;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

/**
 * @author Lukas Knuth
 * @version 1.0
 * A Log-window for the Swing Window-Toolkit used to show the log
 *  on the GUI.
 */
public class Log extends AppenderSkeleton{

    /** The {@code JTextPane} which this logger logs on. */
    private static final JTextPane log;
    /** The {@code StyledDocument} to append the logs to */
    private static final StyledDocument doc;

    /** Used for the info-markup */
    private static final SimpleAttributeSet info_log;
    /** Used for the error-markup */
    private static final SimpleAttributeSet error_log;

    /** ScrollPane used to scroll the log */
    private JScrollPane scrollPane;

    /**
     * Initialize the components so Log4J and Swing use
     *  the same.
     * @see http://stackoverflow.com/questions/8546995
     */
    static {
        log = new JTextPane();
        doc = log.getStyledDocument();
        // Create the colors for the log:
        info_log = new SimpleAttributeSet();
        StyleConstants.setForeground(info_log, Color.green);
        error_log = new SimpleAttributeSet();
        StyleConstants.setForeground(error_log, Color.red);
        StyleConstants.setBold(error_log, true);
    }

    /**
     * A Swing-component which is used to log on with Log4J.
     */
    public Log(){
        super();
    }

    /**
     * Append a new {@code LoggingEvent} to this Logger.
     * @param loggingEvent the event to log.
     */
    @Override
    protected void append(LoggingEvent loggingEvent) {
        String message = ((String) loggingEvent.getMessage())+"\n";
        if (loggingEvent.getLevel() == Level.INFO){
            // Append a new Info to the Log:
            try {
                doc.insertString(doc.getLength(), message, info_log);
                Rectangle rectangle = log.getVisibleRect();
                rectangle.height += 10;
                log.scrollRectToVisible(rectangle);
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
        } else if (loggingEvent.getLevel() == Level.ERROR){
            // Append a new Error-Log:
            try {
                doc.insertString(doc.getLength(), message, error_log);
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Does the basic setup for the used {@code JTextPane}.
     */
    private void doBasicSetup(){
        // Create the SimpleAttributeSet styles
        log.setEditable(false);
        // Setup the ScrollPane:
        scrollPane = new JScrollPane(log,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        // Enable auto-scroll:
        // TODO Allow manual scroll up.
        scrollPane.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
            @Override
            public void adjustmentValueChanged(AdjustmentEvent e) {
                e.getAdjustable().setValue(e.getAdjustable().getMaximum());
            }
        });
        // No border:
        scrollPane.setBorder(null);
        // Get some space:
        Dimension dimension = log.getPreferredSize();
        dimension.height += 40;
        scrollPane.setPreferredSize(dimension);
    }

    /**
     * Returns the {@code JTextPane} which is used to log
     *  on.
     * @return The {@code JTextPane}-instance.
     */
    public JComponent getView(){
        // Check if already set up:
        if (log.isEditable()) doBasicSetup();
        return scrollPane;
    }

    /**
     * Closes this logger.
     */
    public void close() {
        // Nothing to do here...
    }

    /**
     * Determines if this logger must have a given Layout.
     * @return always {@code false} in this implementation.
     */
    public boolean requiresLayout() {
        return false;
    }
}
