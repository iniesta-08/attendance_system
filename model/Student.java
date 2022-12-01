package model;

import java.util.Objects;


/**
 * This class represents a model of a real-world student.
 * It defines the basic requisite information for any student 
 * and provides a user-defined datatype to represent the student object.
 * 
 *
 * @author Shrinkhala Kayastha
 * @author Mukul Mahadik
 * @version 1.0
 */
public class Student {
    
    private final String id;
    private final String firstName;
    private final String lastName;
    private final String asurite;

    public Student(String[] data) {
        this.id = data[0];
        this.firstName = data[1];
        this.lastName = data[2];
        this.asurite = data[3];
    }

    
    /** 
     * Returns student ID
     */
    public String getId() {
        return id;
    }

    
    /** 
     * Returns student first name
     */
    public String getFirstName() {
        return firstName;
    }

    
    /** 
     * Returns student last name
     */
    public String getLastName() {
        return lastName;
    }

    
    /** 
     * Returns student ASURITE
     */
    public String getAsurite() {
        return asurite;
    }
    
    
    /** 
     * Returns objects hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    
    /** 
     * Checks whether object types match
     * @param o Object to be checked
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student stud = (Student) o;
        return id.equals(stud.id);
    }
}
