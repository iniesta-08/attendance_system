package model;
/**
 * This class models a Coordinate class based on inbuilt Point class.
 * It stores X and Y coordinate values of Double type.
 * 
 * @author Mukul Mahadik
 * @version 1.0
 */
public class Coordinate {
    private double x;
    private double y;

    public Coordinate() {
        this.x = 0.00;
        this.y = 0.00;
    }

    public Coordinate(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /** 
     * Returns X coordinate of point
     */
    public double getX() {
        return x;
    }

    /** 
     * Sets X coordinate of point
     */
    public void setX(double x) {
        this.x = x;
    }

    /** 
     * Sets Y coordinate of point
     */
    public void setY(double y) { 
        this.y = y; 
    }

    /** 
     * Returns Y coordinate of point
     */
    public double getY() {
        return y;
    }
}