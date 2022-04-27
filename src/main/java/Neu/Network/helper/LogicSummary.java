package Neu.Network.helper;

import Neu.Network.model.flower.Iris;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class LogicSummary {
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

        if((result.get(0) > 0.8 && result.get(1) > 0.8 && result.get(2) > 0.8 && result.get(3) > 0.8)
        || (result.get(0) < 0.8 && result.get(1) < 0.8 && result.get(2) < 0.8 && result.get(3) < 0.8)
        || (result.get(0) < 0.8 && result.get(1) > 0.8 && result.get(2) > 0.8)
        || (result.get(0) > 0.8 && result.get(1) < 0.8 && result.get(2) > 0.8)
        || (result.get(0) > 0.8 && result.get(1) > 0.8 && result.get(2) < 0.8)) {
            unidentified++;
        } else if(result.get(0) > 0.8) {
            if(flower.getType() == 0) {
                firstTypeTrue++;
            } else {
                firstType++;
                unidentified++;
            }
        } else if(result.get(1) > 0.8) {
            if(flower.getType() == 1) {
                secondTypeTrue++;
            } else {
                secondType++;
                unidentified++;
            }
        } else if(result.get(2) > 0.8) {
            if(flower.getType() == 2) {
                thirdTypeTrue++;
            } else {
                thirdType++;
                unidentified++;
            }
        }

//        if(flower.getType() == 0) {
//            if(result.get(0) > 0.8 && result.get(1) < 0.5 && result.get(2) < 0.5 && result.get(3) < 0.5) {
//                System.out.println("Flower type FIRST found as expected, The significant factor has been reached:" + result.get(0));
//                firstType++;
//            } else {
//                System.out.println("The obtained data did not clearly identify the type of flower");
//                unidentified++;
//            }
//        } else if(flower.getType() == 1) {
//            if(result.get(0) < 0.5 && result.get(1) > 0.8 && result.get(2) < 0.5 && result.get(3) < 0.5) {
//                System.out.println("Flower type SECOND found as expected, The significant factor has been reached:" + result.get(0));
//                secondType++;
//            } else {
//                System.out.println("The obtained data did not clearly identify the type of flower");
//                unidentified++;
//            }
//        } else {
//            if(result.get(0) < 0.5 && result.get(1) < 0.5 && result.get(2) > 0.8 && result.get(3) < 0.5) {
//                System.out.println("Flower type THIRD found as expected, The significant factor has been reached:" + result.get(0));
//                thirdType++;
//            } else {
//                System.out.println("The obtained data did not clearly identify the type of flower");
//                unidentified++;
//            }
//        }
    }

    private double precision() {
        return (firstTypeTrue+secondTypeTrue+thirdTypeTrue)/(firstTypeTrue+secondTypeTrue+thirdTypeTrue+unidentified);
    }

    private double recall() {
        double t = (firstTypeTrue+secondTypeTrue+thirdTypeTrue);
        double u = (firstType+secondType+thirdType);
        return t/(t+u);
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
