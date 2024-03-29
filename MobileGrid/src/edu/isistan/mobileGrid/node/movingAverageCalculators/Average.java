package edu.isistan.mobileGrid.node.movingAverageCalculators;

import edu.isistan.mobileGrid.node.ConnectionScoreCalculator;

/**
 * moving average calculator implementation.
 */
public class Average implements ConnectionScoreCalculator {

    private ExponentialMovingAverage delegate = new ExponentialMovingAverage(1d);

    public Average(String[] args) {
    }

    public Average() {
    }

    @Override
    public void calculate(long time, boolean connect) {
        delegate.calculate(time, connect);
    }

    @Override
    public double getScore() {
        return delegate.getScore();
    }

    /*  private Double lastScore = null;
    private long lastMeasuredTime = 0;

    @Override
    public void average(long time, boolean connectionState) {
        if (lastScore == null) {
            lastScore = 0d;
            lastMesuredTime = time;
            return;
        }
        if (connectionState == CONNECT) {
            lastScore = (lastScore + (time - lastMesuredTime));
        } else {
            lastScore = (lastScore - (time - lastMesuredTime));
        }

    }

    @Override
    public double getScore() {
        return lastScore;
    }*/


}
