package sample;

/**
 * Created by Yehia830 on 9/6/16.
 */
public class Stroke {
    private double xCord;
    private double yCord;
    private int strokeSize;

    public Stroke(){

    }

    public Stroke(double xCord, double yCord, int strokeSize) {
        this.xCord = xCord;
        this.yCord = yCord;
        this.strokeSize = strokeSize;
    }

    @Override
    public String toString() {
        return "Stroke : " +
                "xCord=" + xCord +
                ", yCord=" + yCord +
                ", strokeSize=" + strokeSize +
                '}';
    }

    public double getxCord() {
        return xCord;
    }

    public void setxCord(double xCord) {
        this.xCord = xCord;
    }

    public double getyCord() {
        return yCord;
    }

    public void setyCord(double yCord) {
        this.yCord = yCord;
    }

    public int getStrokeSize() {
        return strokeSize;
    }

    public void setStrokeSize(int strokeSize) {
        this.strokeSize = strokeSize;
    }
}
