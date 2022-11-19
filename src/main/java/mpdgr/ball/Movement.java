package mpdgr.ball;

public class Movement {
    private double gravAcc; //gravitational acceleration - px per second^2
    private double xVelocity; //initial horizontal speed - px per second
    private double yVelocity; //initial vertical speed - px per second

    private Position startingPosition;
    private long startingTime;
    private double energyLoss;
    private double rollingResistance; //speed loss factor per second while rolling
    private boolean isRolling = false;

    private boolean yBounceLocked = false;
    private boolean xBounceLocked = false;

    public Movement(double gravAcc, double xVelocity, double yVelocity) {
        this.gravAcc = gravAcc;
        this.xVelocity = xVelocity;
        this.yVelocity = yVelocity;
    }

    public Movement(Position startingPosition) {
        this.gravAcc = -400;
        this.xVelocity = 200;
        this.yVelocity = -600;
        this.startingPosition = startingPosition;
        this.startingTime = System.currentTimeMillis();
        this.energyLoss = 0.1;
        this.rollingResistance = 1;
        this.isRolling = (yVelocity == 0);
    }

    Position computePosition (long time1){
        long time = System.currentTimeMillis() - startingTime;
        double startX = startingPosition.getX();
//        System.out.println(startX);
        double startY = startingPosition.getY();
        double timeSec = time / 1000.0;
        double endX = startX + xVelocity * timeSec;
        double endY = startY - ((yVelocity * timeSec) + (gravAcc * timeSec * timeSec) / 2);

        if (endY >= 390 && !yBounceLocked){
            this.startingPosition = new Position(endX, endY);
            this.startingTime = System.currentTimeMillis();
            this.yVelocity = currentYVelocity(timeSec);
            this.yVelocity *= (1 - energyLoss);
            this.yVelocity = Math.abs(yVelocity);
            yBounceLocked = true;
            System.out.println("bounce");
        } //else if (endY >= 390){}
        else {
            yBounceLocked = false;
        }

//        if (endY <= 10){
//            this.startingPosition = new Position(endX, endY);
//            this.startingTime = System.currentTimeMillis();
//            this.yVelocity *= -(1 - energyLoss);
//        }
//
        if ((endX >= 790 || endX <= 10) && !xBounceLocked ){
            this.startingPosition = new Position(endX, endY);
            this.startingTime = System.currentTimeMillis();
            this.xVelocity *= - (1 - energyLoss);
//            System.out.println(yVelocity);
            this.yVelocity = currentYVelocity(timeSec);
            xBounceLocked = true;
        } //else if (endX >= 790 || endX <= 10){}
        else {
            xBounceLocked = false;
        }
//
        if (endY >= 390 && currentYVelocity() <= 1 && currentYVelocity() >= -1){
            this.yVelocity = 0;
            this.gravAcc = 0;
            this.isRolling = true;
            System.out.println("rolling");
        }

        if (isRolling) {
            endY = 390;
        }
//        System.out.println(yVelocity);
//        System.out.println(time);
        return new Position(endX, endY);
    }

    double currentXVelocity(){
        long time = System.currentTimeMillis() - startingTime;
        double timeSec = time / 1000.0;
        double currentXVelocity;
        if (isRolling){
            currentXVelocity = xVelocity - (rollingResistance * timeSec);
            currentXVelocity = currentXVelocity <= 0 ? 0 : currentXVelocity;
        } else {
            currentXVelocity = xVelocity; //TODO: wind resistance factor
        }
        return currentXVelocity;
    };


    double currentYVelocity(double timeSec){
        double currentYVelocity = yVelocity + (gravAcc * timeSec);
        return isRolling ? 0 : currentYVelocity;
    };

    double currentYVelocity(){
        long time = System.currentTimeMillis() - startingTime;
        double timeSec = time / 1000.0;
        double currentYVelocity = yVelocity + (gravAcc * timeSec);
//        System.out.println(currentYVelocity);
        return currentYVelocity;
    };

    void speedUpBy(double factor){
        gravAcc *= factor;
        xVelocity *= factor;
        yVelocity *= factor;
    }

    public void setStartingPosition(Position startingPosition) {
        this.startingPosition = startingPosition;
    }
}


//TODO: wind resistance factor
//TODO: wall bounce energy loss