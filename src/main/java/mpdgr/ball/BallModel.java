package mpdgr.ball;

public class BallModel {

    private Physics physics;
    private Position startingPosition;

    private double xVelocity;           //horizontal speed - px per second
    private double yVelocity;           //vertical speed - px per second

    private long startingTime;
    private long startRollingTime;

    private enum Direction {LEFT, RIGHT};
    private Direction direction;

    private boolean rollingStarted = false;
    private boolean xBounceLocked = false;
    private boolean yBounceLocked = false;
    private boolean isRolling = false;

    private boolean terminated = false;

    public BallModel(Physics physics, Position startingPosition, double xVelocity, double yVelocity) {
        this.physics = physics;
        this.startingPosition = startingPosition;
        this.xVelocity = xVelocity;
        this.yVelocity = yVelocity;
    }

    public BallModel(Position startingPosition) {
        this.physics = new Physics();
        physics.setGravAcc(- 1000);
        this.xVelocity = 300;
        this.yVelocity = 1000;
        this.startingPosition = startingPosition;
        this.startingTime = System.currentTimeMillis();
        this.startRollingTime = startingTime;
        physics.setEnergyLoss(0.1);
//        this.energyLoss = 0.1;
        physics.setRollingResistance(- 0.5);
//        this.rollingResistance = - 0.5;
        this.isRolling = (yVelocity == 0);
        this.direction = Direction.RIGHT;
    }

    Position computePosition (){
        long time = System.currentTimeMillis() - startingTime;
        long rollTime = System.currentTimeMillis() - startRollingTime;
          double startX = startingPosition.getX();
        double startY = startingPosition.getY();
        double timeSec = time / 1000.0;


        /*X speed component calculation-------*/

        double resistanceCorrection = physics.getRollingResistance() * Math.pow(rollTime / 1000.0, 2) / 2;
        resistanceCorrection = xVelocity < 0 ?  - resistanceCorrection : resistanceCorrection;

        double endX = isRolling ?
                startX + xVelocity * timeSec + resistanceCorrection :
                startX + xVelocity * timeSec;

        /*Y speed component calculation-------*/

        double endY = startY - ((yVelocity * timeSec) + (physics.getGravAcc() * Math.pow(timeSec, 2)) / 2);

        if (isRolling && ((xVelocity * timeSec + resistanceCorrection < 0 && direction == Direction.RIGHT)
                || (xVelocity * timeSec + resistanceCorrection > 0 && direction == Direction.LEFT))) {
            terminated = true;
            System.out.println("stop");
        }

        /*floor bounce-----------------------*/

        if (endY >= 390 && !yBounceLocked){
            startingPosition = new Position(endX, endY);
            startingTime = System.currentTimeMillis();
            yVelocity = currentYVelocity(timeSec);
            yVelocity *= (1 - physics.getEnergyLoss());
            yVelocity = Math.abs(yVelocity);
            yBounceLocked = true; //lock prevents multiple bounce during one contact with the floor
        }
        else {
            yBounceLocked = false;
        }

        /*wall bounce------------------------*/

        if ((endX >= 790 || endX <= 10) && !xBounceLocked ){
            startingPosition = new Position(endX, endY);
            startingTime = System.currentTimeMillis();
            startRollingTime = startingTime;
            xVelocity *= - (1 - physics.getEnergyLoss());
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

        /*roll start---------------------------*/

        if (endY >= 390 && currentYVelocity() <= 10 && currentYVelocity() >= -10){
            yVelocity = 0;
            physics.setGravAcc(0);
            isRolling = true;
            endY = 390;
            if (!rollingStarted){
                startRollingTime = System.currentTimeMillis();
                System.out.println("rolling");
            }
            rollingStarted = true;
        }
        return new Position(endX, endY);
    }

    void timeStart() {
        startingTime = System.currentTimeMillis();
    }
    double currentYVelocity(double timeSec){
        double currentYVelocity = yVelocity + (physics.getGravAcc() * timeSec);
        return isRolling ? 0 : currentYVelocity;
    }

    double currentYVelocity(){
        long time = System.currentTimeMillis() - startingTime;
        double timeSec = time / 1000.0;
        return yVelocity + (physics.getGravAcc() * timeSec);
    }

    public boolean isTerminated() {
        return terminated;
    }

    public void setTerminated(boolean terminated) {
        this.terminated = terminated;
    }

    public void setPhysics(Physics physics) {
        this.physics = physics;
    }

    public void setStartingPosition(Position startingPosition) {
        this.startingPosition = startingPosition;
    }

    public void setxVelocity(double xVelocity) {
        this.xVelocity = xVelocity;
    }

    public void setyVelocity(double yVelocity) {
        this.yVelocity = yVelocity;
    }

    public double getxVelocity() {
        return xVelocity;
    }

    public double getyVelocity() {
        return yVelocity;
    }
}




//TODO: air resistance
//
//    double currentXVelocity(double timeSec) {
//        long rollTime = System.currentTimeMillis() - startRollingTime;
//        double currentX;
//
//        if (isRolling) {
//            double resistanceCorrection = rollingResistance * rollTime / 50;
//            resistanceCorrection = xVelocity < 0 ? - resistanceCorrection : resistanceCorrection;
//            currentX = xVelocity + resistanceCorrection;
//        }
//        else {
//            currentX = xVelocity;
//        }
//        return currentX;
//    }