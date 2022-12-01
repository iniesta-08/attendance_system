package model;

import java.util.List;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.HashMap;
import java.util.LinkedHashMap;


/**
 * This is a Singleton class that is the principal data source 
 * that is commonly available across the entire application.
 * 
 * It implements the Singleton design pattern by ensuring that 
 * an object of this class is created only after verifying 
 * whether an object has already been instantiated or not.
 * 
 * Additionally, it is an extension of the built-in Observable class and implements the Observer design pattern.
 * It is used to achieve the functionality of notifying the Observer class DisplayPanel 
 * as soon as it updates its data.
 * 
 * @author Mukul Mahadik
 * @version 1.0
 */

public class Blackboard extends Observable {
    private static volatile Blackboard INSTANCE;

    private final List<Student> studentRoster;
    private final List<Attendance> attendances;
    private Map<String, Integer> extraAttendees;

    private String action = "";

    private Blackboard() {
        this.studentRoster = new ArrayList<Student>();
        this.attendances = new ArrayList<Attendance>();
        this.extraAttendees = new HashMap<String, Integer>();
    }

    
    /** 
     * Returns instantiated object of this class
     */
    public static Blackboard getInstance() {
        if (INSTANCE == null) {
            synchronized (Blackboard.class) {
                if (INSTANCE == null) {
                    INSTANCE = new Blackboard();
                }
            }
        }
        return INSTANCE;
    }

    
    /** 
     * Adds new student to the blackboard
     * @param stud Student to be added
     */
    public void addStudent(Student stud) {
        studentRoster.add(stud);
    }

    
    /** 
     * Adds new dated attendance to the blackboard
     * @param attend
     */
    public void addAttendance(Attendance attend) {
        attendances.add(attend);
    }

    
    /**
     * Calls observer methods by notifying them when file parsing is completed
     */
    public void fileParsed() {
        this.setChanged();
        this.notifyObservers();
    }

    
    /** 
     * Checked whether a student exists in the blackboard
     * @param stud Student to be checked
     */
    public boolean hasStudent(Student stud) {
        return studentRoster.contains(stud);
    }

    
    /** 
     * Returns total number of students in class
     */
    public int getStudentCount() {
        return studentRoster.size();
    }

    
    /** 
     * Returns list of students in class
     */
    public List<Student> getStudents() {
        return studentRoster;
    }

    
    /** 
     * Returns all dates attendance data for the class
     */
    public List<Attendance> getAttendances() {
        return attendances;
    }

    /**
     * Clears the student roster data
     */
    public void clearRoster() {
        studentRoster.removeAll(studentRoster);
    }

    
    /** 
     * Returns the menu action chosen
     */
    public String getAction() {
        return action;
    }

    
    /** 
     * Sets the menu action chosen
     * @param action Action to be set
     */
    public void setAction(String action) {
        this.action = action;
    }

    
    /** 
     * Formats fetched attendance date into appropriate format
     * @param date Date fetched from attendance file
     */
    public String getFormattedDate(Date date) {
        SimpleDateFormat dt1 = new SimpleDateFormat("MM/dd/yy");
        return dt1.format(date);
    }

    
    /** 
     * Checks whether a student exists in blackboard
     * @param asurite Asurite id to be checked
     */
    public int hasAsurite(String asurite) {
        int studIDFlag = -1;
        for (Student stud : studentRoster) {
            if (asurite.equals(stud.getAsurite())) {
                studIDFlag = studentRoster.indexOf(stud);
                return studIDFlag;
            }
        }
        return studIDFlag;
    }

    
    /** 
     * Sets the extra non-rostered students
     * @param extraAttendees Extra students list
     */
    public void setExtras(Map<String, Integer> extraAttendees) {
        this.extraAttendees = extraAttendees;
    }

    
    /** 
     * Returns the extra non-rostered students
     */
    public Map<String, Integer> getExtras() {
        return extraAttendees;
    }

    
    /** 
     * Returns the list of datewise attendance with total student count for each date
     */
    public LinkedHashMap<String, Integer> getAttendanceCount() {
        LinkedHashMap<String, Integer> attendCount = new LinkedHashMap<String, Integer>();

        for (Attendance attend : attendances) {
            attendCount.put(attend.getFormattedDate(), attend.getOrderedAttendanceCount());
        }

        return attendCount;
    }
}
