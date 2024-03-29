package edu.isistan.seas.util;

public class ProgressiveStandardizer {

    private double standardDeviation = 0;
    private double average = 0;
    private int amountOfSamples = 0;
    private int sumOfPowedSamples = 0;

    private void addValue(double value) {
        average = (average * amountOfSamples++ + value) / amountOfSamples;
        //for the standar deviation
        if (amountOfSamples <= 1) standardDeviation = 0;
        else {
            sumOfPowedSamples += Math.pow(value, 2);
            double pow=Math.abs(sumOfPowedSamples * amountOfSamples - (Math.pow(average, 2))) / (amountOfSamples * (amountOfSamples - 1));
            standardDeviation = Math.sqrt(pow);
        }
    }

    public double getStandar(double value) {
        addValue(value);
        return (standardDeviation==0?0:((value - average) / standardDeviation));
    }
}
