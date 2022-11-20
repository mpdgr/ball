package mpdgr.ball;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class BallView {
    private final float diameter;

    public BallView(float diameter) {
        this.diameter = diameter;
    }

    public BallView() {
         this.diameter = 20;
    }

    GraphicsContext drawBall(GraphicsContext gc, Position position) {
        double x = position.getX(); //x, y stand for center of the ball
        double y = position.getY();
        gc.strokeOval(x - diameter / 2, y - diameter / 2, diameter, diameter);
        gc.setFill(Color.ORANGE);
        gc.fillOval(x - diameter / 2, y - diameter / 2, diameter, diameter);
        return gc;
    }
}