package Neu.Network.model.component;

import Neu.Network.model.flower.Irys;
import Neu.Network.model.layer.HiddenLayer;
import Neu.Network.model.layer.InputLayer;
import Neu.Network.model.layer.OutputLayer;
import java.io.Serializable;
import java.util.ArrayList;

public class NeuralNetwork implements Serializable {
    private final double learningFactor;
    private final double momentumFactor;

    private final InputLayer inputLayer;
    private final HiddenLayer hiddenLayer;
    private final OutputLayer outputLayer;

    public NeuralNetwork(double learningFactor, double momentumFactor) {
        this.learningFactor = learningFactor;
        this.momentumFactor = momentumFactor;
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

    public ArrayList<Double> calculate(Irys flower) {
        ArrayList<Double> input = inputLayer.calculate(flower);
        ArrayList<Double> hidden = hiddenLayer.calculate(input);
        ArrayList<Double> output = outputLayer.calculate(hidden);

        return output;
    }

    public void train(Irys flower, int epochs) {

    }
}
