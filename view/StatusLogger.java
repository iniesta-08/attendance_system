package view;

import javax.swing.JLabel;
import javax.swing.*;
import java.awt.*;

/**
 * This class represents a model of the event logger for 
 * all the major actions occurred in the application.
 * It generates a JPanel and updates a nested JLabel to indicate 
 * the latest action triggered by the user or the system.
 * 
 *
 * @author Shrinkhala Kayastha
 * @author Mukul Mahadik
 * @version 1.0
 */
public class StatusLogger extends JPanel {

    private static volatile StatusLogger INSTANCE;

    private JLabel statusLabel;

    private StatusLogger() {
        setLayout(new BorderLayout(1,1));

        statusLabel = new JLabel("Ready");
        statusLabel.setBorder(BorderFactory.createLoweredBevelBorder());
        statusLabel.setForeground(Color.BLACK);
        add(BorderLayout.CENTER, statusLabel);
    }

    
    /** 
     * Returns instantiated object of this class
     */
    public static StatusLogger getInstance() {
        if (INSTANCE == null) {
            synchronized (StatusLogger.class) {
                if (INSTANCE == null) {
                    INSTANCE = new StatusLogger();
                }
            }
        }
        return INSTANCE;
    }

    
    /** 
     * Sets the status message to be displayed
     * @param message Status message to be updated
     */
    public void setMessage(String message) {
        if (message.equals(""))
            statusLabel.setText("Ready");
        else
            statusLabel.setText(message);
    }
}
