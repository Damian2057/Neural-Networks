package Neu.Network.summary;

import Neu.Network.model.flower.Iris;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class SummaryCalculator {
    private double firstType = 0;
    private double firstTypeTrue = 0;
    private double secondType = 0;
    private double secondTypeTrue = 0;
    private double thirdType = 0;
    private double thirdTypeTrue = 0;
    private double unidentified = 0;

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

    public void summarize(@NotNull ArrayList<Double> result, Iris flower) {

        if(result.get(0) > result.get(1) && result.get(0) > result.get(2)) {
            if(flower.getType() == 0) {
                firstTypeTrue++;
            } else {
                firstType++;
                unidentified++;
            }
        } else if(result.get(1) > result.get(0) && result.get(1) > result.get(2)) {
            if(flower.getType() == 1) {
                secondTypeTrue++;
            } else {
                secondType++;
                unidentified++;
            }
        } else if(result.get(2) > result.get(0) && result.get(2) > result.get(1))  {
            if(flower.getType() == 2) {
                thirdTypeTrue++;
            } else {
                thirdType++;
                unidentified++;
            }
        } else {
            unidentified++;
        }
    }

    private double precision() {
        return (firstTypeTrue+secondTypeTrue+thirdTypeTrue)/(firstTypeTrue+secondTypeTrue+thirdTypeTrue+unidentified);
    }

    private double recall() {
        return (firstTypeTrue+secondTypeTrue+thirdTypeTrue)
                / ((firstTypeTrue+secondTypeTrue+thirdTypeTrue) + (firstType+secondType+thirdType));
    }

    private double fMeasure(double precision, double recall) {
        return 2*precision*recall/(precision+recall);
    }

    public void summarizeOfAllTypes() {
        System.out.println("\nIdentified number of flowers of the first species: "+ firstTypeTrue);
        System.out.println("Identified number of flowers of the second species: " + secondTypeTrue);
        System.out.println("Identified number of flowers of the third species: " + thirdTypeTrue);
        System.out.println("unidentified flowers: " + unidentified);
        double precision = precision();
        System.out.println("Precision: "+ precision);
        double recall = recall();
        System.out.println("Recall: "+ recall);
        System.out.println("F-Measure: " + fMeasure(precision,recall));
    }
}