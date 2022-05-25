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
    private double fourType = 0.0;
    private ConfusionMatrix zeroSpecies = new ConfusionMatrix(0);
    private ConfusionMatrix firstSpecies = new ConfusionMatrix(1);
    private ConfusionMatrix secondSpecies = new ConfusionMatrix(2);
    private ConfusionMatrix thirdSpecies = new ConfusionMatrix(2);

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

        zeroSpecies.classificationLogic(networkResult,trueType);
        firstSpecies.classificationLogic(networkResult,trueType);
        secondSpecies.classificationLogic(networkResult,trueType);
        thirdSpecies.classificationLogic(networkResult,trueType);

        if(networkResult == 0) {
            firstType++;
        } else if(networkResult == 1) {
            secondType++;
        } else if(networkResult == 2){
            thirdType++;
        } else {
            fourType++;
        }
    }


    public void summarizeOfAllTypes() {
        System.out.println("\nIdentified number of flowers of the first species: "+ firstType);
        System.out.println("Identified number of flowers of the second species: " + secondType);
        System.out.println("Identified number of flowers of the third species: " + thirdType);
        System.out.println("Identified number of flowers of the third species: " + fourType);
        zeroSpecies.getInformation();
        firstSpecies.getInformation();
        secondSpecies.getInformation();
    }
}