package model;

import java.io.File;
import java.util.Scanner;
import java.io.FileNotFoundException;


/**
 * This class contains methods to handle input files pertaining to 
 * student roster data.
 * Roster data comprises the following columns for each row of student data
 * 1. ID - indicates unique numeric student ID
 * 2. First Name  - indicates student first name
 * 3. Last Name - indicates student last name
 * 4. ASURITE - indicates unique alphabetic student ID
 * 
 * @author Shrinkhala Kayastha
 * @version 1.0
 */
public class ParseRoster {

    private Scanner myReader;

    private Blackboard blackboard;
    
    public ParseRoster() {
        this.blackboard = Blackboard.getInstance();
    }

    
    /** 
     * Parses the user inputted student roster file 
     * and stores data into blackboard
     * @param file File to be parsed
     */
    public void parseRosterFile(File file) {
        String data;
        String[] tokens;
        
		try{
            myReader = new Scanner(file);
			while (myReader.hasNextLine()) {
				data = myReader.nextLine();
				tokens = data.split(",");
                Student newStud = new Student(tokens);
                if (!blackboard.hasStudent(newStud))
                    blackboard.addStudent(new Student(tokens));
			}
            myReader.close();
            blackboard.fileParsed();
		} catch (FileNotFoundException e) {
			System.out.println("Error occurred in finding roster file: " + e);
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("Error occurred in parsing roster data: " + e);
			e.printStackTrace();
		}
	}
}
