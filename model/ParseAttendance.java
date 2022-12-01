package model;

import java.io.File;
import java.util.Scanner;

import view.TablePane;

import java.io.FileNotFoundException;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Map;
import java.util.HashMap;

/**
 * This class contains methods to handle input files pertaining to 
 * student attendance data.
 * Attendance data comprises the following columns for each row of student data
 * 1. ASURITE - indicates unique alphabetic student ID
 * 2. Minutes - indicates time in minutes for which student attended lecture
 * 
 * @author Shrinkhala Kayastha
 * @version 1.0
 */

public class ParseAttendance {

    private Scanner myReader;

    private Blackboard blackboard;
    private TablePane tableData;

    private Map<String, Integer> extraAttendees;

    public ParseAttendance() {
        this.blackboard = Blackboard.getInstance();
        this.tableData = TablePane.getInstance();
        this.extraAttendees = new HashMap<String, Integer>();
    }

    
    /** 
     * Parses the user inputted student attendance files
     * and extracts dates from filename
     * @param file Files to be parsed
     */
    public void parseAttendance(File[] files) {


        String dateStr = "", dateTable = "";
        SimpleDateFormat dt1;
        Date date;

        for (File file : files) {
            try {
                dateStr = file.getName().substring(0, 8);
                dt1 = new SimpleDateFormat("yyyyMMdd");
                date = dt1.parse(dateStr);
                dateTable = blackboard.getFormattedDate(date);
                if (!tableData.hasColumn(dateTable))
                    parseAttendanceFile(file, date);
            } catch(ParseException e) {
                e.printStackTrace();
            }
        }
        blackboard.fileParsed();
    }

    
     /** 
     * Parses the user inputted dated attendance file 
     * and stores data into blackboard
     * @param file File to be parsed
     */
    public void parseAttendanceFile(File file, Date date) {
        String data, asurite;
        String[] tokens;
        int minutes, studIDFlag;

        Attendance attend = new Attendance(date);

        try {
            myReader = new Scanner(file);
			while (myReader.hasNextLine()) {
				data = myReader.nextLine();
				tokens = data.split(",");
                asurite = tokens[0].trim();
                minutes = Integer.parseInt(tokens[1].trim());
                studIDFlag = blackboard.hasAsurite(asurite);
                if (studIDFlag != -1) {
                    if (!attend.hasStudentTime(asurite)) {
                        attend.addStudentTime(asurite, minutes);
                    }
                    else {
                        attend.mergeStudentTime(asurite, minutes);
                    }
                    if (!attend.hasStudentTime2(asurite)) {
                        attend.addStudentTime(studIDFlag, asurite, minutes);
                    }
                    else {
                        attend.mergeStudentTime(studIDFlag, asurite, minutes);
                    }
                }
                else
                    extraAttendees.put(asurite, minutes);
            }
            blackboard.addAttendance(attend);
            blackboard.setExtras(extraAttendees);
            myReader.close();
		} catch (FileNotFoundException e) {
			System.out.println("Error occurred in finding attendance file(s): " + e);
			e.printStackTrace();
        }
	}
}
