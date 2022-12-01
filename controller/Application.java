package controller;

import view.ApplicationGUI;

import javax.swing.SwingUtilities;


/**
 * This class serves as the starting point for the programming logic flow
 * for the Attendance App software developed.
 * It instantiates an object of the ApplicationGUI class 
 * which generates the principal frame window for the app.
 * 
 * The following Software Design Patterns have been used with their respective classes mentioned:
 * 1. Observer - Blackboard, DisplayPanel
 * 2. Singleton - Blackboard
 *
 * @author Shrinkhala Kayastha
 * @author Mukul Mahadik
 * @version 1.0
 */
public class Application {
    
    /** 
     * Starting point of Attendance app software programming logic flow.
     * @param args Stores input data file path passed as command line argument
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ApplicationGUI("Assignment 4");
        });
    }
}
