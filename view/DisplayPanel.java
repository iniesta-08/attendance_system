package view;

import model.Attendance;
import model.Blackboard;
import model.Student;
import model.Coordinate;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.Color;
import java.awt.Dimension;
import java.util.Observer;
import java.util.Observable;
import java.util.List;
import java.util.LinkedHashMap;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;


/**
 * This class deals with the panel that displays the required GUI components 
 * including the table for the data, plots for the data.
 * GUI Panel has majorly two components which will be displayed 
 * based on their corresponding action chosen:
 * 1. Roster and attendance data in tabular format.
 * 2. Plots containing datewise attendance data.
 *
 * @author Shrinkhala Kayastha
 * @author Mukul Mahadik
 * @version 1.0
 * */
public class DisplayPanel extends JPanel implements Observer {

    private static final int PREF_W = 700;
    private static final int PREF_H = 250;

    private final int yLowData = 0;
    private final int yHighData = 400;
    private final int yLowPixel = 300;
    private final int yHighPixel = 700;

    private int[] yValPix;

    private LinkedHashMap<String, Coordinate> coordinates;
    private LinkedHashMap<String, Integer> attendCount;

    private TablePane tableData;

    public DisplayPanel() {
        this.setBackground(Color.white);
        this.setBorder(new LineBorder(Color.DARK_GRAY, 1, true));

        this.tableData = TablePane.getInstance();
        this.yValPix = new int[(yHighData-yLowData)/50 + 1];

        for (int i = 0; i < yValPix.length; i++) {
            yValPix[i] = 300 + 50*i;
        }

        this.attendCount = new LinkedHashMap<String, Integer>();
    }

    
    /** 
     * This function sets the size for the GUI component
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(PREF_W, PREF_H);
    }


    
    /**
     * This function updates the GUI panel with corresponding tables 
     * and plots based on the chosen menu action
     * and plots the points and average in the GUI
     * @param o object of Observable class
     * @param arg additional object iwth update function
     */
    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof Blackboard) {
            List<Student> studs = ((Blackboard) o).getStudents();
            List<Attendance> attendances = ((Blackboard) o).getAttendances();

            DefaultTableModel model = tableData.getTableModel();

            String action = ((Blackboard) o).getAction();

            if (!action.equals("Plot")) {

                if (action.equals("Load")) {
                    studs.forEach(stud -> {
                    model.addRow(new Object[]{stud.getId(), stud.getFirstName(), stud.getLastName(), stud.getAsurite()});
                    });
                }

                if (action.equals("Add")) {
                    attendances.forEach(attend -> {
                        String colDate = attend.getFormattedDate();
                        if (!tableData.hasColumn(colDate))
                            model.addColumn(attend.getFormattedDate(), attend.getOrderedAttendance());
                    });
                }

                tableData.setJTableColumnsWidth();

                tableData.getJSP().setPreferredSize(this.getSize());
                this.add(tableData.getJSP());
            }

            if (action.equals("Plot")) {
                this.remove(tableData.getJSP());
                attendCount = ((Blackboard)o).getAttendanceCount();
                coordinates = generateCoordinates(attendCount);

                repaint();

            }
        }
    }

    
    /** 
     * Generates scaled coordinates for the datewise attendance to be plotted
     * @param attendCount Contains the datewise attendance data 
     */
    public LinkedHashMap<String, Coordinate> generateCoordinates(LinkedHashMap<String, Integer> attendCount) {

        final int numberDates = 30;
        
        int xScale[] =  new int[numberDates];
        for(int i = 0; i < numberDates; i++)
            xScale[i] = 150 + 50*(i);
        
        LinkedHashMap<String, Coordinate> coordinates = new LinkedHashMap<String, Coordinate>();

        int yVal;
        double y;
        int i = 0;

        for (String date : attendCount.keySet()) {

            yVal = attendCount.get(date);
            y = normalizeYValues(yVal);

            Coordinate xy = new Coordinate();
            xy.setX(xScale[i++]);
            xy.setY(y);
            
            coordinates.put(date, xy);

        }
        return coordinates;
    }


    
    /** 
     * Normalized attendance minutes data to fit them on the screen
     * @param yVal Non-normalized attendance minutes 
     * @return double
     */
    public double normalizeYValues(int yVal) {
        double yMid, yNorm;

        yMid = (double)(yVal - yLowData) / (yHighData - yLowData);
        yNorm = (yMid * (yHighPixel - yLowPixel)) + yLowPixel;

        return yNorm;
    }


    
    /**
     * This function plots the generated coordinates 
     * for the attendance data in the GUI.
     * @param g object for class Graphics
     */
    @Override
    public void paintComponent(Graphics g) {
        Stroke GRAPH_BAR_STROKE = new BasicStroke(6f);
        int GRAPH_POINT_WIDTH = 8;
        boolean flag = false;
        if (coordinates != null) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setColor(Color.GRAY);
            g2.setStroke(GRAPH_BAR_STROKE);
            AffineTransform defaultAt = g2.getTransform();

            g2.scale(1, -1);
            g2.translate(0, -getHeight());

            for (String date : coordinates.keySet()) {
                int x1 = (int)coordinates.get(date).getX();
                int y1 = (int)coordinates.get(date).getY();

                g2.drawLine(x1, yLowPixel, x1, y1);
            }

            g2.setTransform(defaultAt);

            for (int y1 : yValPix) {
                int yLabel = y1 - yLowPixel;
                g2.drawString(Integer.toString(yLabel), 100, getHeight() - y1 + 5);
            }

            for (String date : coordinates.keySet()) {
                int x1 = (int)coordinates.get(date).getX();
                int y1 = (int)coordinates.get(date).getY();
                
                String numStuds = Integer.toString(attendCount.get(date));

                g2.drawString(numStuds, x1-5, getHeight() - y1 - 5);

                if (!flag) {
                    y1 = 700;
                    flag = true;
                }
                else {
                    y1 = 720;
                    flag = false;
                }
                g2.drawString(date, x1 - 20, y1);
            }

            g2.drawString("Dates", 900, 740);
            g2.setTransform(defaultAt);
            AffineTransform atLabels = AffineTransform.getQuadrantRotateInstance(3);
            g2.setTransform(atLabels);
            g2.drawString("No. of Students", -540, 30);
            g2.setTransform(defaultAt);
        }
    }
}
