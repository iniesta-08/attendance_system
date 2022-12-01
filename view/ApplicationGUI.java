package view;

import model.Blackboard;
import model.ParseAttendance;
import model.ParseRoster;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.GridLayout;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JFileChooser;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenuItem;
import java.awt.Container;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.io.File;
import java.io.FileWriter;
import java.util.Vector;
import java.util.Map;

/**
 * This class generates the principal frame window for the app.
 * Objects for relevant classes have been used to handle 
 * various menu operations in an efficient manner.
 * 
 *
 * @author Shrinkhala Kayastha
 * @author Mukul Mahadik
 * @version 1.0
 */

public class ApplicationGUI extends JFrame implements ActionListener {

    StatusLogger statusBar;
    MenuPanel menu;
    DisplayPanel rightPanel;
    Blackboard blackboard;
    TablePane tableData;
    DefaultTableModel model;
    JMenuItem[] items;

    public ApplicationGUI(String title) {
        super(title);
        setResizable(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setSize((int) screenSize.getWidth() - ((int) (0.1 * screenSize.getWidth())), (int) screenSize.getHeight() - ((int) (0.1 * screenSize.getHeight())));
        int x = (int) ((screenSize.getWidth() - this.getWidth()) / 2);
        int y = (int) ((screenSize.getHeight() - this.getHeight()) / 2);
        this.setLocation(x, y);
        this.getContentPane().setBackground(Color.WHITE);
        setVisible(true);

        initialize();
    }

    /**
     * Initializes JFrame menu, panel components.
     */
    private void initialize() {

        blackboard = Blackboard.getInstance();
        tableData = TablePane.getInstance();
        rightPanel = new DisplayPanel();
        blackboard.addObserver(rightPanel);
        statusBar = StatusLogger.getInstance();
        menu = new MenuPanel();
        setJMenuBar(menu.getMenuBar());
        addComponentsToPane(this.getContentPane(), rightPanel);
        items = menu.getMenuOptions();
        for (JMenuItem item : items)
            item.addActionListener(this);
        
        if (blackboard.getStudents().isEmpty())
            items[1].setEnabled(false);
    }

    
    /** 
     * Handles action events triggered as per the menu options
     * @param e Stores current action event
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        String choice = e.getActionCommand();
        statusBar.setMessage(choice);
        
        if (e.getSource() == items[0])
            handleLoadRoster();
        else if (e.getSource() == items[1])
            handleAddAttendance();
        else if (e.getSource() == items[2])
            handleSaveData();
        else if (e.getSource() == items[3])
            handlePlotData();
        else if (e.getSource() == items[4])
            handleTeamInfo();
    }


    
    /** 
     * Adds the various GUI components to the JFrame
     * @param pane Stores current pane that contains GUI components
     * @param rightPanel Refers to the display panel showing all loaded data
     */
    public void addComponentsToPane(Container pane, DisplayPanel rightPanel) {
 
        pane.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.5;
        c.gridx = 0;
        c.gridy = 0;
        c.ipady = 700;
        pane.add(rightPanel, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipady = 20;
        c.anchor = GridBagConstraints.PAGE_END;
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 4;
        pane.add(statusBar, c);
    }

    /**
     * Handles the menu action event for displaying team info
     */
    public void handleTeamInfo() {
        JPanel dPane = new JPanel();
        dPane.setLayout(new GridLayout(0,1));
        JDialog d = new JDialog(this, "Project Team");

        for (String person : menu.getTeamInfo()) {
            JLabel label = new JLabel(person);
            dPane.add(label);
        }
        d.add(dPane);
        d.setSize(500, 500);
        d.setVisible(true);
    }

    /**
     * Handles the menu action event for plotting attendance data
     */
    public void handlePlotData() {
        blackboard.setAction("Plot");
        blackboard.fileParsed();
    }

    /**
     * Handles the menu action event for loading and displaying student roster data
     */
    public void handleLoadRoster() {
        blackboard.setAction("Load");

        JFileChooser fc = new JFileChooser();
        fc.setMultiSelectionEnabled(false);
        int r = fc.showOpenDialog(null);
        if (r == JFileChooser.APPROVE_OPTION) {

            blackboard.clearRoster();
            model = tableData.getTableModel();
            model.setRowCount(0);
            model.setColumnCount(4);    

            File rosterFile = fc.getSelectedFile();
            statusBar.setMessage("Roster File loaded");
            ParseRoster parser = new ParseRoster();
            parser.parseRosterFile(rosterFile);
            if (!blackboard.getStudents().isEmpty())
                items[1].setEnabled(true);
        }
        else {
            statusBar.setMessage("User cancelled the operation");
            if (!blackboard.getStudents().isEmpty())
                items[1].setEnabled(true);
            else if (blackboard.getStudents().isEmpty())
                items[1].setEnabled(false);
        }
    }

    /**
     * Handles the menu action event for loading and displaying student attendance data
     */
    public void handleAddAttendance() {

        blackboard.setAction("Add");

        JFileChooser fc = new JFileChooser();
        fc.setMultiSelectionEnabled(true);
        int r = fc.showOpenDialog(null);
        if (r == JFileChooser.APPROVE_OPTION) {
            File[] attendanceFiles = fc.getSelectedFiles();
            statusBar.setMessage("Attendance Files loaded");
            ParseAttendance parser = new ParseAttendance();
            parser.parseAttendance(attendanceFiles);


            JPanel dPane = new JPanel();
            dPane.setLayout(new GridLayout(0,1));
            JDialog d = new JDialog(this, "Attendance Loaded");

            Map<String, Integer> extraAttendees = blackboard.getExtras();

            JLabel label1 = new JLabel(extraAttendees.size() + " additional attendee(s) found: ");
            dPane.add(label1);
    
            for (String asurite : extraAttendees.keySet()) {
                JLabel label2 = new JLabel(asurite + " connected for " + blackboard.getExtras().get(asurite) + " minutes");
                dPane.add(label2);
            }
            d.add(dPane);
            d.setSize(500, 500);
            d.setVisible(true);
        }
        else {
            statusBar.setMessage("User cancelled the operation");
        }
    }

    /**
     * Handles the menu action event for saving displayed student data
     */
    public void handleSaveData() {

        JFileChooser fc = new JFileChooser();
        fc.setMultiSelectionEnabled(false);
        int r = fc.showSaveDialog(null);
        if (r == JFileChooser.APPROVE_OPTION) {

            File rosterFile = fc.getSelectedFile();
            rosterFile = new File(fc.getSelectedFile() + ".csv");
            statusBar.setMessage("Table data saved successfully");

            String DELIMITER = ",";
            String SEPARATOR = "\n";
            String HEADER = "";

            model = tableData.getTableModel();

            int numberRows = model.getRowCount();
            int numberColumns = model.getColumnCount();
            String[] headers = new String[numberColumns];

            for (int i = 0; i < numberColumns; i++)
                headers[i] =  model.getColumnName(i);

            HEADER = String.join(DELIMITER + " ", headers);
            
            
            try
            {
                FileWriter saveFileWrite = new FileWriter(rosterFile);
                saveFileWrite.append(HEADER);
                saveFileWrite.append(SEPARATOR);

                Vector allData = model.getDataVector();
                for (int i = 0; i < numberRows; i++) {
                    Vector<String> row = (Vector<String>)allData.elementAt(i);
                    String rowData = String.join(DELIMITER + " ", row);
                    saveFileWrite.append(rowData);

                    saveFileWrite.append(SEPARATOR);
                }
                saveFileWrite.close();

            } catch(Exception e) {
                e.printStackTrace();
            }
        }
        else
            statusBar.setMessage("User cancelled the operation");
    }
}

