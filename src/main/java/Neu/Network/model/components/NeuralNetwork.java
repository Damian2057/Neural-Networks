package Neu.Network.model.components;

import Neu.Network.model.flower.Iris;
import java.io.Serializable;
import java.util.ArrayList;

public class NeuralNetwork implements Serializable {
    private final double learningFactor;
    private double momentumFactor = 0;
    private int epochs = 0;
    private double accuracy = 0.0;
    private final Layer hiddenNeurons;
    private final Layer outPutNeurons;
    private final Layer hiddenBias;
    private final Layer outPutBias;

    public NeuralNetwork(int numberOfInPuts, int numberOfHiddenNeurons, int numberOfOutPuts ,double learningFactor) {
        this.learningFactor = learningFactor;
        hiddenNeurons = new Layer(numberOfHiddenNeurons, numberOfInPuts);
        outPutNeurons = new Layer(numberOfOutPuts, numberOfHiddenNeurons);
        hiddenBias = new Layer(numberOfHiddenNeurons,1);
        outPutBias = new Layer(numberOfOutPuts,1);
    }

    public double getLearningFactor() {
        return learningFactor;
    }

    public double getMomentumFactor() {
        return momentumFactor;
    }

    public ArrayList<Double> calculate(Iris flower, boolean bias) {
        Layer inPut = Layer.toLayer(flower);
        Layer hiddenOutPut = Layer.multiply(hiddenNeurons,inPut);
        if(bias) {
            hiddenOutPut.add(hiddenBias);
        }
        hiddenOutPut.sigmoid();

        Layer outPut = Layer.multiply(outPutNeurons,hiddenOutPut);

        if(bias) {
            outPut.add(outPutBias);
        }
        outPut.sigmoid();
        return outPut.toArray();
    }

    /**
     * method of weight change
     * @param epochsError value taken from the app layer
     * @param stopFlag  true -> epochs, false -> accuracy
     */

    public void train(Iris flower, boolean stopFlag, double epochsError, double momentumFactor) {

        if(!stopFlag) {
            //Error here (accuracy)
        }


        for (int i = 0; i < epochs; i++) {
            if(i % 100 == 0) {
                //StatisticGenerator.saveEpochStats(GlobalConfiguration.epochsToCollect,5.569);
            }

        }
    }

    public void trainByEpochs(ArrayList<Iris> data, int epochs, double momentumFactor, boolean method) {
        this.epochs = epochs;
        this.momentumFactor = momentumFactor;
    }

    public void trainByAccurany(ArrayList<Iris> data, double accuracy, double momentumFactor, boolean method) {
        this.accuracy = accuracy;
        this.momentumFactor = momentumFactor;
    }


    public int getEpochs() {
        return epochs;
    }

    private void calculateError() {

    }

    public double getAccuracy() {
        return accuracy;
    }

    public void showInformation() {
        if(epochs > 0 ) {
            System.out.println("\nlearning factor: " + getLearningFactor()
                    + "\nmomentum factor: " + getMomentumFactor()
                    +"\nTaught on: " + getEpochs() + " epochs");
        } else {
            System.out.println("\nlearning factor: " + getLearningFactor()
                    + "\nmomentum factor: " + getMomentumFactor()
                    +"\nTaught with accuracy: "+ getAccuracy());
        }
    }
}
