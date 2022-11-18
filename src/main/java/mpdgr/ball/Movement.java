package mpdgr.ball;

public class Movement {
    private final double gravAcc; //gravitational acceleration - px per second^2
    private final double xVelocity; //initial horizontal speed - px per second
    private final double yVelocity; //initial vertical speed - px per second

    public Movement(double gravAcc, double xVelocity, double yVelocity) {
        this.gravAcc = gravAcc;
        this.xVelocity = xVelocity;
        this.yVelocity = yVelocity;
    }

    public Movement() {
        this.gravAcc = 10;
        this.xVelocity = 10;
        this.yVelocity = 10;
    }

    Position computePosition (Position p, int time){
        double startX = p.getX();
        double startY = p.getY();
        double endX = startX + xVelocity * time;
        double endY = startY + yVelocity + gravAcc * ((time * time) / 2.0);
        return new Position(endX, endY);
    }
}
