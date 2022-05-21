package Neu.Network.summary;

import Neu.Network.model.flower.EncoderData;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.Collections;

public class SummaryCalculator {

    private double truePositives = 0.0;
    private double trueNegatives = 0.0;
    private double falsePositives = 0.0;
    private double falseNegatives = 0.0;
    private double firstType = 0.0;
    private double secondType = 0.0;
    private double thirdType = 0.0;

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

    public void summarize(@NotNull ArrayList<Double> result, EncoderData flower) {

        int networkResult = result.indexOf(Collections.max(result));
        int trueType = flower.indexOfMax();

        int[] type = new int[3];
        int[] actual = new int[3];

        type[networkResult] = 1;
        actual[trueType] = 1;

        for (int i = 0; i < type.length; i++) {
            if(type[i] == 1) {
                if(type[i] == actual[i]) truePositives++;
                else if(type[i] != actual[i]) trueNegatives++;
            } else {
                if(actual[i] == 1) falsePositives++;
                else if(actual[i] != 1) falseNegatives++;
            }
        }

        if(networkResult == 0) {
            firstType++;
        } else if(networkResult == 1) {
            secondType++;
        } else {
            thirdType++;
        }
    }

    private double precision() {
        return (truePositives)/(truePositives + falsePositives);
    }

    private double recall() {
        return truePositives /(truePositives + falseNegatives);
    }

    private double fMeasure(double precision, double recall) {
        return 2*precision*recall/(precision+recall);
    }

    public void summarizeOfAllTypes() {
        System.out.println("\nIdentified number of flowers of the first species: "+ firstType);
        System.out.println("Identified number of flowers of the second species: " + secondType);
        System.out.println("Identified number of flowers of the third species: " + thirdType);
        double precision = precision();
        System.out.println("Precision: "+ precision);
        double recall = recall();
        System.out.println("Recall: "+ recall);
        System.out.println("F-Measure: " + fMeasure(precision,recall));
    }
}