package Neu.Network.helper;

import Neu.Network.model.flower.Iris;
import java.util.ArrayList;

public class LogicSummary {
    private int firstType = 0;
    private int secondType = 0;
    private int thirdType = 0;
    private int unidentified = 0;

    /**
     * method summarizing the result
     * @param result obtained data
     * @param flower object holding data about its type
     *
     * explanation:
     * 0 - 1 0 0 0
     * 1 - 0 1 0 0
     * 2 - 0 0 1 0
     */

    public void summarize(ArrayList<Double> result, Iris flower) {
        if(flower.getType() == 0) {
            if(result.get(0) > 0.8 && result.get(1) < 0.5 && result.get(2) < 0.5 && result.get(3) < 0.5) {
                System.out.println("Flower type FIRST found as expected, The significant factor has been reached:" + result.get(0));
                firstType++;
            } else {
                System.out.println("The obtained data did not clearly identify the type of flower");
                unidentified++;
            }
        } else if(flower.getType() == 1) {
            if(result.get(0) > 0.8 && result.get(1) < 0.5 && result.get(2) < 0.5 && result.get(3) < 0.5) {
                System.out.println("Flower type SECOND found as expected, The significant factor has been reached:" + result.get(0));
                secondType++;
            } else {
                System.out.println("The obtained data did not clearly identify the type of flower");
                unidentified++;
            }
        } else {
            if(result.get(0) > 0.8 && result.get(1) < 0.5 && result.get(2) < 0.5 && result.get(3) < 0.5) {
                System.out.println("Flower type THIRD found as expected, The significant factor has been reached:" + result.get(0));
                thirdType++;
            } else {
                System.out.println("The obtained data did not clearly identify the type of flower");
                unidentified++;
            }
        }
    }

    private void recallTest(double value) {

    }

    private double precision() {
        return (firstType/50 + secondType/50 + thirdType/50);
    }

    private double recall() {
        return (firstType/150 + secondType/150 + thirdType/150);
    }

    private double fMeasure(double precision, double recall) {
        return 2*precision*recall/(precision+recall);
    }

    public void summarizeOfAllTypes() {
        System.out.println("\nIdentified number of flowers of the first species: "+ firstType);
        System.out.println("Identified number of flowers of the second species: " + secondType);
        System.out.println("Identified number of flowers of the third species: " + thirdType);
        System.out.println("unidentified flowers: " + unidentified);
        double precision = precision();
        System.out.println("Precision: "+ precision);
        double recall = recall();
        System.out.println("Recall: "+ precision);
        System.out.println("F-Measure: " + fMeasure(precision,recall));

    }
}
