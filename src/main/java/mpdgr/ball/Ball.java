package mpdgr.ball;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Ball {
    //x, y stand for center of the ball
    public GraphicsContext drawBall(GraphicsContext gc, Position position){
        double x = position.getX();
        double y = position.getY();
       float diameter = 20;
        gc.strokeOval(x - diameter / 2, y - diameter / 2,diameter, diameter);
        gc.setFill(Color.ORANGE);
        gc.fillOval(x - diameter / 2, y - diameter / 2,diameter, diameter);
        return gc;
    }
}