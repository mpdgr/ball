package mpdgr.ball;

public class Movement {
    private double gravAcc; //gravitational acceleration - px per second^2
    private double xVelocity; //initial horizontal speed - px per second
    private double yVelocity; //initial vertical speed - px per second
    private Position startingPosition;
    private long startingTime;
    private double energyLoss;

    public Movement(double gravAcc, double xVelocity, double yVelocity) {
        this.gravAcc = gravAcc;
        this.xVelocity = xVelocity;
        this.yVelocity = yVelocity;
    }

    public Movement(Position startingPosition) {
        this.gravAcc = -400;
        this.xVelocity = 100;
        this.yVelocity = 500;
        this.startingPosition = startingPosition;
        this.startingTime = System.currentTimeMillis();
        this.energyLoss = 0.1;
    }

    Position computePosition (long time1){
        long time = System.currentTimeMillis() - startingTime;
        double startX = startingPosition.getX();
//        System.out.println(startX);
        double startY = startingPosition.getY();
        double timeSec = time / 1000.0;
        double endX = startX + xVelocity * timeSec;
        double endY = startY - ((yVelocity * timeSec) + (gravAcc * timeSec * timeSec) / 2);
        if (endY >= 390){
            this.startingPosition = new Position(endX, endY);
            this.startingTime = System.currentTimeMillis();
            this.yVelocity *= (1 - energyLoss);
        }

//        if (endY <= 10){
//            this.startingPosition = new Position(endX, endY);
//            this.startingTime = System.currentTimeMillis();
//            this.yVelocity *= -(1 - energyLoss);
//        }
//
//        if (endX >= 790 || endX <= 10){
//            this.startingPosition = new Position(endX, endY);
//            this.startingTime = System.currentTimeMillis();
//            this.xVelocity *= -(1 - energyLoss);
//        }
//
//        if (endY >= 390 && yVelocity <= 2){
//            yVelocity = 0;
//        };

//        System.out.println(time);
        return new Position(endX, endY);
    }

    void speedUpBy(double factor){
        gravAcc *= factor;
        xVelocity *= factor;
        yVelocity *= factor;
    }

    public void setStartingPosition(Position startingPosition) {
        this.startingPosition = startingPosition;
    }
}
