package sample;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;


public class Stroke {
    public double xCoordinate;
    public double yCoordinate;
    public int strokeSize;

    public Stroke() {
    }

    public Stroke(double xCoordinate, double yCoordinate, int strokeSize) {
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.strokeSize = strokeSize;
    }


    @Override
    public String toString() {
        return "Stroke info: " +
                "xCoordinate=" + xCoordinate + ", " +
                "yCoordinate=" + yCoordinate + ", " +
                "strokeSize=" + strokeSize;
    }

    // getters and setters
    public double getxCoordinate() {
        return xCoordinate;
    }

    public void setxCoordinate(double xCoordinate) {
        this.xCoordinate = xCoordinate;
    }

    public double getyCoordinate() {
        return yCoordinate;
    }

    public void setyCoordinate(double yCoordinate) {
        this.yCoordinate = yCoordinate;
    }

    public int getStrokeSize() {
        return strokeSize;
    }

    public void setStrokeSize(int strokeSize) {
        this.strokeSize = strokeSize;
    }

}
