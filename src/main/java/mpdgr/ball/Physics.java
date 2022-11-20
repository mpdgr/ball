package mpdgr.ball;

public class Physics {
    private double gravAcc;             //gravitational acceleration - px per second^2
    private double energyLoss;          //% of energy loss when ball bounces
    private double rollingResistance;   //speed loss factor per second while rolling

    public Physics(double gravAcc, double energyLoss, double rollingResistance) {
        this.gravAcc = gravAcc;
        this.energyLoss = energyLoss;
        this.rollingResistance = rollingResistance;
    }

    public Physics() {
    }

    public double getGravAcc() {
        return gravAcc;
    }

    public void setGravAcc(double gravAcc) {
        this.gravAcc = gravAcc;
    }

    public double getEnergyLoss() {
        return energyLoss;
    }

    public void setEnergyLoss(double energyLoss) {
        this.energyLoss = energyLoss;
    }

    public double getRollingResistance() {
        return rollingResistance;
    }

    public void setRollingResistance(double rollingResistance) {
        this.rollingResistance = rollingResistance;
    }
}
