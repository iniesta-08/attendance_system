package model;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Objects;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Vector;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * This class represents a attendance list maintained by a professor for a class.
 * It maintains datewise attendance data for all different dates as per the attendance files uploaded.
 * It contains methods to handle this attendance data.
 * 
 *
 * @author Shrinkhala Kayastha
 * @author Mukul Mahadik
 * @version 1.0
 */

public class Attendance {

    private final Date date;
    private Map<String, Integer> attendMap;
    private Map<String, int[]> attendanceMap;
    private SortedSet<String> sortedAttendance;
    private Blackboard blackboard;

    public Attendance(Date date) {
        this.date = date;
        this.attendMap =  new ConcurrentHashMap<>();
        this.attendanceMap = new ConcurrentHashMap<String, int[]>();
        this.blackboard = Blackboard.getInstance();
    }

    
    /** 
     * Returns attendance Date
     */
    public Date getDate() {
        return date;
    }

    
    /** 
     * Returns formatted attendance Date
     */
    public String getFormattedDate() {
        SimpleDateFormat dt1 = new SimpleDateFormat("MM/dd/yy");
        return dt1.format(date);
    }

    
    /** 
     * Returns attendance data
     */
    public Map<String, Integer> getAttendance() {
        return attendMap;
    }

    
    /** 
     * Sets attendance data
     * @param attendMap Attendance data
     */
    public void setAttendance(Map<String, Integer> attendMap) {
        this.attendMap = attendMap;
    }

    
    /** 
     * Checked whether student already exists in attendance data
     * @param asurite Asurite to be checked
     */
    public boolean hasStudentTime(String asurite) {
        return attendMap.containsKey(asurite);
    }

    
    /** 
     * Adds student attendance data
     * @param asurite Asurite to be added
     * @param minutes Minutes data for each student
     */
    public void addStudentTime(String asurite, int minutes) {
        attendMap.put(asurite, minutes);
    }

    
    /** 
     * Merges student attendance data
     * @param asurite Asurite to be merged
     * @param minutes Minutes data for each student
     */
    public void mergeStudentTime(String asurite, int minutes) {
        int totalMinutes = attendMap.get(asurite) + minutes;
        attendMap.replace(asurite, totalMinutes);
    }

    
    /** 
     * Returns total attendance count
     */
    public int getAttendanceCount() {
        return attendMap.size();
    }

    
    /** 
     * Returns attendance minutes
     */
    public Vector<String> getMinutes() {
        Vector<String> minutes = new Vector<String>();

        for (Integer minute : attendMap.values())
            minutes.add(minute.toString());
        return minutes;
    }

    /** 
     * Prints attendance data
     */
    public void printAttendance() {
        for (String asurite : attendMap.keySet())
            System.out.println("ASURITE: " + asurite + "\t" + "Minutes: " + attendMap.get(asurite));
    }

    
    /** 
     * Returns attendance data
     */
    public Map<String, int[]> getAttendance2() {
        return attendanceMap;
    }

    
     /** 
     * Sets attendance data
     * @param attendanceMap Attendance data
     */
    public void setAttendance2(Map<String, int[]> attendanceMap) {
        this.attendanceMap = attendanceMap;
    }

    
    /** 
     * Checked whether student already exists in attendance data
     * @param asurite Asurite to be checked
     */
    public boolean hasStudentTime2(String asurite) {
        return attendanceMap.containsKey(asurite);
    }

    /** 
     * Adds student attendance data
     * @param index Student index as per roster data
     * @param asurite Asurite to be added
     * @param minutes Minutes data for each student
     */
    public void addStudentTime(int index, String asurite, int minutes) {
        int[] studInfo = new int[2];
        studInfo[0] = index;
        studInfo[1] = minutes;
        attendanceMap.put(asurite,studInfo);
    }

    
    /** 
     * Merges student attendance data
     * @param index Student index as per roster data
     * @param asurite Asurite to be merged
     * @param minutes Minutes data for each student
     */
    public void mergeStudentTime(int index, String asurite, int minutes) {
        int totalMinutes = attendanceMap.get(asurite)[1] + minutes;

        int[] studInfo = new int[2];
        studInfo[0] = index;
        studInfo[1] = totalMinutes;
        attendanceMap.replace(asurite, studInfo);
    }

    
    /** 
     * Returns total attendance count
     */
    public int getOrderedAttendanceCount() {
        return sortedAttendance.size();
    }

    /** 
     * Prints attendance data
     */
    public void printAttendance2() {
        for (String asurite : attendanceMap.keySet())
            System.out.println("ASURITE: " + asurite + "\tIndex: " + attendanceMap.get(asurite)[0] + "\t" + "Minutes: " + attendanceMap.get(asurite)[1]);

        sortedAttendance.forEach(key -> System.out.println(
            "Key : " + key  + "\t\t"  + 
                    "Value : " + attendanceMap.get(key)[1]
            ));        
    }

    
    /** 
     * Returns sorted attendance data as per roster order
     */
    public Vector<String> getOrderedAttendance() {

        Vector<String> minutesOld = new Vector<String>();
        Vector<String> minutes = new Vector<String>();

        sortedAttendance = new TreeSet<>(new Comparator<String>() {
 
            @Override
            public int compare(String str1, String str2) {
                int comp = attendanceMap.get(str1)[0] - attendanceMap.get(str2)[0];
                return comp == 0 ? 1 : comp;
            }
        });

        sortedAttendance.addAll(attendanceMap.keySet());
        sortedAttendance.forEach(key -> minutesOld.add(Integer.toString(attendanceMap.get(key)[1])));

        minutes = updateMinutes(minutesOld);
        return minutes;
    }


    
    /** 
     * Returns attendance updated minutes
     * @param minutesOld
     */
    public Vector<String> updateMinutes(Vector<String> minutesOld) {
        int i = 0;
        Vector<String> minutes = minutesOld;

        List<String> keys = new ArrayList<String>();
        for (String key : sortedAttendance)
            keys.add(key);

        for (Student stud : blackboard.getStudents()) {
            String str = stud.getAsurite();
            if (!keys.contains(str)) {
                minutes.add(i, null);
            }
            i++;
        }
        return minutes;
    }



    
    /** 
     * Returns objects hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(date);
    }

    
    /** 
     * Checks whether object types match
     * @param o Object to be checked
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Attendance attend = (Attendance) o;
        return date.equals(attend.date);
    }
}
