package Neu.Network.model.component;

import Neu.Network.model.dao.StatisticGenerator;
import Neu.Network.model.flower.Irys;
import Neu.Network.model.layer.HiddenLayer;
import Neu.Network.model.layer.InputLayer;
import Neu.Network.model.layer.OutputLayer;
import java.io.Serializable;
import java.util.ArrayList;

public class NeuralNetwork implements Serializable {
    private final double learningFactor;
    private double momentumFactor = 0;
    private int epochs = 0;
    private double accuracy = 0.0;

    private final InputLayer inputLayer;
    private final HiddenLayer hiddenLayer;
    private final OutputLayer outputLayer;

    public NeuralNetwork(double learningFactor) {
        this.learningFactor = learningFactor;
        inputLayer = new InputLayer(4);
        hiddenLayer = new HiddenLayer(4,2);
        outputLayer = new OutputLayer(2,4);
    }

    public double getLearningFactor() {
        return learningFactor;
    }

    public double getMomentumFactor() {
        return momentumFactor;
    }

    public ArrayList<Double> calculate(Irys flower, boolean bias) {
        ArrayList<Double> input = inputLayer.calculate(flower);
        ArrayList<Double> hidden = hiddenLayer.calculate(input);
        ArrayList<Double> output = outputLayer.calculate(hidden);

        return output;
    }

    /**
     * method of weight change
     * @param epochsError value taken from the app layer
     * @param stopFlag  true -> epochs, false -> accuracy
     */

    public void train(Irys flower, boolean stopFlag, double epochsError, double momentumFactor) {
        if(stopFlag) {
            this.epochs = (int) epochsError;
        } else {
            this.accuracy = epochsError;
        }
        this.momentumFactor = momentumFactor;
        for (int i = 0; i < epochs; i++) {
            if(i%100 == 0) {
              //  StatisticGenerator.saveEpochStats(500,5.569);
            }
        }
        //TODO:train HERE

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
