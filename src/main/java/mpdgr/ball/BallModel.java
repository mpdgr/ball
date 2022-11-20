package mpdgr.ball;

public class BallModel {
    private double gravAcc;             //gravitational acceleration - px per second^2
    private double xVelocity;           //horizontal speed - px per second
    private double yVelocity;           //vertical speed - px per second

    private Position startingPosition;
    private long startingTime;
    private double energyLoss;          //% of energy loss when ball bounces
    private double rollingResistance;   //speed loss factor per second while rolling
    private boolean isRolling = false;

    private boolean rollingStarted = false;
    private long startRollingTime;

    private boolean ballStopped = false;
    private double lastSeenX;
    private double lastSeenY;
    private enum Direction {LEFT, RIGHT};
    private Direction direction;

    private boolean xBounceLocked = false;
    private boolean yBounceLocked = false;

    private boolean terminated = false;


    public BallModel(double gravAcc, double xVelocity, double yVelocity) {
        this.gravAcc = gravAcc;
        this.xVelocity = xVelocity;
        this.yVelocity = yVelocity;
    }

    public BallModel(Position startingPosition) {
        this.gravAcc = - 1000;
        this.xVelocity = 150;
        this.yVelocity = 400;
        this.startingPosition = startingPosition;
        this.startingTime = System.currentTimeMillis();
        this.startRollingTime = startingTime;
        this.energyLoss = 0.1;
        this.rollingResistance = - 0.5;
        this.isRolling = (yVelocity == 0);
        this.lastSeenX = startingPosition.getX();
        this.lastSeenY = startingPosition.getY();
        this.direction = Direction.RIGHT;
    }

    Position computePosition (){
        long time = System.currentTimeMillis() - startingTime;
        long rollTime = System.currentTimeMillis() - startRollingTime;
        double startX = startingPosition.getX();
        double startY = startingPosition.getY();
        double timeSec = time / 1000.0;
        System.out.println(rollTime);
        double resistanceCorrection = rollingResistance * Math.pow(rollTime / 1000.0, 2) / 2;
        System.out.println(resistanceCorrection
        );
//        resistanceCorrection = resistanceCorrection >= 0 ? 0 : resistanceCorrection;
        resistanceCorrection = xVelocity < 0 ?  - resistanceCorrection : resistanceCorrection;
//        System.out.println(resistanceCorrection);
        double endX = isRolling ?
                startX + xVelocity * timeSec + resistanceCorrection :
                startX + xVelocity * timeSec;
//        double endX = startX + xVelocity * timeSec;
        double endY = startY - ((yVelocity * timeSec) + (gravAcc * timeSec * timeSec) / 2);

        double currentXV = currentXVelocity(timeSec);
//        System.out.println(currentXV);
//        System.out.println(xVelocity);
//        if ((currentXV <= 0 && direction == Direction.RIGHT) || (currentXV >= 0 && direction == Direction.LEFT) && isRolling) {
//        if (endX == startX && isRolling) {
                if (isRolling && (((xVelocity * timeSec + resistanceCorrection < 0) && direction == Direction.RIGHT) || ((xVelocity * timeSec + resistanceCorrection > 0) && direction == Direction.LEFT))) {
            terminated = true;
                    System.out.println("stop");
//            return new Position(lastSeenX, lastSeenY);
        }

        //floor bounce
        if (endY >= 390 && !yBounceLocked){
            this.startingPosition = new Position(endX, endY);
            this.startingTime = System.currentTimeMillis();
//            this.startRollingTime = System.currentTimeMillis();
            this.yVelocity = currentYVelocity(timeSec);
            this.yVelocity *= (1 - energyLoss);
            this.yVelocity = Math.abs(yVelocity);
            yBounceLocked = true; //lock prevents multiple bounce during one contact with the floor
//            System.out.println("bounce");
        }
        else {
            yBounceLocked = false;
        }

        //wall bounce
        if ((endX >= 790 || endX <= 10) && !xBounceLocked ){
            startingPosition = new Position(endX, endY);
            startingTime = System.currentTimeMillis();
            startRollingTime = startingTime;
            xVelocity *= - (1 - energyLoss);
            if (xVelocity >= 0){
                direction = Direction.RIGHT;
            }
            else {
                direction = Direction.LEFT;
            }
            yVelocity = currentYVelocity(timeSec);
            xBounceLocked = true; //lock prevents multiple bounce during one contact with the wall
        }
        else {
            xBounceLocked = false;
        }

        //ball starts rolling
        if (endY >= 390 && currentYVelocity() <= 10 && currentYVelocity() >= -10){
            yVelocity = 0;
            gravAcc = 0;
            isRolling = true;
            endY = 390;
            if (!rollingStarted){
                startRollingTime = System.currentTimeMillis();
//                System.out.println("rolling");
//                System.out.println("xvel" + xVelocity);
//                System.out.println("curxvel" + currentXVelocity(timeSec));
            }
            rollingStarted = true;
        }

        lastSeenX = startX;
        lastSeenY = startY;

        return new Position(endX, endY);
    }

    double currentXVelocity(double timeSec){


        long rollTime = System.currentTimeMillis() - startRollingTime;

        double currentX;
        if (isRolling){
//            System.out.println(rollTime + "rtime");
            double resistanceCorrection = rollingResistance * rollTime / 50;
//        resistanceCorrection = resistanceCorrection >= 0 ? 0 : resistanceCorrection;
            resistanceCorrection = xVelocity < 0 ?  - resistanceCorrection : resistanceCorrection;

//

//            double resistanceCor = rollingResistance * timeSec;
//            resistanceCor = resistanceCor <= 0 ? 0 : resistanceCor;
//            resistanceCor = xVelocity < 0 ? - resistanceCor : resistanceCor;
            currentX = xVelocity + resistanceCorrection;
//            System.out.println(currentX);
//            currentX = currentX <= 0 ? 0 : currentX;
        } else {
            currentX = xVelocity;
        }
        return currentX;
    }

    double currentYVelocity(double timeSec){
        double currentYVelocity = yVelocity + (gravAcc * timeSec);
        return isRolling ? 0 : currentYVelocity;
    }

    double currentYVelocity(){
        long time = System.currentTimeMillis() - startingTime;
        double timeSec = time / 1000.0;
        return yVelocity + (gravAcc * timeSec);
    }

    public boolean isTerminated() {
        return terminated;
    }
}

//TODO: wind resistance factor
