//package sample;
//
//import javafx.scene.canvas.GraphicsContext;
//
//import java.awt.*;
//
//public class RunnableGC implements Runnable {
//
//    private GraphicsContext gc = null;
//    private Stroke stroke = null;
//
//    public RunnableGC(GraphicsContext gc, Stroke stroke) {
//        this.gc = gc;
//        this.stroke = stroke;
//    }
//
//    public void run() {
//        gc.strokeOval(stroke.getxCoordinate(), stroke.getyCord(), 10, 10);
//    }
//}